package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.OrderRecord;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface OrderRecordRepository extends JpaRepository<OrderRecord, Integer> {
}
