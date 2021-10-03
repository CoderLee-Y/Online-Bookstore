package com.Lee.bookstore_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_auth")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler", "userPassword"})
public class UserAuthority {
    private Integer userId;
    private String username;
    private String userPassword;
    private Integer userIdentity;

    public UserAuthority() {
    }

    public UserAuthority(Integer userId, String username, String userPassword,
        Integer userIdentity) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
        this.userIdentity = userIdentity;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Column(name="user_type")
    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
            "userId=" + userId +
            ", username='" + username + '\'' +
            ", userPassword='" + userPassword + '\'' +
            ", userIdentity=" + userIdentity +
            '}';
    }
}



