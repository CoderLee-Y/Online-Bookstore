package com.example.bookstore_backend.repository;

import com.example.bookstore_backend.entity.Book;
import com.example.bookstore_backend.entity.CartItem;
import com.example.bookstore_backend.entity.User;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartItem, Integer> {

  List<CartItem> findAllByUser(User user);

  @Transactional
  void deleteAllByUserAndBook(User user, Book book);

  @Transactional
  Optional<CartItem> findByUserAndBook(User user, Book book);
}
