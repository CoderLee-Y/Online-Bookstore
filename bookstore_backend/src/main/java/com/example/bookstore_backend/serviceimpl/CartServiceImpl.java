package com.example.bookstore_backend.serviceimpl;

import com.example.bookstore_backend.dao.BookDao;
import com.example.bookstore_backend.dao.CartDao;
import com.example.bookstore_backend.dao.UserDao;
import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.entity.CartItem;
import com.example.bookstore_backend.entity.User;
import com.example.bookstore_backend.service.CartService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {

  CartDao cartDao;
  UserDao userDao;
  BookDao bookDao;

  @Autowired
  public CartServiceImpl(CartDao cartDao,UserDao userDao, BookDao bookDao)
  {
    this.cartDao = cartDao;
    this.userDao = userDao;
    this.bookDao = bookDao;
  }


  @Override
  public void deleteCartItems(Integer user_id, Long book_id) {
    cartDao.deleteCartItems(user_id, book_id);
  }

  @Override
  public void createOrder(Integer user_id, List<Long> book_id,
      List<Integer> amount, List<BigDecimal> price) {
    cartDao.createOrder(user_id, book_id, amount, price);
    bookDao.reduceInventory(book_id, amount);
  }

  @Override
  public boolean checkInventory(List<Long> book_id, List<Integer> amount) {
    return bookDao.checkInventory(book_id, amount);
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
