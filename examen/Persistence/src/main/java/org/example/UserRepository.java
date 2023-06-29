package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class UserRepository implements IUserRepository<Integer,User>{
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    @Autowired
    public UserRepository(@Qualifier("dbProperties")Properties props){
        logger.info("Initializing Repository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Iterable<User> getAll() {
        logger.traceEntry("find all users");
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("select * from users")){
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(users);
        return users;

    }

    @Override
    public User findById(Integer integer) {
        logger.traceEntry("find an user by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from users where id = ?")) {
            ps.setInt(1, integer);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    logger.traceExit(user);
                    return user;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public void add(User entity) {
        logger.traceEntry("saving user {} ",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("insert into users (username, password) values (?,?)")){
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            int result = ps.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting user with {}",integer);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("delete from users where id = ?")){
            ps.setInt(1, integer);
            int result = ps.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(User entity) {
        logger.traceEntry("updating user {} ",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("update users set username = ?, password = ? where id = ?")){
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setInt(3, entity.getId());
            int result = ps.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findByUsername(String username) {
        logger.traceEntry("find an user by username");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from users where username = ?")) {
            ps.setString(1, username);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    logger.traceExit(user);
                    return user;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
