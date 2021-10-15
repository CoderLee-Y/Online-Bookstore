package org.lee.searchMicroService.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "book")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Book {
  private long bookId;
  private String isbn;
  private String name;
  private String type;
  private String author;
  private BigDecimal price;
  private String description;
  private Integer inventory;
  private String image;
  private String cover;

  public Book() {
  }

  public Book(long bookId, String isbn, String name, String type, String author,
      BigDecimal price, String description, Integer inventory, String image, String cover) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.name = name;
    this.type = type;
    this.author = author;
    this.price = price;
    this.description = description;
    this.inventory = inventory;
    this.image = image;
    this.cover = cover;
  }

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "id")
  public long getBookId() {
    return bookId;
  }

  public void setBookId(long bookId) {
    this.bookId = bookId;
  }

  @Column(name = "isbn")
  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Column(name = "author")
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Column(name = "price")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "inventory")
  public Integer getInventory() {
    return inventory;
  }

  public void setInventory(Integer inventory) {
    this.inventory = inventory;
  }

  @Column(name = "image")
  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Column(name = "cover")
  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return bookId == book.bookId && Objects.equals(isbn, book.isbn) && Objects
        .equals(name, book.name) && Objects.equals(type, book.type) && Objects
        .equals(author, book.author) && Objects.equals(price, book.price)
        && Objects.equals(description, book.description) && Objects
        .equals(inventory, book.inventory) && Objects.equals(image, book.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookId, isbn, name, type, author, price, description, inventory, image);
  }

  @Override
  public String toString() {
    return "Book{" +
        "bookId=" + bookId +
        ", isbn='" + isbn + '\'' +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", author='" + author + '\'' +
        ", price=" + price +
        ", description='" + description + '\'' +
        ", inventory=" + inventory +
        ", image='" + image + '\'' +
        ", cover='" + cover + '\'' +
        '}';
  }
}

