package org.example;

import java.util.logging.Logger;

public class RoundRepository implements IRoundRepository{
    private static final Logger logger= Logger.getLogger(RoundRepository.class.getName());
    private JdbcUtils jdbcUtils;
    public RoundRepository(JdbcUtils jdbcUtils){
        this.jdbcUtils=jdbcUtils;
    }

    @Override
    public void add(Round entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Round entity) {

    }

    @Override
    public Round findByID(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Round> getAll() {


    }
}
