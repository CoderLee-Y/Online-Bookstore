package com.Lee.bookstore_backend.dao;

import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.entity.UserAuthority;
import java.util.List;

public interface UserDao {
    UserAuthority checkAuthority(String userAccount, String userPassword);

    User getUser();

  void changeReceipt(Object object);

    User getUserById(Integer userId);

  List<User> getCustomer();

  List<UserAuthority> getAdmins();

  void changeMode(Integer userId);

  void register(Object object);

  boolean isDupName(String name);

  User getById(Integer userId);
}

