package com.Lee.bookstore_backend.dao;


import com.Lee.bookstore_backend.entity.Book;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.entity.User;
import java.sql.Timestamp;
import java.util.List;

public interface OrderDao {

  List<OrderTable> getOrder(Integer user_id);

  List<OrderRecord> getOrderItems(OrderTable orderTable);

  List<OrderTable> getAllOrders(Timestamp start, Timestamp end);

  OrderTable getOrderTable(Integer orderId);

  boolean isExist(Book book);

  List<OrderTable> getAllByUser(User user);

  List<OrderTable> getAllOrdersByUserId(User user, Timestamp start, Timestamp end);

  void finishOrder(Integer order_id);
}
