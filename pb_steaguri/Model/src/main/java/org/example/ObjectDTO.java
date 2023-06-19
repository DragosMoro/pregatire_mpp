package org.example;

import java.io.Serializable;

public class ObjectDTO implements Serializable {

    private String username;
    private String date;

    private int score;
    private int guessedPositions;

    public ObjectDTO(String username, String date, int score, int guessedPositions) {
        this.username = username;
        this.date = date;
        this.score = score;
        this.guessedPositions = guessedPositions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
