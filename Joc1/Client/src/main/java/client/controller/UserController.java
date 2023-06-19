package client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.User;
import service.IService;
import service.MyException;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserController {
    IService service;
    @FXML
    TextField searchedUser;

    @FXML
    ListView<User> userList;

    ObservableList<User> userModel = FXCollections.observableArrayList();
    public UserController(IService service) {
        this.service = service;
    }

    public void initialize() {
        userList.setItems(userModel);

        userList.setCellFactory(userListView -> {
            return new ListCell<>() {

                public void updateItem(User user, boolean empty) {
                    if (user == null) {
                        return;
                    }
                    super.updateItem(user, empty);
                    HBox hbox = new HBox();
                    Label nameLabel = new Label();
                    nameLabel.setText(user.getName());
                    hbox.getChildren().addAll(nameLabel);
                    setGraphic(hbox);
                }
            };
        });
        try {
            System.out.println(service.getActiveUsers());
            userModel.setAll((List<User>) service.getActiveUsers());
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPoints() {
       System.out.println("Add");
    }
    private void showDetails(User user) {
        System.out.println(user.getName());
    }


    private void handleFilter() {
        Predicate<User> searched = user -> user.getName().startsWith(searchedUser.getText());
        try {
            userModel.setAll(((List<User>) service.getAllUsers()).stream().filter(searched).collect(Collectors.toList()));
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }

}
