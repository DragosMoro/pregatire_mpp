package model;

import java.util.Map;

public class Game extends Entity<Integer> {
    private int[] positions;
    private int player1 = -1, player2 = -1, player3 = -1;
    private int score1, score2, score3;


    public Game() {}
    public Game(int[] positions, int player1, int player2, int player3, int score1, int score2, int score3) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.positions = positions;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
    }
    public int[] getPositions() {
        return positions;
    }

    public void setPositions(int[] positions) {
        this.positions = positions;
    }

    public Integer getPlayer1() {
        return player1;
    }

    public void setPlayer1(Integer player1) {
        this.player1 = player1;
    }

    public Integer getPlayer2() {
        return player2;
    }

    public void setPlayer2(Integer player2) {
        this.player2 = player2;
    }

    public Integer getPlayer3() {
        return player3;
    }

    public void setPlayer3(Integer player3) {
        this.player3 = player3;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScore3() {
        return score3;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }
}
