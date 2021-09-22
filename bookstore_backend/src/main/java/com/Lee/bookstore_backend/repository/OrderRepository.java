package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.entity.User;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderTable, Integer> {
  List<OrderTable> getAllByUser(User user);

  List<OrderTable> getAllByOrderTimeBetween(Date a, Date b);

  List<OrderTable> getAllByUserAndOrderTimeBetween(User user, Date orderTime, Date orderTime2);
}
