package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class UserRepository implements IUserRepository<Integer, User>{
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    @Autowired
    public UserRepository(Properties props) {
        logger.info("Initializing Repository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Iterable<User> getAll() {
        return null;
    }

    @Override
    public User findById(Integer integer) {
        logger.traceEntry("find an user by id");
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("select * from users where id = ?")){
            ps.setInt(1, integer);
            try(ResultSet resultSet = ps.executeQuery()){
                if(resultSet.next()){
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

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User getUserByUsername(String username) {
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
