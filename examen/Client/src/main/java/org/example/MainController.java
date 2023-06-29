package org.example;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class MainController implements IObserver {


    private IServices server;
    private User rootUser;
    private Game game;


    @FXML
    public TableColumn<ObjectDTO2, String> dateColumn;

    @FXML
    public GridPane gameGridPane;

    @FXML
    public TableView<ObjectDTO2> tableView;


    @FXML
    public TableColumn<ObjectDTO2, Integer> pointsColumn;

    @FXML
    public TableColumn<ObjectDTO2, String> usernameColumn;

    @FXML
    ObservableList<ObjectDTO2> modelAllObjects = FXCollections.observableArrayList();

    public MainController(){
        //this constructor is used by form
    }

    public MainController(IServices server) {
        this.server = server;
        System.out.println("Constructor with server param...");
    }


    public void setServer(IServices server, User rootUser) {
        this.server = server;
        this.rootUser = rootUser;
        System.out.println(rootUser);
        try {
            populate();
           String position = "1" + "x";

            Random rand = new Random();
            Configuration configuration;
            //int userId, String duration, int points, String positionsCoosed, Boolean isOver
            game = new Game(rootUser.getId(), LocalDateTime.now().toString(), 0,position , false);
            int randomNumber = rand.nextInt(10000) + 1;
            game.setId(randomNumber);
            for (int i = 0; i < 5; i++) {
                int row = rand.nextInt(4);
                int col = rand.nextInt(4);
                configuration = new Configuration(game.getId(),row,col);
                server.addConfiguration(configuration);
            }

        } catch (ServicesException e) {
            e.printStackTrace();
        }

    }




    @FXML
    public void initialize(){

        dateColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("duration"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, Integer>("points"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("username"));
        tableView.setItems(modelAllObjects);
    }

    private void populate() throws ServicesException {
        List<Game> games = server.getAllGames().stream().toList();
        Collection<ObjectDTO2> dtoObjectConfiguration = games.stream()
                .sorted((Game x, Game y) -> {
                    if (x.getPoints() <= y.getPoints())
                        return 1;
                    return -1;
                })

                .map(game -> {
                    try {

                        return new ObjectDTO2(server.getUserById(game.getUserId()).getUsername(), game.getPoints(), game.getDuration());

                    } catch (ServicesException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        modelAllObjects.setAll(dtoObjectConfiguration);
    }
    @Override
    public void endGame(Collection<Game> games) throws ServicesException {

        Platform.runLater(() -> {
            Collection<ObjectDTO2> dtoObjectConfiguration = games.stream()
                    .sorted((Game x, Game y) -> {
                        if (x.getPoints() <= y.getPoints())
                            return 1;
                        return -1;
                    })
                    //int tries, String message, String date, String username
                    .map(game -> {
                        try {

                                return new ObjectDTO2(server.getUserById(game.getUserId()).getUsername(), game.getPoints(), game.getDuration());

                            } catch (ServicesException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            modelAllObjects.setAll(dtoObjectConfiguration);
        });
   }

}
