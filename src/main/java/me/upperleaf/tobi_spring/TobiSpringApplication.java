package me.upperleaf.tobi_spring;

import me.upperleaf.tobi_spring.user.User;
import me.upperleaf.tobi_spring.user.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        SpringApplication.run(TobiSpringApplication.class, args);
//        SpringApplication app = new SpringApplication();
//        app.setWebApplicationType(WebApplicationType.NONE);
//        app.run(TobiSpringApplication.class, args);

        UserDao userDao = new UserDao();

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
