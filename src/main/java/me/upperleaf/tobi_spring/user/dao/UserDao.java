package me.upperleaf.tobi_spring.user.dao;

import me.upperleaf.tobi_spring.user.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
