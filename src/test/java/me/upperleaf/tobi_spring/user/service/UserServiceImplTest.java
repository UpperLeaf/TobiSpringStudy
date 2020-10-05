package me.upperleaf.tobi_spring.user.service;

import me.upperleaf.tobi_spring.user.dao.MockUserDao;
import me.upperleaf.tobi_spring.user.dao.UserDao;
import me.upperleaf.tobi_spring.user.domain.Level;
import me.upperleaf.tobi_spring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static me.upperleaf.tobi_spring.user.service.DefaultUserLevelUpgradePolicy.MIN_LOG_COUNT_FOR_SILVER;
import static me.upperleaf.tobi_spring.user.service.DefaultUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceImplTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @Test
    public void bean() {
        assertThat(userService, is(notNullValue()));
    }

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", "test1@email.com", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", "test2@email.com", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", "test3@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", "test4@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", "test5@email.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @DirtiesContext
    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl userService = new UserServiceImpl();

        TestUserLevelUpgradePolicy testUserLevelUpgradePolicy = new TestUserLevelUpgradePolicy(users.get(3).getId(), userDao);
        testUserLevelUpgradePolicy.setMailSender(mailSender);

        userService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy);
        userService.setUserDao(userDao);

        TxProxyFactoryBean txProxyFactoryBean = applicationContext.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTransactionManager(transactionManager);
        txProxyFactoryBean.setTarget(userService);

        UserService txUserService = (UserService)txProxyFactoryBean.getObject();

        userDao.deleteAll();
        users.forEach(userService::add);
        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException Expected");
        } catch (TestUserServiceException ignored) {

        }
        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void upgradeLevels() {
        UserServiceImpl userService = new UserServiceImpl();

        MockUserDao userDao = new MockUserDao(this.users);

        MockMailSender mailSender = new MockMailSender();

        DefaultUserLevelUpgradePolicy policy = new DefaultUserLevelUpgradePolicy();
        policy.setUserDao(userDao);
        policy.setMailSender(mailSender);

        userService.setUserDao(userDao);
        userService.setUserLevelUpgradePolicy(policy);

        userService.upgradeLevels();

        List<User> updated = userDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

    }


    private void checkUserAndLevel(User user, String expectedId, Level expectedLevel) {
        assertThat(user.getId(), is(expectedId));
        assertThat(user.getLevel(), is(expectedLevel));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User usersWithoutLevel = users.get(0);
        usersWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(usersWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(usersWithoutLevel.getId());

        checkLevel(userWithLevelRead, userWithLevel.getLevel());
        checkLevel(userWithoutLevelRead, Level.BASIC);
    }

    private void checkLevel(User user, Level level) {
        assertThat(user.getLevel(), is(level));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}