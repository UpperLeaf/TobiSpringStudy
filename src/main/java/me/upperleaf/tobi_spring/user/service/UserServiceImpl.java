package me.upperleaf.tobi_spring.user.service;

import me.upperleaf.tobi_spring.user.dao.UserDao;
import me.upperleaf.tobi_spring.user.domain.Level;
import me.upperleaf.tobi_spring.user.domain.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    UserLevelUpgradePolicy userLevelUpgradePolicy;
    UserDao userDao;

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        users.forEach((user) -> {
            if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                userLevelUpgradePolicy.upgradeLevel(user);
            }
        });
    }

    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
