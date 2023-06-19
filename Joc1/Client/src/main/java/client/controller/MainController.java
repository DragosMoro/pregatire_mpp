package client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Game;
import model.User;
import service.IObserver;
import service.IService;


public class MainController implements IObserver {

    public static MainController instance;
    public static User user;
    private IService service;
    @FXML
    SplitPane mainPain;
    @FXML
    AnchorPane displayPane;
    @FXML
    Label userNameLabel, status;

    @FXML
    Button startButton;

    public MainController(IService service) {
        if (instance == null) {
            instance = this;
        }
        this.service = service;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void initialize() {
        userNameLabel.setText("Player: " + user.getName());
    }

    public void startGame() {
        GameController controller = new GameController(service, user);
        try {
            if (service.addPlayer(user, controller)) {
                //waiting to start
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/gameView.fxml"));
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), 800, 600);
                controller.initialize();
                Stage currentStage = (Stage) status.getScene().getWindow();
                Stage newStage = new Stage();
                ObservableList<Node> displayNodes = displayPane.getChildren();
                displayNodes.clear();
                displayNodes.addAll(scene.getRoot().getChildrenUnmodifiable());
                startButton.setDisable(true);
                status.setText("Waiting for all the players to join!");
            } else {
                //waiting for another session
                status.setText("The game started already, wait until it ends...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void seeParticipants() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/gameView.fxml"));
            UserController controller = new UserController(service);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 883, 501);
            controller.initialize();
            ObservableList<Node> displayNodes = displayPane.getChildren();
            displayNodes.clear();
            displayNodes.addAll(scene.getRoot().getChildrenUnmodifiable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logout() {
        try {
            service.logout(user, null);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/loginView.fxml"));
            LoginController controller = new LoginController(service);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 320, 340);
            Stage currentStage = (Stage) mainPain.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Login");
            currentStage.close();
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshAll(Game game) {
        Platform.runLater(() -> {
            System.out.println("refresh");
        });
    }

    @Override
    public void playerUpdate(int Number) {
        Platform.runLater(() -> {
            System.out.println("update");
        });
    }

}
