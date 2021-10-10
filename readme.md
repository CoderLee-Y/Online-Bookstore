# Log In SE3353

## Log in Oct.6

### 测试提示

- 访问`/getHomePage`视为一次访问，计入一次访客量；可以使用Jmeter建立线程组访问
- 正常使用Book相关的功能，可以使用redis client查看现有内存缓存

### 访客统计

使用Jmeter并发测试，100线程并发10次。访问前:

<img src="https://i.loli.net/2021/10/01/puHP7XSBoWcglbi.png" alt="image-20211001124633457" style="zoom:50%;" />

测试之后：

<img src="https://i.loli.net/2021/10/01/kaCuIMPlb8tqhWf.png" alt="image-20211001124655982" style="zoom:50%;" />

考虑到测试后登陆网站又会多一次，所以是准确的。

### 内存数据库

使用Redis数据库，对一致性的控制方法是：

- 管理与Add书籍会往数据库中写，然后往内存中写
- 对书籍的删改操作会往数据库写，然后invalid对应书籍缓存
- 每次查看书籍详情先去内存数据库找，如果失配再去db找并且刷到内存中
- 避免内存数据库的频繁读写造成性能下降，我的做法是：
  - 顾客下单，使用数据库数据检查库存，减小数据库内库存，保证事务性
  - 下单不改变内存数据库的库存值
  - 内存设置60s的过期时间
  - 这样，顾客在商品详情页的库存数据可能是不准确的，只有60s的最终一致性
  - 但是，价格等敏感数据是强一致的
  - 由于数据库的一致性和事务，不会出现一本书卖给多人的情况
  - 保证了内存数据库不会被频繁擦写

测试结果如下，两次查找中间访问了一下书本(id == 2). 

<img src="https://i.loli.net/2021/10/01/AGcHlfhSx5Dg8P4.png" alt="image-20211001164330172" style="zoom:67%;" />

此外还测试了增删改对内存数据库的一致性检查，发现加入Redis之后仍然保持了最终一致性。

## Log in Sept.27

### 聊天室

实现了一个客服功能：

- 用户自动接入客服账号聊天
- 客服根据用户发起的会话进行聊天，不能主动发起聊天

![image-20210924003439075](https://i.loli.net/2021/09/24/ewgy1s6jTIhbqtv.png)

### 事务属性说明-以下订单为例

下订单在本系统中分为2步：

2. 检查并扣除库存 reduceInventory(); 如果检查库存失败则返回。
3. 生成订单 createOrder()。

我们最终的事务是：下订单过程中保持REQUIRED事务属性，全过程保持不可重复读隔离属性。好处是：

- REQUIRED不会造成减了库存，下订单失败的情景
- 写锁不会造成减库存并下订单前别人减库存的场景，不会造成检查时有库存，下完单没库存这种不可重复读场景

#### REQUIRED

一路的事务属性都是REQUIRED，最终下单成功。<img src="https://i.loli.net/2021/09/27/mCjyJHpP6a9Fekb.png" alt="image-20210927190640170" style="zoom:67%;" />

#### 异常状况

下面将会在1-2步中间抛出一次异常。我们使用不同的隔离属性观察，

```java
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void handleOrder(@Payload String str) {
    // Get kafka message attributes here
      // check and reduce inventory
    if(bookDao.reduceInventory(book_id, amount) < 0)
      return;
      
    else if(user_id > -2) // Forever true
      throw new NullPointerException();
    cartDao.createOrder(user_id, book_id, amount, price);
  }
```

在此过程中，Repository会保持默认Transactional状态，设置为REQUIRED. 

##### 1

```C++
REQUIRED // handleOrder
	REQUIRED // reduceInventory
	REQUIRED // createOrder
```

<img src="https://i.loli.net/2021/09/27/p5YCbDRnaJfSslz.png" alt="image-20210927191358039" style="zoom: 67%;" />

上图为购买前的库存。由于Kafka的默认设置，这条消息失败会被重新消费10次，然后我们得到的仍然是上图的库存，订单和订单项也没有成功生成。说明事务达到了预期目标。

##### 2

```c++
REQUIRED // handleOrder
	REQUIRED_NEW // reduceInventory
	REQUIRED_NEW // createOrder
```

下面会创建新事务，按照定义，减完库存事务会被提交，那么按理说应该减少10库存，实验结果符合预期。

![image-20210927193549374](https://i.loli.net/2021/09/27/XPOQo3nqWvwMZIp.png)

<img src="https://i.loli.net/2021/09/27/kesY4VhONQMIZ5z.png" alt="image-20210927193618950" style="zoom:80%;" />

##### 3

```C++
REQUIRED // handleOrder
	NOT_SUPPORTED // reduceInventory
	NOT_SUPPORTED // createOrder
```

NOT SUPPORTED也会把上层事务挂起，然后不按事务执行，也不会回滚，库存减少10，如下图：

<img src="https://i.loli.net/2021/09/27/lnQTUCf2gxw5EmF.png" alt="image-20210927192013518" style="zoom:80%;" />

<img src="https://i.loli.net/2021/09/27/IcOmzQygAwPDWex.png" alt="image-20210927193347248" style="zoom:80%;" />

##### 4

最后测试一下强制不执行事务的情况。

```c++
REQUIRED // handleOrder
	NEVER // reduceInventory
	NOT_SUPPORTED // createOrder
```

最后根本不会执行减少库存，会抛异常。而且抛异常不是我设定的异常。

![image-20210927193911652](https://i.loli.net/2021/09/27/BQ527svgmcRFUWE.png)

## Log in Sept.22

- 完成了有状态服务改造：将下订单Service设定为有状态，ProtoType设计模式；
  - 不同实例检查库存后查询Session获取用户信息
  - 更好的应对抢单等高并发场景
- 完成了下订单的消息队列改造，使用了Kafka

# Online bookstore

A secure, fully functional and (maybe) efficient online bookstore Apps. It's part of project in 2021-spring SJTU SE122 web application project, which directed by Prof Chen Haopeng.

## Tech stack

In frontend, I use JSX and React, with some cascaded and necessary `.css` file to organize page. The front-end library I used is [Ant designed](https://ant.design/). What's more, I used some similar-styled bootstrap components and icons to prettier my website.

In backend, I use Spring boot, which is hierarchical and  implementing-interface-separated. Spring JPA is used to get access to data source. Only sql is applied in this project.

As for pack tools, in frontend I use webpack and maven in backend. Jetbrains IDEA is used over dev-process.

## Advanced topics

1. ORM: **Object Relational Mapping** is extensively used in my project. For example, in order table, we store address and order time which is identical in the same order. And table hold a list table record, which include every book information in the order.
2. In frontend, apply component-developing with react-component.
3. Well designed database, including index of frequently-used names, decimal to store money and ORM + cascade access.
4. Security topics: never send password to frontend, use session to guarantee security and design CORS to avoid HTTP Hack. Never use urls to pass important info and use tokens to avoid cheat HTTP.
5. As a mature system, apply IOC and ORM, has package of `Controller, Service, Dao, Repository, Entity.` Impl and interface are independently designed.

## How to run

### frontend

```shell
npm install
npm start
```

### backend

1. First change application.properties to change sql admin names and password, and run bookstore.sql.
2. Then use maven to add dependencies.
3. Run my backend projects.

## Contact with me

`yiyanleee@gmail.com`



