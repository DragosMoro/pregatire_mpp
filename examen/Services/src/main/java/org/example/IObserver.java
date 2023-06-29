package org.example;



import java.util.Collection;

public interface IObserver {
    void endGame(Collection<Game> games) throws ServicesException;
}
