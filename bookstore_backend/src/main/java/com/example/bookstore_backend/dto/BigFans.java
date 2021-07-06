package com.example.bookstore_backend.dto;
import com.example.bookstore_backend.entity.User;
import java.math.BigDecimal;

public class BigFans {
  User user;
  Integer amount;
  BigDecimal value;

  public BigFans(User user, Integer amount, BigDecimal value) {
    this.user = user;
    this.amount = amount;
    this.value = value;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }
}
