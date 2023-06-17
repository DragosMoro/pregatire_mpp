package org.example;

import java.io.Serializable;

public class Game extends Entity<Integer> implements Serializable {
    private int userId;
    private int configurationId;
    private String date;

    private int tries;

    private StringBuffer proposedPositions = new StringBuffer();

    private Boolean isOver = false;

    public Game(int userId, int configurationId, String date, int tries, StringBuffer proposedPositions, Boolean isOver) {
        this.userId = userId;
        this.configurationId = configurationId;
        this.date = date;
        this.tries = tries;
        this.proposedPositions = proposedPositions;
        this.isOver = isOver;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public StringBuffer getProposedPositions() {
        return proposedPositions;
    }

    public void setProposedPositions(StringBuffer propsedPositions) {
        this.proposedPositions = propsedPositions;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }
}
