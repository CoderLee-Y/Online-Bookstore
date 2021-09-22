package com.Lee.bookstore_backend.daoimpl;

import com.Lee.bookstore_backend.dao.CartDao;
import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.CartItem;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.repository.BookRepository;
import com.Lee.bookstore_backend.repository.CartRepository;
import com.Lee.bookstore_backend.repository.OrderRecordRepository;
import com.Lee.bookstore_backend.repository.OrderRepository;
import com.Lee.bookstore_backend.repository.UserRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartDaoImpl implements CartDao {

  private CartRepository cartRepository;
  private UserRepository userRepository;
  private BookRepository bookRepository;
  private OrderRepository orderRepository;
  private OrderRecordRepository orderRecordRepository;

  @Autowired
  public void setCartRepository(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Autowired
  public void setOrderRecordRepository(OrderRecordRepository orderRecordRepository) {
    this.orderRecordRepository = orderRecordRepository;
  }

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setBookRepository(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Autowired
  public void setOrderRepository(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public void deleteCartItems(Integer user_id, Long book_id) {
    Book book = bookRepository.getOne(book_id);
    User user = userRepository.getOne(user_id);
    cartRepository.deleteAllByUserAndBook(user, book);
  }

  @Override
  public List<CartItem> getCartItemsByUserId(Integer user_id) {
    User user = userRepository.getOne(user_id);
    return cartRepository.findAllByUser(user);
  }

  @Override
  public void addCartItems(Integer user_id, Long book_id) {
    CartItem cartItem = new CartItem();
    Book book = bookRepository.getOne(book_id);
    User user = userRepository.getOne(user_id);
    if (cartRepository.findByUserAndBook(user, book).isPresent()) {
      return;
    }
    cartItem.setBook(book);
    cartItem.setUser(user);

    cartRepository.saveAndFlush(cartItem);
  }

  @Override
  public Integer createOrder(Integer user_id, List<Long> book_id,
      List<Integer> amount, List<BigDecimal> price) {
    User customer = userRepository.getByUserId(user_id);
    OrderTable orderTable = new OrderTable();

    Timestamp ts = new Timestamp(System.currentTimeMillis());
    orderTable.setOrderTime(ts);
    orderTable.setStatus(0);
    orderTable.setAddress(customer.getAddress());
    orderTable.setUser(customer);
    orderTable.setPhoneNumber(customer.getTel());

    orderTable = orderRepository.saveAndFlush(orderTable);
    List<OrderRecord> items = new ArrayList<>();

    for (int i = 0; i < book_id.toArray().length; ++i) {
      OrderRecord orderRecord = new OrderRecord();
      orderRecord.setAmount(amount.get(i));
      orderRecord.setPrice(price.get(i));
      orderRecord.setBook(bookRepository.getOne(book_id.get(i)));

//      orderRecordRepository.saveAndFlush(orderRecord);
      items.add(orderRecord);
      orderTable.setItems(items);
      orderRepository.saveAndFlush(orderTable);

      Book book = bookRepository.getOne(book_id.get(i));
      User user = userRepository.getOne(user_id);
      cartRepository.deleteAllByUserAndBook(user, book);
    }

    orderTable.setItems(items);
    orderRepository.saveAndFlush(orderTable);
    return orderTable.getOrderId();
  }

}
