package org.example;

import java.io.Serializable;

public class ObjectDTO implements Serializable {
    private String username;
    private int tries;
    private String message;
    private String proposedPositions;



    public ObjectDTO(int tries, String message, String propsedPositions, String username) {
        this.tries = tries;
        this.message = message;
        this.proposedPositions = propsedPositions;
        this.username = username;
    }

    public ObjectDTO() {
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

    public String getProposedPositions() {
        return proposedPositions;
    }

    public void setProposedPositions(String propsedPositions) {
        this.proposedPositions = propsedPositions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
