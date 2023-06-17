package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class GameRoundRepository implements IGameRoundRepository{
    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public GameRoundRepository(Properties serverProps) {
        dbUtils = new JdbcUtils(serverProps);
    }

    @Override
    public GameRound findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<GameRound> findAll() {
        return null;
    }

    @Override
    public void save(GameRound entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(GameRound entity) {

    }
}
