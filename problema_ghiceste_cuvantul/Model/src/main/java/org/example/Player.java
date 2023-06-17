package org.example;

import java.io.Serializable;

public class Player extends Entity<Integer> implements Serializable {
    private User user;
    private Game game;
    private String word;


}
