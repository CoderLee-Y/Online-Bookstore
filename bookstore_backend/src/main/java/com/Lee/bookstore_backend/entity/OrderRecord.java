package com.Lee.bookstore_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "order_record")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
public class OrderRecord {

  private Integer recordId;
  private Integer amount;
  private BigDecimal price;
  private Book book;

  public OrderRecord() {
  }

  public OrderRecord(Integer recordId, Integer amount, BigDecimal price,
      Book book, OrderTable orderTable) {
    this.recordId = recordId;
    this.amount = amount;
    this.price = price;
    this.book = book;
  }

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public Integer getRecordId() {
    return recordId;
  }

  public void setRecordId(Integer recordId) {
    this.recordId = recordId;
  }

  @Column(name = "amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  @Column(name = "price")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  //  book是受控方，主控方写join column
  @ManyToOne(targetEntity = Book.class, cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  @JoinColumn(name = "book_id")
  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }
}
