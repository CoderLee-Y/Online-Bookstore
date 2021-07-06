package com.example.bookstore_backend.dao;

import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.entity.CartItem;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CartDao {

  List<CartItem> getCartItemsByUserId(Integer user_id);

  public void deleteCartItems(Integer user_id, Long book_id);

  public void addCartItems(Integer user_id, Long book_id);

  public Integer createOrder(Integer user_id, List<Long> book_id,
      List<Integer> amount, List<BigDecimal> price);

}
