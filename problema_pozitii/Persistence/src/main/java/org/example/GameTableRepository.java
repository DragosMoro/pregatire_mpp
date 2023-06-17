package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class GameTableRepository implements IGameTableRepositry{
    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public GameTableRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }
    @Override
    public GameTable findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<GameTable> findAll() {
        return null;
    }

    @Override
    public void save(GameTable entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(GameTable entity) {


    }
}
