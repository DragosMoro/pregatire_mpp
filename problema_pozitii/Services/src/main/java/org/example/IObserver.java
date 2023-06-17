package org.example;

import javax.swing.text.Position;
import java.util.List;

public interface IObserver {
    void updateGameStarted(List<User> users, List<Integer> positions ) throws ServicesException;
    void updateGameRound(User user, int diceNumber, int position, int money) throws ServicesException;

}
