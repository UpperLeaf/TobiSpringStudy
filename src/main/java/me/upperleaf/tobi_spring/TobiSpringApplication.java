package me.upperleaf.tobi_spring;

import me.upperleaf.tobi_spring.user.User;
import me.upperleaf.tobi_spring.user.dao.DaoFactory;
import me.upperleaf.tobi_spring.user.dao.UserDao;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("upperleaf");
        user.setName("김상엽");
        user.setPassword("password");

        userDao.add(user);

        User user2 = userDao.get(user.getId());

        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
