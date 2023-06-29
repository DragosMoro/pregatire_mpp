package org.example;

import java.io.Serializable;

public class Game extends Entity<Integer> implements Serializable {
    private  int userId;
    private String duration;
    private int points;
    private String positionsChoosed;

    private String proposedPositions;
    private Boolean isOver = false;


    public Game (){}

    public Game(int userId, String duration, int points, String positionsCoosed, Boolean isOver) {
        this.userId = userId;
        this.duration = duration;
        this.points = points;
        this.positionsChoosed = positionsCoosed;
        this.isOver = isOver;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPositionsChoosed() {
        return positionsChoosed;
    }

    public void setPositionsChoosed(String positionsCoosed) {
        this.positionsChoosed = positionsCoosed;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean over) {
        isOver = over;
    }
}
