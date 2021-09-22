package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

  List<User> findAllByUserIdIn(Collection<Integer> userId);

  User getByUserId(Integer userId);
}
