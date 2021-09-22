package com.Lee.bookstore_backend.serviceimpl;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.dao.CartDao;
import com.Lee.bookstore_backend.dao.UserDao;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.CartItem;
import com.Lee.bookstore_backend.service.CartService;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


@Service
public class CartServiceImpl implements CartService {

  CartDao cartDao;
  UserDao userDao;
  BookDao bookDao;
  KafkaTemplate<String, String> sender;

  @Autowired
  public CartServiceImpl(CartDao cartDao, UserDao userDao, BookDao bookDao,
      KafkaTemplate<String, String> kafkaTemplate) {
    this.cartDao = cartDao;
    this.userDao = userDao;
    this.bookDao = bookDao;
    this.sender = kafkaTemplate;
  }

  @Override
  public void deleteCartItems(Integer user_id, Long book_id) {
    cartDao.deleteCartItems(user_id, book_id);
  }

  @Override
  public void createOrder(Integer user_id, List<Long> book_id,
      List<Integer> amount, List<BigDecimal> price) {

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("userId", user_id);
    jsonObject.put("bookId", book_id);
    jsonObject.put("amount", amount);
    jsonObject.put("price", price);

    ListenableFuture<SendResult<String, String>> future = sender.send("order", "1", jsonObject.toJSONString());
    future.addCallback(new KafkaSendCallback<String, String>() {
      @Override
      public void onSuccess(SendResult<String, String> result) {
//      TODO: What to do when On success?
      }

      @Override
      public void onFailure(KafkaProducerException ex) {
        System.out.println(ex.getFailedProducerRecord());
      }
    });

  }

  @Override
  public boolean checkInventory(List<Long> book_id, List<Integer> amount) {
    return bookDao.checkInventory(book_id, amount);
  }

  //  Just tool: handle kafka queue
  @KafkaListener(id = "orderHandler", topics = "order", groupId = "orderHandler")
  public void handleOrder(@Payload String str) {
    JSONObject data = JSONObject.parseObject(str);
    Integer user_id = data.getInteger("userId");
    List<Long> book_id = data.getJSONArray("bookId").toJavaList(Long.TYPE);
    List<Integer> amount = data.getJSONArray("amount").toJavaList(Integer.TYPE);
    List<BigDecimal> price = data.getJSONArray("price").toJavaList(BigDecimal.class);

    cartDao.createOrder(user_id, book_id, amount, price);
    bookDao.reduceInventory(book_id, amount);
  }

  @Override
  public List<Book> getCartItems(Integer user_id) {
    List<CartItem> cartItems = cartDao.getCartItemsByUserId(user_id);
    List<Book> books = new ArrayList<>();
    cartItems.forEach(s -> books.add(s.getBook()));
    return books;
  }

  @Override
  public void addCartItems(Integer user_id, Long book_id) {
    List<CartItem> cartItems = cartDao.getCartItemsByUserId(user_id);
    cartDao.addCartItems(user_id, book_id);
  }
}
