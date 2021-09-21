package com.Lee.bookstore_backend.serviceimpl;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.dao.OrderDao;
import com.Lee.bookstore_backend.dao.UserDao;
import com.Lee.bookstore_backend.dto.BestSellers;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.service.BookService;
import com.Lee.bookstore_backend.utils.fileProcessor.FileProcessor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookServiceImpl implements BookService {
  private BookDao bookDao;
  private OrderDao orderDao;
  private UserDao userDao;

  @Autowired
  public void setBookDao(BookDao bookDao){
    this.bookDao = bookDao;
  }

  @Autowired
  public void setOrderDao(OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  @Autowired
  public void setUserDao(UserDao userDao){
    this.userDao = userDao;
  }

  @Override
  public Book findBookById(Long id){
    return bookDao.findOne(id);
  }

  @Override
  public Page<Book> getBooks(Integer page, Integer sortId){
    //    从大到小 ASC 从小到大 DESC
    PageRequest pageRequest;
    switch (sortId){
//      sortId:
//      1: isbn 升序 - 也是默认序
//      2: isbn 降序
//      3. inventory 升序
//      4. inventory 降序
//      5. price 升序
//      6. Price 降序
//      0： 默认序
      case 1:
        pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Direction.ASC, "isbn"));
        break;
      case 2:
        pageRequest = PageRequest.of(page, 12, Sort.by(Direction.DESC, "isbn"));
        break;
      case 3:
        pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Direction.ASC, "inventory"));
        break;
      case 4:
        pageRequest = PageRequest.of(page, 12, Sort.by(Direction.DESC, "inventory"));
        break;
      case 5:
        pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Direction.ASC, "price"));
        break;
      case 6:
        pageRequest = PageRequest.of(page, 12, Sort.by(Direction.DESC, "price"));
        break;
      default:
        pageRequest = PageRequest.of(page, 12, Sort.by(Direction.ASC, "bookId"));
    }

    return bookDao.getBooks(pageRequest);
  }

  @Override
  public void deleteBookById(Long id) {
    bookDao.deleteBookById(id);
  }

  @Override
  public void saveOriginBook(Map<String,String> paras) {
    bookDao.saveOriginBook(paras);
  }

  @Override
  public Book addBook() {
    return bookDao.addBook();
  }

  @Override
  public List<BestSellers> getBestSellers(Timestamp start, Timestamp end, Integer sortId) {
    List<Book> books = bookDao.getAllBooks();
    List<BestSellers> ret = new ArrayList<>();

    for(Book book : books)
    {
      ret.add(new BestSellers(book, 0, new BigDecimal(0)));
    }

    List<OrderTable> orderTables = orderDao.getAllOrders(start, end);
    for(OrderTable orderTable: orderTables){
      List<OrderRecord> orderRecords = orderTable.getItems();
      for(OrderRecord orderRecord: orderRecords){
        for(BestSellers bestSellers: ret){
          if(bestSellers.getBook().equals(orderRecord.getBook())){
            bestSellers.setAmount(bestSellers.getAmount() + orderRecord.getAmount());
            bestSellers.setTotal(bestSellers.getTotal().add(new BigDecimal
                (orderRecord.getAmount()).multiply(orderRecord.getPrice())));
            break;
          }
        }
      }
    }
    if(sortId == 0) {
      ret.sort(Comparator.comparing(BestSellers::getAmount).reversed());
    }
    else{
      ret.sort(Comparator.comparing(BestSellers::getTotal).reversed());
    }
    return ret;
  }

  @Override
  public List<BestSellers> getFavourite(Integer userId, Timestamp start, Timestamp end) {
    List<BestSellers> ret = new ArrayList<>();
    List<OrderTable> orderTables = orderDao.getAllByUser(userDao.getUserById(userId));

    for(OrderTable orderTable:orderTables){
      if(orderTable.getOrderTime().before(start)||orderTable.getOrderTime().after(end))
        continue;
      List<OrderRecord> orderRecords = orderTable.getItems();
      for(OrderRecord orderRecord: orderRecords){
        boolean hasCreated = false;
        Book book = orderRecord.getBook();
        for(BestSellers bestSellers: ret){
          if(bestSellers.getBook().equals(book)){
            bestSellers.setAmount(bestSellers.getAmount()+orderRecord.getAmount());
            bestSellers.setTotal(bestSellers.
                getTotal().add(orderRecord.getPrice().
                multiply(new BigDecimal(orderRecord.getAmount()))));
                hasCreated = true;
                break;
          }
        }
        if(!hasCreated){
          ret.add(new BestSellers(book, orderRecord.getAmount(),
              orderRecord.getPrice().multiply(new BigDecimal(orderRecord.getAmount()))));
        }
      }
    }

    ret.sort(Comparator.comparing(BestSellers::getAmount).reversed());

    return ret;
  }

  @Override
  public Page<Book> searchBooks(Integer page, String bookName) {
    PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Direction.ASC, "bookId"));

    return bookDao.searchBooks(pageRequest, ("%" + bookName + "%"));
  }

  @Override
  public void uploadPicture(MultipartFile multipartFile) {
    String filename = multipartFile.getOriginalFilename();
    System.out.println("Filename: " + filename);
    FileProcessor.createFileProcessor()
        .addFilters(new String[]{"pdf", "jpg", "png", "txt", "csv", "doc", "docx"})
        .addUploadFile(multipartFile)
        .saveUploadFileTo("src/main/resources/static/bookImg");
  }
}
