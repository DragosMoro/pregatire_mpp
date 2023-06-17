package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepository implements IUserRepository{
    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public UserRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }
    @Override
    public User findOne(Integer integer) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try (var ps = connection.prepareStatement("select * from users where id=?")) {
            ps.setInt(1, integer);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    return user;
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();

        try (var ps = connection.prepareStatement("select * from users")) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
        return users;

    }

    @Override
    public void save(User entity) {
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(User entity) {
    }

    @Override
    public User findByUsername(String username) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try (var ps = connection.prepareStatement("select * from users where username=?")) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String password = rs.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    return user;
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
        return null;
    }
}
