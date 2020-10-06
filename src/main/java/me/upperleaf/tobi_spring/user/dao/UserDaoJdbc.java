package me.upperleaf.tobi_spring.user.dao;

import me.upperleaf.tobi_spring.user.domain.Level;
import me.upperleaf.tobi_spring.user.domain.User;
import me.upperleaf.tobi_spring.user.service.SqlService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UserDaoJdbc implements UserDao{

    private String sqlAdd;

    private JdbcTemplate jdbcTemplate;

    private SqlService sqlService;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    };

    public void add(final User user)  {
        jdbcTemplate.update(sqlService.getSql("userAdd"),
                user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), userRowMapper, id);
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Integer.class);
    }

    @Override
    public void update(User user1) {
        this.jdbcTemplate.update(sqlService.getSql("userUpdate"),
                user1.getName(), user1.getPassword(), user1.getEmail(), user1.getLevel().intValue(), user1.getLogin(), user1.getRecommend(), user1.getId());
    }

    public void deleteAll() {
        jdbcTemplate.update( sqlService.getSql("userDeleteAll"));
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("userGetAll"), userRowMapper);
    }
}
