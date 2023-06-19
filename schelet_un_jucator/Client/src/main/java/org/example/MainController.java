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
import java.util.Random;


public class MainController implements IObserver {


    private IServices server;
    private User rootUser;
    private Game game;

    @FXML
    public GridPane gameGridPane;
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
    }



    @Override
    public void endGame(Collection<Game> games) throws ServicesException {
//
//        Platform.runLater(() -> {
//            Collection<ObjectDTO2> dtoObjectConfiguration = games.stream()
//                    .sorted((Game x, Game y) -> {
//                        if (x.getTries() <= y.getTries())
//                            return 1;
//                        return -1;
//                    })
//                    //int tries, String message, String date, String username
//                    .map(game -> {
//                        try {
//                            if(game.getTries() >=4)
//                            {
//                                return new ObjectDTO2(game.getTries(), "", game.getDate(), server.getUserById(game.getUserId()).getUsername());
//
//                            }
//                            else {
//                                return new ObjectDTO2(game.getTries(), server.getConfById(game.getConfigurationId()).getMessage(), game.getDate(), server.getUserById(game.getUserId()).getUsername());
//                            }
//                            } catch (ServicesException e) {
//                            throw new RuntimeException(e);
//                        }
//                    })
//                    .toList();
//
//            modelAllObjects.setAll(dtoObjectConfiguration);
//        });
   }

}
