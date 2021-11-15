package com.Lee.bookstore_backend.daoimpl;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.BookRemark;
import com.Lee.bookstore_backend.entity.Comment;
import com.Lee.bookstore_backend.entity.Label;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.repository.BookLabelRepository;
import com.Lee.bookstore_backend.repository.BookRemarkRepository;
import com.Lee.bookstore_backend.repository.BookRepository;
import com.Lee.bookstore_backend.utils.redisUtil.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookDaoImpl implements BookDao {

  private BookRepository bookRepository;
  private BookRemarkRepository bookRemarkRepository;
  private BookLabelRepository bookLabelRepository;
  private RedisUtil redisUtil;

  Logger logger = LoggerFactory.getLogger(BookDao.class);

  @Autowired
  public void setBookRepository(BookRepository bookRepository, RedisUtil redisUtil,
      BookRemarkRepository bookRemarkRepository) {
    this.bookRepository = bookRepository;
    this.bookRemarkRepository = bookRemarkRepository;
    this.redisUtil = redisUtil;
  }

  @Autowired
  public void setBookLabelRepository( BookLabelRepository bookLabelRepository){
    this.bookLabelRepository = bookLabelRepository;
  }

  @Override
  public JSONObject findByLabelName(String label) {
    JSONObject jsonObject = new JSONObject();

    List<Book> books = bookRepository.findAllByType(label);
    jsonObject.put("origin", books);
    List<Label> labels = bookLabelRepository.findLabelsByName(label);
    bookLabelRepository.save(labels.get(0));
    List<Book> alsoLikeBooks = new ArrayList<>();
    for(Label alsoLikeLabel: labels){
      alsoLikeBooks.addAll(bookRepository.findAllByType(alsoLikeLabel.getName()));
    }

    jsonObject.put("alsoLike", alsoLikeBooks);
    return jsonObject;
  }

  @Override
  public Book findOne(Long id) {
    if (redisUtil.hHasKey(RedisUtil.BookHashTable, id.toString())) {
      Book book = (Book) redisUtil.hget(RedisUtil.BookHashTable, id.toString());
      book.setBookRemark(bookRemarkRepository.findDistinctByBookId(id));
      return book;
    } else {
      Book book = bookRepository.findById(id).orElse(null);
      if (book == null) {
        return new Book();
      }
      if (!redisUtil.hset(RedisUtil.BookHashTable, id.toString(), book, 60)) {
        logger.error("fail to set hash table of book in BookDao");
      }
      book.setBookRemark(bookRemarkRepository.findDistinctByBookId(id));
      return book;
    }
  }

  @Override
  public Page<Book> getBooks(PageRequest pageRequest) {
    //    if inventory < 0, means abandoned by manager
    return bookRepository.findAll(pageRequest);
  }

  private void invalidBookCache(Long id) {
    redisUtil.hdel(RedisUtil.BookHashTable, id.toString());
  }

  @Override
  public void deleteBookById(Long id) {
    Book book = bookRepository.findById(id).orElse(null);
    if (book == null) {
      return;
    }
    book.setInventory(-1);
    bookRepository.saveAndFlush(book);
    invalidBookCache(id);
  }

  @Override
  public void saveOriginBook(Map<String, String> paras) {
    Long bookId = Long.valueOf(paras.get("bookId"));

    Book book = bookRepository.findById(bookId).orElse(new Book());

    book.setInventory(Integer.valueOf(paras.get("inventory")));
    book.setPrice(BigDecimal.valueOf(Double.parseDouble(paras.get("price"))));
    book.setAuthor(paras.get("author"));
    book.setImage(paras.get("image"));
    book.setDescription(paras.get("description"));
    book.setIsbn(paras.get("isbn"));
    book.setName(paras.get("name"));
    book.setType(paras.get("type"));
    bookRepository.saveAndFlush(book);

    invalidBookCache(bookId);
  }

  @Override
  public Book addBook() {
    Book book = new Book();
    book.setAuthor("default");
    book.setType("default");
    book.setName("default");
    book.setIsbn("default");
    book.setDescription("default");
    book.setImage("default");
    book.setInventory(0);
    book.setPrice(BigDecimal.valueOf(0));
    book = bookRepository.saveAndFlush(book);

    if (!redisUtil.hset(RedisUtil.BookHashTable, Long.toString(book.getBookId()), book, 60)) {
      logger.error("fail to set hash table of book in BookDao");
    }
    return book;
  }

  @Override
  public List<Book> searchBooks(PageRequest pageRequest, String bookName)
      throws SolrServerException, IOException {
    Page<Book> ret = bookRepository.findByNameLike(("%" + bookName + "%"), pageRequest);
    List<Book> fullResult = getFullTextSearch(bookName);
    List<Book> searchResult = ret.getContent();
    for(Book book: searchResult){
      boolean dup = false;
      for(Book has: fullResult){
        if(has.getBookId() == book.getBookId()){
          dup = true;
          break;
        }
      }
      if(!dup){
        fullResult.add(book);
      }
    }
    return fullResult;
  }

  @Override
  public List<Book> getFullTextSearch(String text) throws SolrServerException, IOException {
    String SOLR_URL = "http://localhost:8983/solr";
    String CORE_NAME = "bookstore";

    List<Book> ret = new ArrayList<>();
    HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL)
        .withConnectionTimeout(10000).withSocketTimeout(60000).build();

    SolrQuery query = new SolrQuery();
    query.setFields("id", "description");
    query.set("q", ("description:" + text));
    QueryResponse response = client.query(CORE_NAME, query);
    SolrDocumentList results = response.getResults();

    for (SolrDocument doc : results) {
      bookRepository.findById(Long.valueOf(doc.get("id").toString())).ifPresent(ret::add);
    }
    return ret;
  }

  @Override
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Integer reduceInventory(List<Long> book_id, List<Integer> amount) {
    for (int i = 0; i < book_id.toArray().length; ++i) {
      Book book = bookRepository.findById(book_id.get(i)).orElse(null);
      if (book == null) {
        return -1;
      }

      Integer book_num = book.getInventory();
      if (book_num <= 0) {
        return -1;
      }

      book.setInventory(book_num - amount.get(i));
      bookRepository.saveAndFlush(book);
    }

    return 0;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
  public boolean checkInventory(List<Long> book_id, List<Integer> amount) {
    for (int i = 0; i < book_id.toArray().length; ++i) {
      Book book = bookRepository.findById(book_id.get(i)).orElse(null);
      if (book == null) {
        return false;
      }
      if (book.getInventory() < amount.get(i)) {
        return false;
      }
    }
    //    true means ok
    return true;
  }

  @Override
  public void addComment(Long id, Integer authorId, String text) {
    BookRemark all = bookRemarkRepository.findDistinctByBookId(id);
    if(all == null){
      all = new BookRemark();
      all.setBookId(id);

       List<Comment> tmp = new ArrayList<>();
       all.setComment(tmp);
    }

    List<Comment> comments = all.getComment();
    Comment new_comment = new Comment();
    List<Comment> replies = new ArrayList<>();

    new_comment.setReply(replies);
    new_comment.setText(text);
    new_comment.setAuthorId(authorId);

    comments.add(new_comment);
    all.setComment(comments);
    bookRemarkRepository.save(all);
  }
}
