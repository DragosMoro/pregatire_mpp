package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository{
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils jdbcUtils;
    public UserRepository(JdbcUtils jdbcUtils){
        this.jdbcUtils=jdbcUtils;
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
    public User findByID(Integer integer) {
        return null;
    }

    @Override
    public Iterable<User> getAll() {
        logger.traceEntry();
        Connection con=jdbcUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(var ps=con.prepareStatement("select * from users")){
            try(var rs=ps.executeQuery()){
                while(rs.next()){
                    User user=extractEntity(rs);
                    users.add(user);
                }
            }
        }
        catch (Exception ex){
            logger.error(ex);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public User findByUsername(String username) {
        logger.traceEntry();
        Connection con=jdbcUtils.getConnection();
        try(var ps=con.prepareStatement("select * from users where username=?")){
            ps.setString(1,username);
            try(var rs=ps.executeQuery()){
                rs.next();
                return extractEntity(rs);
            }
        }
        catch (Exception ex){
            logger.error(ex);
        }
        return null;
    }


private User extractEntity(ResultSet rs) throws SQLException {
    int id=rs.getInt("id");
    String username=rs.getString("username");
    String password=rs.getString("password");
    User user = new User(id,username,password);
    user.setId(id);
    return user;
    }

}
