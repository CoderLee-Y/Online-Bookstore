package com.Lee.bookstore_backend.dto;

import com.Lee.bookstore_backend.entity.Book;
import java.math.BigDecimal;

public class BestSellers {
  Book book;
  Integer amount;
  BigDecimal total;

  public BestSellers(Book book, Integer amount, BigDecimal total) {
    this.book = book;
    this.amount = amount;
    this.total = total;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }


}
