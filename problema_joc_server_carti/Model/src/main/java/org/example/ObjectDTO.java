package org.example;

import java.io.Serializable;

public class ObjectDTO implements Serializable {

    private String username;

    private int points;

    private String chosenLetters;

    public ObjectDTO(String username, int points, String chosenLetters) {
        this.username = username;
        this.points = points;
        this.chosenLetters = chosenLetters;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getChosenLetters() {
        return chosenLetters;
    }

    public void setChosenLetters(String chosenLetters) {
        this.chosenLetters = chosenLetters;
    }
}
