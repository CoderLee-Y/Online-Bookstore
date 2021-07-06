package com.example.bookstore_backend.dao;

import com.example.bookstore_backend.entity.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookDao {
  Book findOne(Long id);

  Page<Book> getBooks(PageRequest pageRequest);

  List<Book> getAllBooks();

  Page<Book> searchBooks(PageRequest pageRequest, String bookName);

  void deleteBookById(Long id);

  void saveOriginBook(Map<String,String> paras);

  Book addBook();

  void reduceInventory(List<Long> book_id, List<Integer> amount);

  boolean checkInventory(List<Long> book_id, List<Integer> amount);
}
