package repository;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository {
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public UserDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<User> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from users")) {
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    String password = result.getString("password");
                    User user = new User(name, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" +e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public User get(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from users where id=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                String name = result.getString("name");
                String password = result.getString("password");
                User user = new User(name, password);
                user.setId(id);
                logger.traceExit(user);
                return user;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" +e);
        }
        return null;
    }

    @Override
    public void save(User user) {
        logger.traceEntry("saving task{} ", user);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into users(name, password) values(?,?)")) {
            preStmt.setString(1, user.getName());
            preStmt.setString(2, user.getPassword());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" +ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("delete user ", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("delete from users where id = ?")) {
            statement.setInt(1, id);
            statement.execute();
        } catch(SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(User user) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("update users set name = ?, password = ? where id = ?")) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch(SQLException ex) {
           logger.error(ex);
           System.out.println("Error DB" + ex);
        }
    }

    @Override
    public User findByName(String name) {
        List<User> users = getAll();
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
}
