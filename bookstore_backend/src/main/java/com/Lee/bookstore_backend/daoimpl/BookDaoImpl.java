package com.Lee.bookstore_backend.daoimpl;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl implements BookDao {

  private BookRepository bookRepository;

  @Autowired
  public void setBookRepository(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Book findOne(Long id){
    return bookRepository.getOne(id);
  }

  @Override
  public Page<Book> getBooks(PageRequest pageRequest) {
    //    if inventory < 0, means abandoned by manager
//    ret = ret.stream().filter(s -> (s.getInventory()>=0)).collect(Collectors.toList());
    return bookRepository.findAll(pageRequest);
  }

  @Override
  public void deleteBookById(Long id) {
    Book book = bookRepository.getOne(id);
    book.setInventory(-1);
    bookRepository.saveAndFlush(book);
  }

  @Override
  public void saveOriginBook(Map<String,String> paras) {
    Long bookId = Long.valueOf(paras.get("bookId"));

    Book book = bookRepository.getOne(bookId);
    book.setInventory(Integer.valueOf(paras.get("inventory")));
    book.setPrice(BigDecimal.valueOf(Double.parseDouble(paras.get("price"))));
    book.setAuthor(paras.get("author"));
    book.setImage(paras.get("image"));
    book.setDescription(paras.get("description"));
    book.setIsbn(paras.get("isbn"));
    book.setName(paras.get("name"));
    book.setType(paras.get("type"));
    bookRepository.saveAndFlush(book);
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
    return book;
  }

  @Override
  public Page<Book> searchBooks(PageRequest pageRequest, String bookName) {
    return bookRepository.findByNameLike(bookName, pageRequest);
  }

  @Override
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  @Override
  public void reduceInventory(List<Long> book_id, List<Integer> amount) {

    for(int i = 0; i < book_id.toArray().length; ++i)
    {
      Book book = bookRepository.findById(book_id.get(i)).orElse(null);
      if(book == null)
        return;
      book.setInventory(book.getInventory() - amount.get(i));
      bookRepository.saveAndFlush(book);
    }
  }

  @Override
  public boolean checkInventory(List<Long> book_id, List<Integer> amount) {
    for(int i = 0; i < book_id.toArray().length; ++i)
    {
      Book book = bookRepository.getOne(book_id.get(i));
      if(book.getInventory() < amount.get(i))
        return false;
    }
//    true means ok
    return true;
  }
}
