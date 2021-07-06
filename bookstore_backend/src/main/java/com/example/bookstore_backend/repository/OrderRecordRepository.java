package com.example.bookstore_backend.repository;

import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.entity.OrderRecord;
import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.User;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderRecordRepository extends JpaRepository<OrderRecord, Integer> {
}
