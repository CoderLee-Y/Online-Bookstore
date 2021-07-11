package com.example.bookstore_backend.repository;

import com.example.bookstore_backend.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
  List<User> findAllByUserIdIn(Collection<Integer> userId);
}
