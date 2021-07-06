package com.example.bookstore_backend.serviceimpl;

import com.example.bookstore_backend.dao.OrderDao;
import com.example.bookstore_backend.dao.UserDao;
import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.OrderRecord;
import com.example.bookstore_backend.entity.User;
import com.example.bookstore_backend.service.OrderService;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService {

  OrderDao orderDao;
  UserDao userDao;

  @Autowired
  public OrderServiceImpl(OrderDao orderDao, UserDao userDao) {
    this.orderDao = orderDao;
    this.userDao = userDao;
  }

  @Override
  public void finishOrder(Integer order_id) {
    orderDao.finishOrder(order_id);
  }

  @Override
  public List<OrderTable> getOrder(Integer user_id) {
    return orderDao.getOrder(user_id);
  }

  @Override
  public List<OrderRecord> getOrderItems(Integer order_id) {
    OrderTable orderTable = orderDao.getOrderTable(order_id);
    return orderDao.getOrderItems(orderTable);
  }

  @Override
  public List<OrderTable> getAllOrders(Timestamp start, Timestamp end) {
    return orderDao.getAllOrders(start, end);
  }

  @Override
  public List<OrderTable> getAllOrdersByUserId(Integer userId, Timestamp start, Timestamp end) {
    User user = userDao.getUserById(userId);
    return orderDao.getAllOrdersByUserId(user, start, end);
  }
}
