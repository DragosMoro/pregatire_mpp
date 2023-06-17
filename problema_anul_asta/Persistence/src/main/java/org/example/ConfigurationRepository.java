package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class ConfigurationRepository implements IConfigurationRepository<Integer, Configuration>{
    private SessionFactory sessionFactory;


    @Autowired
    public ConfigurationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Iterable<Configuration> getAll() {
        Collection<Configuration> configurations = new ArrayList<>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                configurations = session.createQuery("from Configuration ", Configuration.class).list();
                tx.commit();
                return configurations;
            }catch (RuntimeException ex){
                System.err.println("Error occurred to getAll " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Configuration findById(Integer integer) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Configuration configuration = session.createQuery("from Configuration where id = :id", Configuration.class)
                        .setParameter("id", integer).setMaxResults(1).uniqueResult();
                tx.commit();
                return configuration;
            }catch (RuntimeException ex){
                System.err.println("Error occurred to findById " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public void add(Configuration entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            }catch (RuntimeException ex){
                System.err.println("Error occurred to add method: " + ex.getMessage());
                if(tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Configuration entity) {

    }
}
