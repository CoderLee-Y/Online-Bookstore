package com.Lee.bookstore_backend.service;

import com.Lee.bookstore_backend.dto.BestSellers;
import com.Lee.bookstore_backend.entity.Book;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {

  Book findBookById(Long id);

  Page<Book> getBooks(Integer page, Integer sortId);

  void deleteBookById(Long id);

  void saveOriginBook(Map<String,String> paras);

  Book addBook();

  List<BestSellers> getBestSellers(Timestamp start,
      Timestamp end, Integer sortId);

  List<BestSellers> getFavourite(Integer userId, Timestamp start, Timestamp end);

  List<Book> searchBooks(Integer page, String bookName) throws SolrServerException, IOException;

  void uploadPicture(MultipartFile multipartFile);
}
