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



