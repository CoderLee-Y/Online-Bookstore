package com.Lee.bookstore_backend.controller;

import com.Lee.bookstore_backend.dto.BestSellers;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.Label;
import com.Lee.bookstore_backend.microService.feign.FeignClientForSearch;
import com.Lee.bookstore_backend.multithreading.AddVisitors;
import com.Lee.bookstore_backend.service.BookService;
import com.Lee.bookstore_backend.utils.messageUtils.MessageUtil;
import com.Lee.bookstore_backend.utils.messageUtils.returnMessage;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BookController {

  private BookService bookService;

  private FeignClientForSearch feignClientForSearch;

  private static final ExecutorService exec = Executors.newFixedThreadPool(20);

  @Autowired
  public void setBookService(BookService bookService){
    this.bookService = bookService;
  }

  @Autowired
  public void setFeignClientForSearch(FeignClientForSearch feignClientForSearch){
    this.feignClientForSearch = feignClientForSearch;
  }

  @GetMapping("/book/searchAuthorByName")
  List<String> getAuthorByName(@RequestParam("bookName") String bookName){
    return feignClientForSearch.getAuthorByName(bookName);
  }

  @RequestMapping("/getHomePage")
  public returnMessage getHomePage() throws ExecutionException, InterruptedException {
    JSONObject data = new JSONObject();
    FutureTask<Integer> futureTask = new FutureTask<Integer>(new AddVisitors());
    exec.submit(futureTask);
    data.put("visitors", futureTask.get());
    return MessageUtil.createMessage(0, "success", data);
  }


  @RequestMapping("/book/getBooksForTest")
  public Page<Book> getBooksForTest() {
    return bookService.getBooks(0, 0);
  }

  @RequestMapping("/getBooks")
  public List<Book> getBooks(@RequestBody Map<String, Integer> paras) {
    Integer page = paras.get("page");
    Integer sortId = paras.get("sortId");

    return bookService.getBooks(page, sortId).getContent();
  }

  @RequestMapping("/searchBooks")
  public List<Book> searchBookByName(@RequestBody Map<String, String> paras)
      throws SolrServerException, IOException {
    Integer page = Integer.valueOf(paras.get("page"));
    String bookName = paras.get("bookName");
    return bookService.searchBooks(page, bookName);
  }

  @RequestMapping("/getBookById")
  public Book getBook(@RequestBody Map<String, Long> paras) {
    Long id = paras.get("id");
    return bookService.findBookById(id);
  }

  @RequestMapping("/addComment")
  public Integer addComment(@RequestBody JSONObject paras) {
    Long id = paras.getLong("bookId");
    String text = paras.getString("comment");
    JSONObject author = (SessionUtil.getAuthority());
    if(author == null)
      return 0;

    Integer authorId = author.getIntValue("userId");
    bookService.addComment(id, authorId, text);
    return 1;
  }

  @RequestMapping("/findAllByLabel")
  public JSONObject findAllByLabel(String label) {
    return bookService.findByLabelName(label);
  }

  @RequestMapping("/deleteBookById")
  public void deleteBookById(@RequestBody Map<String, Long> paras) {
    Long id = paras.get("bookId");
    bookService.deleteBookById(id);
  }

  @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
  public void setBook(@RequestBody Map<String, String> paras) {
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
    Integer userId = Objects.requireNonNull(SessionUtil.getAuthority()).getIntValue("userId");

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
