package org.example;


import java.util.Collection;
import java.util.Collections;

public interface IServices {
    User login(User user, IObserver client) throws ServicesException;
    void logout(User user, IObserver client) throws ServicesException;
    Game play(Game game) throws  ServicesException;
    Collection<Configuration> getAllConf() throws ServicesException;

    Configuration getConfById(Integer id) throws ServicesException;

    User getUserById(Integer data) throws ServicesException;
}
