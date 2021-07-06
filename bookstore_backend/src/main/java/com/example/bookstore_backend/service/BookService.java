package com.example.bookstore_backend.service;

import com.example.bookstore_backend.dto.BestSellers;
import com.example.bookstore_backend.entity.Book;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookService {

  Book findBookById(Long id);

  Page<Book> getBooks(Integer page, Integer sortId);

  void deleteBookById(Long id);

  void saveOriginBook(Map<String,String> paras);

  Book addBook();

  List<BestSellers> getBestSellers(Timestamp start,
      Timestamp end, Integer sortId);

  List<BestSellers> getFavourite(Integer userId, Timestamp start, Timestamp end);

  Page<Book> searchBooks(Integer page, String bookName);
}
