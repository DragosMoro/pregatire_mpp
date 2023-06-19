package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class GameRepository implements IGameRepository<Integer, Game> {
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;

    @Autowired
    public GameRepository(@Qualifier("dbProperties") Properties props) {
        logger.info("Initializing Repository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public Iterable<Game> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try (var ps = con.prepareStatement("select * from Games")) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int userId = rs.getInt("user_id");
                    int configurationId = rs.getInt("configuration_id");
                    String date = rs.getString("date");
                    int tries = rs.getInt("tries");
                    String propsedPositions = rs.getString("proposed_positions");
                    Boolean isOver = rs.getBoolean("is_over");
                    Game game = new Game(userId, configurationId, date, tries, propsedPositions, isOver);
                    game.setId(id);
                    games.add(game);


                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(games);
        return games;
    }

    @Override
    public Game findById(Integer integer) {
        logger.traceEntry("finding game with id {}", integer);
        Connection con = dbUtils.getConnection();
        try (var ps = con.prepareStatement("select * from Games where id = ?")) {
            ps.setInt(1, integer);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int userId = rs.getInt("user_id");
                    int configurationId = rs.getInt("configuration_id");
                    String date = rs.getString("date");
                    int tries = rs.getInt("tries");
                    String propsedPositions = rs.getString("proposed_positions");
                    Boolean isOver = rs.getBoolean("is_over");
                    Game game = new Game(userId, configurationId, date, tries, propsedPositions, isOver);
                    game.setId(id);
                    logger.traceExit(game);
                    return game;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit("no game found with id {}", integer);
        return null;

    }

    @Override
    public void add(Game entity) {

        logger.traceEntry("saving game {} ", entity);
        Connection con = dbUtils.getConnection();
        try (var ps = con.prepareStatement("insert into Games (user_id, configuration_id, date, tries, proposed_positions, is_over) values (?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, entity.getUserId());
            ps.setInt(2, entity.getConfigurationId());
            ps.setString(3, entity.getDate());
            ps.setInt(4, entity.getTries());
            ps.setString(5, entity.getProposedPositions());
            ps.setBoolean(6, entity.getOver());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Game entity) {

    }
}

