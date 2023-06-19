package client.controller;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import model.User;
import service.IService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField name;
    @FXML
    PasswordField password;

    IService service;

    public LoginController(IService srv) {
        service = srv;
    }


    @FXML
    protected void login() throws IOException {
        User user = null;
        User loggedUser = new User(name.getText(), password.getText());
        MainController controller = new MainController(service);
        try {
            user = service.login(loggedUser, controller);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect name or password", ButtonType.OK);
        }
        if (user != null && user.getPassword().equals(password.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
            controller.setUser(user);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 800, 600);
            controller.initialize();
            Stage currentStage = (Stage) name.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.logout();
                    System.exit(0);
                }
            });
            newStage.setScene(scene);
            newStage.setTitle("Triatlon");
            currentStage.close();
            newStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect name or password", ButtonType.OK);
            alert.show();
        }
    }

}
