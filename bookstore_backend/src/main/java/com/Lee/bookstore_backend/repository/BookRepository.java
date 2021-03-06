package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.Book;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface BookRepository extends JpaRepository<Book, Long>{

  @Query("SELECT book FROM OrderRecord GROUP BY book ORDER BY SUM(amount)")
  List<Book> getTopSellers();

  Page<Book> findByNameLike(String name, Pageable pageRequest);

  @Override
  @Query("select b from Book b where b.inventory <> -1")
  Page<Book> findAll(Pageable pageRequest);

  List<Book> findAllByType(String type);
}
