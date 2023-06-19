package service;

import model.Game;
import model.User;

public interface IObserver{
    void refreshAll(Game game);
    void playerUpdate(int Number);
}

