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
public class ConfigurationRepository implements IConfigurationRepository<Integer, Configuration>{
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    @Autowired
    public ConfigurationRepository(@Qualifier("dbProperties") Properties props) {
        logger.info("Initializing Repository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Iterable<Configuration> getAll() {
        logger.traceEntry("find all configurations");
        Connection connection = dbUtils.getConnection();
        List<Configuration> configurations = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("select * from configurations")){
            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    //public Configuration(String flagConfiguration){
                    int id = resultSet.getInt("id");
                    String flagConfiguration = resultSet.getString("flag_configuration");
                    Configuration configuration = new Configuration(flagConfiguration);
                    configuration.setId(id);
                    configurations.add(configuration);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(configurations);
        return configurations;
    }

    @Override
    public Configuration findById(Integer integer) {
        logger.traceEntry("find an configuration by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from configurations where id = ?")) {
            ps.setInt(1, integer);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String flagConfiguration = resultSet.getString("flag_configuration");
                    Configuration configuration = new Configuration(flagConfiguration);
                    configuration.setId(id);
                    logger.traceExit(configuration);
                    return configuration;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit("No configuration found with id {}", integer);
        return null;
    }

    @Override
    public void add(Configuration entity) {
        logger.traceEntry("saving configuration {} ",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("insert into configurations(flag_configuration) values (?)")){
            ps.setString(1,entity.getFlagConfiguration());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Configuration entity) {

    }
}
