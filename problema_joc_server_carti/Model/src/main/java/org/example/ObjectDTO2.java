package org.example;

public class ObjectDTO2 implements java.io.Serializable{
    private String username;
    private int points;
    private String date;

    public ObjectDTO2(String username, int points, String date) {
        this.username = username;
        this.points = points;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
