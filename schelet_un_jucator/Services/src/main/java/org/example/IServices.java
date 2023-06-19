package org.example;


import java.util.Collection;

public interface IServices {
    User login(User user, IObserver client) throws ServicesException;
    void logout(User user, IObserver client) throws ServicesException;
    Game play(Game game) throws  ServicesException;
    User getUserById(Integer data) throws ServicesException;
}
