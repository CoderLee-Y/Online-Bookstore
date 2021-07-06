package com.example.bookstore_backend.service;


import com.example.bookstore_backend.dto.BigFans;
import com.example.bookstore_backend.entity.User;
import com.example.bookstore_backend.entity.UserAuthority;
import java.sql.Timestamp;
import java.util.List;
import net.sf.json.JSONObject;

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

