package org.example;

public class ObjectDTO {
    private String username;
    private int tries;
    private String message;
    private StringBuffer proposedPositions = new StringBuffer();



    public ObjectDTO(int tries, String message, StringBuffer propsedPositions, String username) {
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

    public StringBuffer getProposedPositions() {
        return proposedPositions;
    }

    public void setProposedPositions(StringBuffer propsedPositions) {
        this.proposedPositions = propsedPositions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
