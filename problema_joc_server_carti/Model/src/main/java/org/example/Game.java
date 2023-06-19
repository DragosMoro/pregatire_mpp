package org.example;

public class Game extends Entity<Integer>{

    private int userId;
    private int configurationId;
    private int points;

    private String date;

    private String choosedLetters;

    private String choice;

    private String serverChoice;

    private int tries;

    private boolean over;

    public Game (){}

      public Game(int userId, int configurationId, int points, String date, String choosedLetters, String choice, String serverChoice, int tries, boolean over) {
            this.userId = userId;
            this.configurationId = configurationId;
            this.points = points;
            this.date = date;
            this.choosedLetters = choosedLetters;
            this.choice = choice;
            this.serverChoice = serverChoice;
            this.tries = tries;
            this.over = over;
        }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public boolean getOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
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

    public String getChoosedLetters() {
        return choosedLetters;
    }

    public void setChoosedLetters(String choosedLetters) {
        this.choosedLetters = choosedLetters;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getServerChoice() {
        return serverChoice;
    }

    public void setServerChoice(String serverChoice) {
        this.serverChoice = serverChoice;
    }
}
