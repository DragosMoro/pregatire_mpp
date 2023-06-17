package org.example;

import java.io.Serializable;

public class GameRound extends Entity<Integer> implements Serializable {
    private User user;

    private Game game;
    private int money;

    private int diceNumber;

    private int position;



    public GameRound(){
    }

    public GameRound(User user, Game game, int money, int diceNumber, int position) {
        this.user = user;
        this.game = game;
        this.money = money;
        this.diceNumber = diceNumber;
        this.position = position;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
