package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.UserAuthority;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {

  @Query(value = "select * from bookstore.user_auth where password=:userPassword and username=:username", nativeQuery = true)
  UserAuthority checkAuthority(@Param("username") String username,
      @Param("userPassword") String userPassword);

  List<UserAuthority> findAllByUserIdentity(Integer userIdentity);

  List<UserAuthority> findAllByUserIdentityBetween(Integer userIdentity,
      Integer userIdentity2);

  List<UserAuthority> findAllByUsername(String username);
}
