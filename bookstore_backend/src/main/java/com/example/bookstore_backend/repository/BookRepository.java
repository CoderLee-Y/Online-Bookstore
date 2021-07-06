package com.example.bookstore_backend.repository;

import com.example.bookstore_backend.entity.Book;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Long>{
  @Query("SELECT book FROM OrderRecord GROUP BY book ORDER BY SUM(amount)")
  List<Book> getTopSellers();

  @Transactional
  Page<Book> findByNameLike(String name, Pageable pageRequest);
}
