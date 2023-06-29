package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GameRepository implements IGameRepository<Integer, Game>{
    private SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public GameRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Iterable<Game> getAll() {
        logger.traceEntry("getAllGames");
        Collection<Game> all = new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                all = session.createQuery("from Game ", Game.class).list();
                tx.commit();
                logger.traceExit("getAllGames");
                return all;
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.err.println("Error occurred to getAll " + ex.getMessage());
                if (tx != null)
                    tx.rollback();
                return new ArrayList<>();

            }
        }
        catch (Exception e){
            logger.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public Game findById(Integer integer) {
        logger.traceEntry("findByIdGame");
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Game game = session.createQuery("from Game where id = :id", Game.class)
                        .setParameter("id", integer).setMaxResults(1).uniqueResult();
                tx.commit();
                logger.traceExit("findByIdGame");
                return game;
            }catch (RuntimeException ex){
                logger.error(ex);
                System.err.println("Error occurred to findById " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public void add(Game entity) {
        logger.traceEntry("addGame");
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                logger.traceExit("addGame");
            }catch (RuntimeException ex){
                logger.error(ex);
                System.err.println("Error occurred to add " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
            }
        }

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleteGame");
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Game game = session.createQuery("from Game where id = :id", Game.class)
                        .setParameter("id", integer).setMaxResults(1).uniqueResult();
                session.delete(game);
                tx.commit();
                logger.traceExit("deleteGame");
            }catch (RuntimeException ex){
                logger.error(ex);
                System.err.println("Error occurred to delete " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
            }
        }

    }

    @Override
    public void update(Game entity) {
        logger.traceEntry("updateGame");
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.update(entity);
                tx.commit();
                logger.traceExit("updateGame");
            }catch (RuntimeException ex){
                logger.error(ex);
                System.err.println("Error occurred to update " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
            }
        }

    }

    public List<Game> findByUserId(Integer id) {
        logger.traceEntry("findByUserIdGame");
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                List<Game> games = session.createQuery("from Game where user_id = :id", Game.class)
                        .setParameter("id", id).list();
                tx.commit();
                logger.traceExit("findByUserIdGame");
                return games;
            }catch (RuntimeException ex){
                logger.error(ex);
                System.err.println("Error occurred to findById " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }
}
