package org.example;

import java.io.Serializable;

public class Configuration extends Entity<Integer> implements Serializable {
    private int gameId;
    private int row;
    private int column;

    public Configuration( int gameId, int row, int column) {
        this.gameId = gameId;
        this.row = row;
        this.column = column;
    }

    public Configuration(){}


    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
