package org.example;

import java.io.Serializable;

public class ObjectDTO implements Serializable {
    private String username;
    private String proposedPositions;
    private String duration;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProposedPositions() {
        return proposedPositions;
    }

    public void setProposedPositions(String proposedPositions) {
        this.proposedPositions = proposedPositions;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ObjectDTO(String username, String proposedPositions, String duration) {
        this.username = username;
        this.proposedPositions = proposedPositions;
        this.duration = duration;
    }
}
