package org.lee.searchMicroService.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.lee.searchMicroService.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findAllByAuthorLike(String author);

  List<Book> findAllByName(String name);
}

