package org.example;

import java.io.Serializable;

public class GameTable extends Entity<Integer> implements Serializable {
    private User user;

    private Game game;
    private int value;
    private int number;

    public GameTable(User user, Game game, int value, int number) {
        this.user = user;
        this.game = game;
        this.value = value;
        this.number = number;
    }

    public GameTable(){
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
