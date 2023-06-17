package org.example;

public interface IService {
    User login(User user, IObserver player) throws ServicesException;
    void logout(User user, IObserver player) throws ServicesException;


    void startGame(User user, IObserver player) throws ServicesException;
}
