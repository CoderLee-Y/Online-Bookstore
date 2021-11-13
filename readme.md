# Log In SE3353

## Contact with me

`yiyanleee@gmail.com`

## Log on Nov.12

### 测试提示

- 运行第一个Module，您只需要新增MongoDB的数据库并运行在27017默认端口即可；

- 运行Neo4j, 您需要新增图数据库并且运行在7687端口。

  ```cypher
  CREATE (编程:label{name:'编程'})
  CREATE (儿童文学:label{name:'儿童文学'})
  CREATE (世界名著:label{name:'世界名著'})
  CREATE (社会小说:label{name:'社会小说'})
  CREATE (青春文学:label{name:'青春文学'})
  CREATE (中小学教辅:label{name:'中小学教辅'})
  CREATE (武侠小说:label{name:'武侠小说'})
  CREATE (魔幻小说:label{name:'魔幻小说'})
  CREATE (古籍:label{name:'古籍'})
  CREATE (人物传记:label{name:'人物传记'})
  ```

  

### Mongo DB与评论

为书籍增加了评论功能，任何用户都可以在登陆后发表评论，游客也可以查看评论。

![2021-11-12 22-29-05 的屏幕截图.png](https://i.loli.net/2021/11/12/3G2bTYM9fyJSAXO.png)

主要代码改动有：

- add `BookRemark` class and `Comment` class. Add `BookRemark` class to `Book` with `Transient` annotation.
- Add `MongoRepository` for `BookRemark`, add origin `Book` and `BookRemark` together.
- Add function of publish and check comments of any Books.

下面是评论信息在数据库中的存储。

![2021-11-12 22-44-04 的屏幕截图.png](https://i.loli.net/2021/11/12/3q7PygamodXEVrD.png)

为什么书籍评价更加适合存在文档数据库中：

- 文档数据库对于评价与评论回复这种一对多关系更加友好
  - 可以像JSON一样扩展，存在一张List内
  - 不必做大量外键关联和JOIN操作
  - 局部性很好，不用手动进行评论的嵌套组合
- 评论的查询都是一次性查询一本书的评论以及其嵌套回复，更像文档
- 方便的实现多层回复嵌套

### Neo4j 与 标签



## Log on Oct.15

### 测试提示

- 您需要运行以下工程进行测试：`./bookstore_microService/{eureka, gateway, searchService}`并修改里面的数据库信息
- 运行主程序，调用`http://localhost:8082/book/searchAuthorByName?bookName=小王子`查看该作作者

### 服务注册发现，微服务，网关，函数式

使用Netflix/Eureka作为服务发现组件，向其管理的Eureka client提供包括服务注册，心跳检测功能。使用Spring cloud gateway注册在Eureka进行网关服务，统一对外提供网关接口，还可以实现鉴权等功能。避免对微服务地址进行硬编码，便于服务的扩充和更改。

在上述技术栈基础上，实现了一个根据书名查找作者列表的微服务，由统一的网关提供服务。首先，将Eureka运行在8761端口，然后配置网关，微服务，主服务的相应端口，在Eureka注册Bookstore书店主业务服务，网关服务和搜索微服务。

![](https://i.loli.net/2021/10/15/q8iUNDvfPdwgraH.png)

随后配置网关，使用注册中心服务名将/book/**前缀的所有请求发给BOOKSTORE微服务。此时，我们访问8082网关端口，已经能使用8080端口的服务了。

![](https://i.loli.net/2021/10/15/9IoqYrKkZcWM4LU.png)

随后在主服务调用其他微服务，使用到了feign，帮助我们方便的利用Eureka的注册中心中的服务名将其他服务类似远程方法调用(RPC)的方式调用，我们只需要提供服务名和接口，就可以把远程的函数注入到本地，从而无感的使用其他服务的函数。

然后我们再把这个服务按照网关格式提供，就可以通过统一的接口从8082网关调用8080服务，在8080服务中调用11130服务。效果如图所示：

![](https://i.loli.net/2021/10/15/RA2IfnlOXsyQdrV.png)

当然这个场景对于经过8080端口意义不大。这时搜索内加了一个场景：能在搜索某本书的时候调用11130服务查询到所有这个作者写的其他书，做到基于作者的联想，同时利用了微服务的优势。

此外稍微了解了一下函数式服务(Serverless). 采用了Flux+Reactor并使用了atomic变量防止在流场景下的并发错误。但是测试的时候还是遇到了一点Bug，暂时还未完成第二个选项的全部内容。代码在`./bookstore_microService/functional`下。

### HTTPS

分为两部分：首先是Backend工程将8081端口的流量强制要求HTTPS协议访问，同时保证8080端口支持HTTP，以便于前端能继续正常访问8080端口。第二部分是把Eureka的协议改为HTTPS，将Gateway注册的协议改为HTTPS。

第一步的代码在` BookstoreBackendApplication`类中，具体就是区分端口，对8081端口`setSecure=true;` 然后我们使用POSTMAN访问时，会出现以下提示：

![](https://i.loli.net/2021/10/20/b16pO7t5TwjJYW2.png)

意思是这个是用户自己生成的证书，没有经过受信任的机构认证过。

我们可以配置自己的client.crt，然后将我们的server.crt证书导入，让我们的自签名证书能在我们自己的电脑上受到信任。具体配置为：

![](https://i.loli.net/2021/10/20/eG9LZjKFQCJwVI7.png)

[ref](https://blog.csdn.net/ONS_cukuyo/article/details/79172242)

签名证书的位置在`./bookstore_security`. 由于识别端口的原因，我们原本的前端还是可以正常工作的。

然后配置eureka，具体在eureka工程的appication.yaml文件中，这时候访问eureka服务器，可以看到我们能用HTTPS协议访问但是不能用HTTP协议访问。其中，用HTTPS协议访问的时候会出现不安全的提示，原因同理。

![](https://i.loli.net/2021/10/20/FosTzwkL6MqRNl4.png)

## Log on Oct.11

### 测试提示

- 在Book页面搜索会得到来自书名和简介的搜索结果，简介采用全文搜索

- 如果需要正确运行我的全文搜索模块，[您可能需要参考这里。](https://juejin.cn/post/6844903873178501133#heading-4)有一些事情需要注意：
  - solr使用的conf在`./bookstore_solr`目录下
  - core name为bookstore，请在SOLR_HOME下面data文件夹下创建好bookstore
  - 请把conf文件夹整个复制到bookstore下，然后在8983端口创建核心
  - 请修改`~/conf/my-data-config.xml`把数据库和密码改称自己的
  
- 如果需要测试WS，您的请求XML为：

  ```xml
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:gs="http://spring.io/guides/gs-producing-web-service">
      <soapenv:Header/>
      <soapenv:Body>
          <gs:getSearchRequest>
              <gs:text>经典</gs:text>
          </gs:getSearchRequest>
      </soapenv:Body>
  </soapenv:Envelope>
  ```

  

### 全文搜索

使用Solr作为工具，采用ik-analyzer中文分词器，支持从数据库直接读取简介和书本信息，自动进行分词缓存，自动进行增量更新缓存，支持定时更新索引。

<img src="https://i.loli.net/2021/10/11/hokfuGZIRp7DtSB.png" alt="2021-10-11 20-16-03 的屏幕截图.png" style="zoom: 50%;" />

集成了搜索框，支持按名字，简介查询。

<img src="https://i.loli.net/2021/10/11/kmoCs6x4iJf1hAp.png" alt="效果" style="zoom: 50%;" />

可以看到，上面是简介的搜索效果。Java部分代码在BookDaoImpl中的getFullTextSearch函数。

### WebService

使用SOAP向外部暴露了全文搜索接口。采用list对列表进行存储。这样有助于其他语言编写的客户端执行某些功能。列表项之间由空格间隔开来。

![](https://i.loli.net/2021/10/11/B3QRlcvYjmwExKP.png)

此外，还在Java中新建程序，接入WSDL接口，自动进行接口解析和Java调用，看见了SOAP的意义。接口工程在./bookstore_solr中。

![](https://i.loli.net/2021/10/14/U7u4J1FXjrOsgCq.png)

## Log on Oct.6

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



