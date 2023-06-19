package repository;

import model.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class GameStateORMRepository implements GameStateRepository {
    private static final Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;
    public GameStateORMRepository() {
        logger.info("Initializing UserORMRepository");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public List<GameState> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<GameState> states = session.createQuery("from GameState", GameState.class).list();
                tx.commit();
                logger.info("States found: " + states.size());
                return states;
            } catch (RuntimeException ex) {
                System.err.println("Get Users Error: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public GameState get(Integer integer) {
        return null;
    }

    @Override
    public void save(GameState entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                logger.info("State saved");
            } catch (RuntimeException ex) {
                System.err.println("Save State Error: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(GameState entity) {

    }

    public List<GameState> getByIds(int playerId, int gameId) {
        List<GameState> states = getAll();
        if (states == null) {
            return null;
        }
        return states.stream().filter(state -> state.getPlayerID() == playerId && state.getGameID() == gameId).collect(Collectors.toList());
    }
}
