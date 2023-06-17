package org.example;

import java.io.Serializable;

public class Game extends Entity<Integer> implements Serializable {
    private int roundNumber;

    public Game(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Game() {
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
