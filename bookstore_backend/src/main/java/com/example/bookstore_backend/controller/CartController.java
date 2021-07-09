package com.example.bookstore_backend.controller;

import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.service.CartService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CartController {

  final CartService cartService;

  @Autowired
  CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @RequestMapping("/getCartItems")
  public List<Book> getCartItems(@RequestParam("user_id") Integer user_id) {
    System.out.println("get cart item receive: controller");
    return cartService.getCartItems(user_id);
  }

  @RequestMapping("/deleteCartItems")
  public void deleteCartItems(@RequestParam("user_id") Integer user_id,
      @RequestParam("book_id") Long book_id) {
    System.out.println("delete cart item receive: controller");
    cartService.deleteCartItems(user_id, book_id);
  }

  @RequestMapping("/addCartItems")
  public void addCartItems(@RequestParam("user_id") Integer user_id,
      @RequestParam("book_id") Long book_id) {
    System.out.println("add cart item receive: controller");
    cartService.addCartItems(user_id, book_id);
  }

  @RequestMapping("/createOrder")
  public void createOrder(@RequestParam("user_id") Integer user_id,
      @RequestParam("book_id") List<Long> book_id,
      @RequestParam("amount") List<Integer> amount,
      @RequestParam("price") List<BigDecimal> price) {
    cartService.createOrder(user_id, book_id, amount,price);
  }

  @RequestMapping("/checkInventory")
  public boolean checkInventory(@RequestParam("book_id") List<Long> book_id,
      @RequestParam("amount") List<Integer> amount) {
    return cartService.checkInventory(book_id, amount);
  }


}
