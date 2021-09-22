package com.Lee.bookstore_backend.controller;

import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.service.CartService;
import com.Lee.bookstore_backend.utils.messageUtils.Message;
import com.Lee.bookstore_backend.utils.messageUtils.MessageUtil;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
public class CartController {

  final CartService cartService;


  @Autowired
  CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @RequestMapping("/getCartItems")
  public List<Book> getCartItems() {
    Integer user_id = Objects.requireNonNull(SessionUtil.getAuthority()).getInt("userId");
    return cartService.getCartItems(user_id);
  }

  @RequestMapping("/deleteCartItems")
  public void deleteCartItems(@RequestParam("book_id") Long book_id) {
    Integer user_id = Objects.requireNonNull(SessionUtil.getAuthority()).getInt("userId");
    cartService.deleteCartItems(user_id, book_id);
  }

  @RequestMapping("/addCartItems")
  public void addCartItems(@RequestParam("book_id") Long book_id) {
    Integer user_id = Objects.requireNonNull(SessionUtil.getAuthority()).getInt("userId");
    cartService.addCartItems(user_id, book_id);
  }

  @RequestMapping("/checkInventory")
  public boolean checkInventory(@RequestParam("book_id") List<Long> book_id,
      @RequestParam("amount") List<Integer> amount) {
    return cartService.checkInventory(book_id, amount);
  }


}