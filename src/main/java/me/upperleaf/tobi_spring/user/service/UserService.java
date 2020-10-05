package me.upperleaf.tobi_spring.user.service;

import me.upperleaf.tobi_spring.user.domain.User;

public interface UserService {
    void upgradeLevels();
    void add(User user);
}
