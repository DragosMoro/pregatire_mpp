package org.example;

import java.io.Serializable;

public class ObjectDTO2 implements Serializable {
    private String username;
    private int tries;
    private String message;
    private String date;

    private String guessedWord;

    public ObjectDTO2(int tries, String message, String date, String username) {
        this.tries = tries;
        this.message = message;
        this.date = date;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGuessedWord() {
        return guessedWord;
    }

    public void setGuessedWord(String guessedWord) {
        this.guessedWord = guessedWord;
    }
}
