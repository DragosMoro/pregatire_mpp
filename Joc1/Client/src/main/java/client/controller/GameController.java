package client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Game;
import model.GameState;
import model.User;
import service.IObserver;
import service.IService;

import java.util.Arrays;

public class GameController implements IObserver {

    int currentPlayer = 0, rounds = 0;
    int myId = 0;
    User user = null;
    Player[] players = {null, null, null};
    Game game = null;
    IService service;

    @FXML
    Button generateButton;

    @FXML
    Label player1Status, player2Status, player3Status, board, status;
    public GameController(IService service, User user) {
       this.service = service;
       this.user = user;
    }

    public void initialize() {
    }

    public void generateNumber() {
        int number = (int)(1 + Math.random() * 3);
        int moneyMade = 0;
        int pos = players[myId].pos + number;
        if (pos >= 5) {
            moneyMade += 5;
            pos %= 5;
        }
        players[myId].pos = pos;
        moneyMade -= game.getPositions()[pos];
        if (players[0].pos == players[1].pos && players[1].pos == players[2].pos) {
            moneyMade += game.getPositions()[pos];
        } else if (myId == 0 && (players[1].pos == pos || players[2].pos == pos)) {
            moneyMade += game.getPositions()[pos] / 2;
            if (players[1].pos == pos) {
                players[1].money += game.getPositions()[pos] / 2;
            } else {
                players[2].money += game.getPositions()[pos] / 2;

            }
        } else if (myId == 1 && (pos == players[0].pos || players[2].pos == pos)) {
            moneyMade += game.getPositions()[pos] / 2;
            if (players[0].pos == pos) {
                players[0].money += game.getPositions()[pos] / 2;
            } else {
                players[2].money += game.getPositions()[pos] / 2;

            }
        } else if (myId == 2 && (pos == players[0].pos || pos == players[1].pos)) {
            moneyMade += game.getPositions()[pos] / 2;
            if (players[0].pos == pos) {
                players[0].money += game.getPositions()[pos] / 2;
            } else {
                players[1].money += game.getPositions()[pos] / 2;

            }
        }
        players[myId].money += moneyMade;
        GameState state = new GameState(user.getId(), game.getId(), moneyMade, pos);
        try {
            service.saveGameState(state);
            service.sendNumber(number);
        } catch (Exception e){
           e.printStackTrace();
        }
        generateButton.setDisable(true);
    }

    private void updateDisplay(int n) {
        String additional = " generated " + n;
        player1Status.setText("Player1: " + players[0].name + " - money: " + players[0].money + " - pos: " + (players[0].pos + 1) + (currentPlayer == 0? additional : ""));
        player2Status.setText("Player2: " + players[1].name + " - money: " + players[1].money + " - pos:  " + (players[1].pos + 1) + (currentPlayer == 1? additional : ""));
        player3Status.setText("Player3: " + players[2].name + " - money: " + players[2].money + " - pos: " + (players[2].pos + 1) + (currentPlayer == 2? additional : ""));
        if (currentPlayer == 2) {
            currentPlayer = 0;
            rounds++;
        } else {
            currentPlayer++;
        }
        board.setText(Arrays.toString(game.getPositions()) + "  ---   current player: " + (currentPlayer + 1));
    }

    public void playerUpdate(int number) {
        Platform.runLater(() -> {
            if (currentPlayer != myId) {
                int pos = players[currentPlayer].pos + number;
                if (pos >= 5) {
                    players[currentPlayer].money += 5;
                    pos %= 5;
                }
                int lastPlayer = 3 - myId - currentPlayer;
                players[currentPlayer].money -= game.getPositions()[pos];
                if (pos == players[myId].pos && pos == players[lastPlayer].pos) {
                    players[currentPlayer].money += game.getPositions()[pos];
                } else if (players[myId].pos == pos) {
                    players[myId].money += game.getPositions()[pos] / 2;
                    players[currentPlayer].money += game.getPositions()[pos] / 2;
                } else if (players[lastPlayer].pos == pos) {
                    players[lastPlayer].money += game.getPositions()[pos] / 2;
                    players[currentPlayer].money += game.getPositions()[pos] / 2;
                }
                players[currentPlayer].pos = pos;
            }
            updateDisplay(number);
            if (rounds == 3) {
                game.setScore1(players[0].money);
                game.setScore2(players[1].money);
                game.setScore3(players[2].money);
                String text = "Game finished! Winner: ";
                if (players[0].money > players[1].money && players[0].money > players[2].money) {
                    text += players[0].name;
                } else if (players[1].money > players[0].money && players[1].money > players[2].money) {
                    text += players[2].name;
                } else if (players[2].money > players[0].money && players[2].money > players[1].money) {
                    text += players[2].name;
                } else {
                    text += "Tie";
                }
                System.out.println(myId);
                status.setText(text);
                if (currentPlayer == myId) {
                    try {
                        service.gameFinished(game);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (players[currentPlayer].id == user.getId()) {
                generateButton.setDisable(false);
            }
        });
    }

    @Override
    public void refreshAll(Game game) {
        Platform.runLater(() -> {
            this.game = game;
            try {
                System.out.println(game.getPlayer1() + " " + game.getPlayer2() + " " + game.getPlayer3());
                User newuser = service.getPlayer(game.getPlayer1());
                players[0] = new Player(newuser.getId(), newuser.getName());
                newuser = service.getPlayer(game.getPlayer2());
                players[1] = new Player(newuser.getId(), newuser.getName());
                newuser = service.getPlayer(game.getPlayer3());
                players[2] = new Player(newuser.getId(), newuser.getName());
                player1Status.setText("Player1: " + players[0].name + " - money: " + players[0].money + " - pos: " + (players[0].pos + 1));
                player2Status.setText("Player2: " + players[1].name + " - money: " + players[1].money + " - pos:  " + (players[1].pos + 1));
                player3Status.setText("Player3: " + players[2].name + " - money: " + players[2].money + " - pos: " + (players[2].pos + 1));
                board.setText(Arrays.toString(game.getPositions()) + "  ---   current player: " + (currentPlayer + 1));
                status.setText("Game is running!");
                if (players[0].id == user.getId()) {
                    myId = 0;
                    generateButton.setDisable(false);
                } else if (players[1].id == user.getId()) {
                    myId = 1;
                } else {
                    myId = 2;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        });
    }
    public class Player {
        public int money, pos, id;
        String name;

        Player(int id, String name) {
            this.money = 50;
            this.pos = -1;
            this.name = name;
            this.id = id;
        }

    }
}
