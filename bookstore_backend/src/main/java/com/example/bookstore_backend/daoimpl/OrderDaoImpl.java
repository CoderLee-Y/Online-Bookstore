package com.example.bookstore_backend.daoimpl;

import com.example.bookstore_backend.dao.OrderDao;
import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.OrderRecord;
import com.example.bookstore_backend.entity.User;
import com.example.bookstore_backend.repository.OrderRecordRepository;
import com.example.bookstore_backend.repository.OrderRepository;
import com.example.bookstore_backend.repository.UserRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

  OrderRepository orderRepository;
  UserRepository userRepository;
  OrderRecordRepository orderRecordRepository;

  @Autowired
  public void setOrderRepository(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Autowired
  public void setOrderRecordRepository(OrderRecordRepository orderRecordRepository) {
    this.orderRecordRepository = orderRecordRepository;
  }

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<OrderTable> getOrder(Integer user_id) {
    return orderRepository.getAllByUser(userRepository.getOne(user_id));
  }

  @Override
  public List<OrderRecord> getOrderItems(OrderTable orderTable) {
    return orderTable.getItems();
  }

  @Override
  public List<OrderTable> getAllOrders(Timestamp start, Timestamp end) {
    return orderRepository.getAllByOrderTimeBetween(start, end);
  }

  @Override
  public void finishOrder(Integer order_id) {
    OrderTable orderTable = orderRepository.getOne(order_id);
    orderTable.setStatus(1);
    orderRepository.saveAndFlush(orderTable);
  }

  @Override
  public OrderTable getOrderTable(Integer orderId) {
    return orderRepository.getOne(orderId);
  }

  @Override
  public boolean isExist(Book book) {
    return false;
  }

  @Override
  public List<OrderTable> getAllByUser(User user) {
    return orderRepository.getAllByUser(user);
  }

  @Override
  public List<OrderTable> getAllOrdersByUserId(User user, Timestamp start, Timestamp end) {
    return orderRepository.getAllByUserAndOrderTimeBetween(user, start, end);
  }
}
