package com.example.bookstore_backend.repository;

import com.example.bookstore_backend.entity.OrderRecord;
import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.User;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderTable, Integer> {
  List<OrderTable> getAllByUser(User user);

  List<OrderTable> getAllByOrderTimeBetween(Date a, Date b);

  List<OrderTable> getAllByUserAndOrderTimeBetween(User user, Date orderTime, Date orderTime2);
}
