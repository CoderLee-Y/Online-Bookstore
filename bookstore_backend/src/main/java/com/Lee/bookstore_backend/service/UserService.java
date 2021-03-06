package com.Lee.bookstore_backend.service;


import com.Lee.bookstore_backend.dto.BigFans;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.entity.UserAuthority;
import com.alibaba.fastjson.JSONObject;
import java.sql.Timestamp;
import java.util.List;

public interface UserService {
    UserAuthority checkAuthority(String username, String userPassword);

    User getUser();

    User getUserById(Integer userId);

    List<User> getCustomer();

    List<UserAuthority> getAdmins();

    void changeMode(Integer userId);

    void register(Object object);

    void changeReceipt(Object object);

    JSONObject getReceipt(Integer userId);

    boolean isDupName(String name);

    List<BigFans> getBigFans(Timestamp start, Timestamp end);
}

