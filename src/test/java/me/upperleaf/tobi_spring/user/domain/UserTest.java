package me.upperleaf.tobi_spring.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        Arrays.stream(levels).forEach(level -> {
            if(level.nextLevel() == null)
                return;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        });
    }

    @Test
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        Arrays.stream(levels).forEach(level -> {
            if (level.nextLevel() != null) return;

            user.setLevel(level);
            assertThrows(IllegalStateException.class, () -> {
                user.upgradeLevel();
            });
        });
    }
}