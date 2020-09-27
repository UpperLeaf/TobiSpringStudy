package me.upperleaf.tobi_spring.user.service;

import me.upperleaf.tobi_spring.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
