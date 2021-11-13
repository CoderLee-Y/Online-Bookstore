package com.Lee.bookstore_backend.dao;

import com.Lee.bookstore_backend.entity.Book;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookDao {
  Book findOne(Long id);

  Page<Book> getBooks(PageRequest pageRequest);

  List<Book> getAllBooks();

  List<Book> searchBooks(PageRequest pageRequest, String bookName)
      throws SolrServerException, IOException;

  List<Book> getFullTextSearch(String text) throws SolrServerException, IOException;

  void deleteBookById(Long id);

  void saveOriginBook(Map<String,String> paras);

  Book addBook();

  Integer reduceInventory(List<Long> book_id, List<Integer> amount);

  boolean checkInventory(List<Long> book_id, List<Integer> amount);

  void addComment(Long id, Integer authorId, String text);

  JSONObject findByLabelName(String label);
}
