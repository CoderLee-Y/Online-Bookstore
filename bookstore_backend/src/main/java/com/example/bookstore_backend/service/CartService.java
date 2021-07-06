package com.example.bookstore_backend.service;

import com.example.bookstore_backend.entity.Book;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
  List<Book> getCartItems(Integer user_id);

  void deleteCartItems(Integer user_id,Long book_id);

  void addCartItems(Integer user_id,Long book_id);

  void createOrder( Integer user_id,List<Long> book_id,
      List<Integer> amount, List<BigDecimal> price);

  boolean checkInventory(List<Long> book_id,
      List<Integer> amount);
}
