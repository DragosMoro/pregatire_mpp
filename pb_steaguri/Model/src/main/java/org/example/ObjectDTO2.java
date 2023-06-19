package org.example;

import java.io.Serializable;

public class ObjectDTO2 implements Serializable {

    private String username;
    private int score;
    private int guessedPositions;

    public ObjectDTO2(String username, int score, int guessedPositions) {
        this.username = username;
        this.score = score;
        this.guessedPositions = guessedPositions;
    }
    public ObjectDTO2() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGuessedPositions() {
        return guessedPositions;
    }

    public void setGuessedPositions(int guessedPositions) {
        this.guessedPositions = guessedPositions;
    }
}

