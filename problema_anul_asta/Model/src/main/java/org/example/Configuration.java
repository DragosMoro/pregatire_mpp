package org.example;

import java.io.Serializable;

public class Configuration extends Entity<Integer> implements Serializable {
    private int column;
    private int row;
    private String message;

    public Configuration(int column, int row, String message) {
        this.column = column;
        this.row = row;
        this.message = message;
    }

    public Configuration() {
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "column=" + column +
                ", row=" + row +
                ", message='" + message + '\'' +
                '}';
    }
}
