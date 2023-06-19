package service;
import model.Game;
import model.GameState;
import model.User;

import java.util.List;

public interface IService {

    List<User> getAllUsers() throws MyException;
    List<User> getActiveUsers() throws MyException;

    User getPlayer(int id) throws MyException;

    Game getGame() throws MyException;

    void sendNumber(int number) throws MyException;
    void saveGameState(GameState state) throws MyException;

    Boolean addPlayer(User player, IObserver gameObserver) throws MyException;

    User login(User user, IObserver userObserver) throws MyException;
    void logout(User user, IObserver userObserver) throws MyException;

    void gameFinished(Game game) throws MyException;
}
