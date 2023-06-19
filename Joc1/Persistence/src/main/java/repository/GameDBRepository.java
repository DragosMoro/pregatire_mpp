package repository;

import model.Game;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GameDBRepository implements GameRepository {
    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public GameDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }


    public Game getLast(){
        List<Game> all = getAll();
        Game result = all.get(0);
        for (Game game : all) {
           if (game.getId() > result.getId()) {
               result = game;
           }
        }
        return result;
    }

    private Game readGame(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        int[] positions = Arrays.stream(result.getString("positions").split(", ")).mapToInt(Integer::parseInt).toArray();
        int player1 =  result.getInt("player1_id");
        int player2 =  result.getInt("player2_id");
        int player3 =  result.getInt("player3_id");
        int score1 =  result.getInt("score1");
        int score2 =  result.getInt("score2");
        int score3 =  result.getInt("score3");
        Game game = new Game(positions, player1, player2, player3, score1, score2, score3);
        game.setId(id);
        return game;
    }

    @Override
    public List<Game> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from games")) {
            try(ResultSet result = preStmt.executeQuery()){
                while (result.next()) {
                    games.add(readGame(result));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" +e);
        }
        logger.traceExit(games);
        return games;
    }

    @Override
    public Game get(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from games where id=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                return readGame(result);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" +e);
        }
        return null;
    }

    @Override
    public void save(Game game) {
        logger.traceEntry("saving task{} ", game);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into games(positions, player1_id, player2_id, player3_id) values(?,?,?,?)")) {
            preStmt.setString(1, Arrays.toString(game.getPositions()).replace("[","").replace("]", ""));
            preStmt.setInt(2, game.getPlayer1());
            preStmt.setInt(3, game.getPlayer2());
            preStmt.setInt(4, game.getPlayer3());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" +ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("delete game ", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("delete from games where id = ?")) {
            statement.setInt(1, id);
            statement.execute();
        } catch(SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Game game) {
        logger.traceEntry("update game ", game.getId());
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("update games set positions = ?, player1_id = ?, player2_id = ?, player3_id = ?, score1 = ?, score2 = ?, score3 = ? where id = ?")) {
            statement.setString(1, Arrays.toString(game.getPositions()).replace("[","").replace("]", ""));
            statement.setInt(2, game.getPlayer1());
            statement.setInt(3, game.getPlayer2());
            statement.setInt(4, game.getPlayer3());
            statement.setInt(5, game.getScore1());
            statement.setInt(6, game.getScore2());
            statement.setInt(7, game.getScore3());
            statement.setInt(8, game.getId());
            statement.execute();
        } catch(SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
    }
}
