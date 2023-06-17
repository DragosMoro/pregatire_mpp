package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameRepository implements IGameRepository {
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public GameRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Game findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try (var ps = connection.prepareStatement("select * from games")) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int roundNumber = rs.getInt("round_number");
                    Game game = new Game(roundNumber);
                    game.setId(id);
                    games.add(game);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
        return games;
    }


    @Override
    public void save(Game entity) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try (var ps = connection.prepareStatement("insert into games(round_number) values (?)")) {
            ps.setInt(1, entity.getRoundNumber());
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Game entity) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try (var ps = connection.prepareStatement("update games set round_number=? where id=?")) {
            ps.setInt(1, entity.getRoundNumber());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}

