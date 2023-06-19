package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ConfigurationRepository implements IConfigurationRepository<Integer, Configuration>{

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();


    public ConfigurationRepository(@Qualifier("dbProperties") Properties props){
        logger.info("Initializing Repository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Iterable<Configuration> getAll() {
        logger.traceEntry("find all configurations");
        Connection connection = dbUtils.getConnection();
        List<Configuration> configurations = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("select * from configurations")){
            try(var resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String configurationPattern = resultSet.getString("configuration_pattern");
                    Configuration configuration = new Configuration(configurationPattern);
                    configuration.setId(id);
                    configurations.add(configuration);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
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
            try(var resultSet = ps.executeQuery()){
                if(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String configurationPattern = resultSet.getString("configuration_pattern");
                    Configuration configuration = new Configuration(configurationPattern);
                    configuration.setId(id);
                    logger.traceExit(configuration);
                    return configuration;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.traceExit("No configuration found with id {}",integer);
        return null;
    }

    @Override
    public void add(Configuration entity) {

        logger.traceEntry("saving configuration {} ",entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("insert into configurations(configuration_pattern) values (?)")){
            ps.setString(1,entity.getConfigurationPattern());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (Exception e) {
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
