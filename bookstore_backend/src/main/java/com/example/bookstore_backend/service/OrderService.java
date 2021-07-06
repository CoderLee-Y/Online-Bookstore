package com.example.bookstore_backend.service;

import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.OrderRecord;
import java.sql.Timestamp;
import java.util.List;

public interface OrderService {

  List<OrderTable> getOrder(Integer user_id);

  List<OrderRecord> getOrderItems(Integer order_id);

  List<OrderTable> getAllOrders(Timestamp start, Timestamp end);

  List<OrderTable> getAllOrdersByUserId(Integer userId, Timestamp start, Timestamp end);

  void finishOrder(Integer order_id);
}
