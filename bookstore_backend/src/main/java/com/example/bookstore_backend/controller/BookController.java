package com.example.bookstore_backend.controller;

import com.example.bookstore_backend.dto.BestSellers;
import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.service.BookService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BookController {
  private BookService bookService;

  @Autowired
  public void setBookService(BookService bookService){
    this.bookService = bookService;
  }

  @RequestMapping("/getBooksForTest")
  public Page<Book> getBooksForTest() {
    return bookService.getBooks(0, 0);
  }

  @RequestMapping("/getBooks")
  public Page<Book> getBooks(@RequestBody Map<String, Integer> paras) {
    Integer page = paras.get("page");
    Integer sortId = paras.get("sortId");

    return bookService.getBooks(page, sortId);
  }

  @RequestMapping("/searchBooks")
  public Page<Book> searchBookByName(@RequestBody Map<String, String> paras) {
    Integer page = Integer.valueOf(paras.get("page"));
    String bookName = paras.get("bookName");
    return bookService.searchBooks(page, bookName);
  }

  @RequestMapping("/getBookById")
  public Book getBook(@RequestBody Map<String, Long> paras) {
    Long id = paras.get("id");
    System.out.println(id);
    return bookService.findBookById(id);
  }

  @RequestMapping("/deleteBookById")
  public void deleteBookById(@RequestBody Map<String, Long> paras) {
    Long id = paras.get("bookId");
    System.out.println(id);
    bookService.deleteBookById(id);
  }

  @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
  public void setBook(@RequestBody Map<String, String> paras) {
    System.out.println(paras.toString());
    bookService.saveOriginBook(paras);
  }

  @RequestMapping(value = "/addBook")
  public Book addBook() {
    return bookService.addBook();
  }

  @RequestMapping(value = "getBestSellers")
  public List<BestSellers> getBestSellers(@RequestBody Map<String, String> paras) {

    String startStr = paras.get("start"), endStr = paras.get("end");
    Integer sortId = Integer.valueOf(paras.get("sortId"));
    Timestamp start = startStr.equals("null")
        ? (new Timestamp(1)) : Timestamp.valueOf(startStr);
    Timestamp end = endStr.equals("null") ?
        (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);
    return bookService.getBestSellers(start, end, sortId);
  }

  @RequestMapping(value = "getFavourite")
  public List<BestSellers> getFavourite(@RequestBody Map<String, String> paras) {
    String startStr = paras.get("start"), endStr = paras.get("end");
    Integer userId = Integer.valueOf(paras.get("userId"));

    Timestamp start = startStr.equals("null")
        ? (new Timestamp(1)) : Timestamp.valueOf(startStr);
    Timestamp end = endStr.equals("null") ?
        (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);
    return bookService.getFavourite(userId, start, end);
  }

  @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
  public void uploadPicture(@RequestParam(value = "photoContent") MultipartFile multipartFile) {
    bookService.uploadPicture(multipartFile);
  }
}
