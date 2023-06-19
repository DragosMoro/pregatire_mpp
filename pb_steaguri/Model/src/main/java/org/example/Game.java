package org.example;

import java.io.Serializable;

public class Game extends Entity<Integer> implements Serializable {

    private int userId;
    private int configurationId;
    private String date;
    private int score;

    private int tries;

    private int guess;

    private int guessedPositions;

    private boolean isOver;

    private String wordStatus;

    private String message;

    public Game (){}

    public Game(int userId, int configurationId, String date, int score, int tries, int guess, int guessedPositions, boolean isOver, String wordStatus, String message) {
        this.userId = userId;
        this.configurationId = configurationId;
        this.date = date;
        this.score = score;
        this.tries = tries;
        this.guess = guess;
        this.guessedPositions = guessedPositions;
        this.isOver = isOver;
        this.wordStatus = wordStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public String getWordStatus() {
        return wordStatus;
    }

    public void setWordStatus(String wordStatus) {
        this.wordStatus = wordStatus;
    }

    public boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
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

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getGuessedPositions() {
        return guessedPositions;
    }

    public void setGuessedPositions(int guessedPositions) {
        this.guessedPositions = guessedPositions;
    }
}
