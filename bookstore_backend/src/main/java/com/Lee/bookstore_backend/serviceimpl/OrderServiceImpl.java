package com.Lee.bookstore_backend.serviceimpl;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.dao.CartDao;
import com.Lee.bookstore_backend.dao.OrderDao;
import com.Lee.bookstore_backend.dao.UserDao;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.service.OrderService;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;


@Service
public class OrderServiceImpl implements OrderService {

  OrderDao orderDao;
  UserDao userDao;
  CartDao cartDao;
  BookDao bookDao;


  @Autowired
  public OrderServiceImpl(OrderDao orderDao, UserDao userDao, CartDao cartDao,
      BookDao bookDao) {
    this.orderDao = orderDao;
    this.userDao = userDao;
    this.cartDao = cartDao;
    this.bookDao = bookDao;
  }

  @Override
  public void finishOrder(Integer order_id) {
    orderDao.finishOrder(order_id);
  }

  @Override
  public List<OrderTable> getOrder(Integer user_id) {
    return orderDao.getOrder(user_id);
  }

  @Override
  public List<OrderRecord> getOrderItems(Integer order_id) {
    OrderTable orderTable = orderDao.getOrderTable(order_id);
    return orderDao.getOrderItems(orderTable);
  }

  @Override
  public List<OrderTable> getAllOrders(Timestamp start, Timestamp end) {
    return orderDao.getAllOrders(start, end);
  }

  @Override
  public List<OrderTable> getAllOrdersByUserId(Integer userId, Timestamp start, Timestamp end) {
    User user = userDao.getUserById(userId);
    return orderDao.getAllOrdersByUserId(user, start, end);
  }

  //  Just tool: handle kafka queue
//  first REQUIRED in a transactional and then lock the book
  @KafkaListener(id = "orderHandler", topics = "order", groupId = "orderHandler")
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void handleOrder(@Payload String str) {
    System.out.println(str);
    JSONObject data = JSONObject.parseObject(str);
    Integer user_id = data.getInteger("userId");
    List<Long> book_id = data.getJSONArray("bookId").toJavaList(Long.TYPE);
    List<Integer> amount = data.getJSONArray("amount").toJavaList(Integer.TYPE);
    List<BigDecimal> price = data.getJSONArray("price").toJavaList(BigDecimal.class);

    List<Double> booksAmount = new ArrayList<>();
    List<Double> booksPrice = new ArrayList<>();
    for (int i = 0; i < price.size(); ++i) {
      booksAmount.add(Double.valueOf(amount.get(i)));
      booksPrice.add(price.get(i).doubleValue());
    }

    RestTemplate restTemplate = new RestTemplate();

//    ??????????????????????????????????????????
//    ?????????????????????????????????????????????
    if (bookDao.reduceInventory(book_id, amount) < 0) {
      return;
    }

    cartDao.createOrder(user_id, book_id, amount, price);
  }
}
