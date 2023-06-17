package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameRepository implements IGameReposiotry{
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils jdbcUtils;
    public GameRepository(JdbcUtils jdbcUtils){
        this.jdbcUtils=jdbcUtils;
    }
    @Override
    public void add(Game entity) {

        logger.traceEntry();
        var con=jdbcUtils.getConnection();
        try(var ps=con.prepareStatement("insert into games (id,player1,player2,rounds) values (?,?,?,?)")){
            ps.setInt(1,entity.getId());
             ps.setInt(4,entity.getRounds());
            ps.executeUpdate();
        }
        catch (Exception ex){
            logger.error(ex);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Game entity) {

    }

    @Override
    public Game findByID(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Game> getAll() {
        return null;
    }
}
