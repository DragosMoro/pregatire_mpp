package org.example;

public class ObjectDTO2 implements java.io.Serializable{
    private String username;
    private int points;
    private String duration;

    public ObjectDTO2(String username, int points, String duration) {
        this.username = username;
        this.points = points;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
