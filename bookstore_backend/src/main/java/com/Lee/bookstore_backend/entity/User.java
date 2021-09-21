package com.Lee.bookstore_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
public class User {
    private Integer userId;
    private String nickname;
    private String email;
    private String tel;
    private String address;
    private String icon;
    private Integer abandon;

    public User() {
    }

    public User(Integer userId, String nickname, String email, String tel, String address,
        String icon, Integer abandon) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.icon = icon;
        this.abandon = abandon;
    }

    @Id
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "abandon")
    public Integer getAbandon() {
        return abandon;
    }

    public void setAbandon(Integer abandon) {
        this.abandon = abandon;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", nickname='" + nickname + '\'' +
            ", email='" + email + '\'' +
            ", tel='" + tel + '\'' +
            ", address='" + address + '\'' +
            ", icon='" + icon + '\'' +
            ", abandon=" + abandon +
            '}';
    }
}
