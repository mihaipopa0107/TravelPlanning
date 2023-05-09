package com.mihai.models;
import com.orm.SugarRecord;

import java.util.Base64;

public class Account extends SugarRecord<Account> {
    private String username;
    private String address;
    private String password;
    private String avatar;
    private boolean isAdmin;

    public Account() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return new String(Base64.getDecoder().decode(password));
    }

    public void setPassword(String password) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}