package model;

public class GameState extends Entity<Integer> {
    private int playerID;
    private int gameID;
    private int money;
    private int position;

    public GameState() {
    }

    public GameState(int playerID, int gameID, int money, int position) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.money = money;
        this.position = position;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
