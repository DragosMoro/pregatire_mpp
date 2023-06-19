package org.example;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;


public class MainController implements IObserver {


    private IServices server;
    private User rootUser;
    private Game game;

    private int row;
    private int col;

    private Configuration configuration;
    public Label label = new Label();

    @FXML
    public TableColumn<ObjectDTO2, String> dateColumn;


    @FXML
    public TableView<ObjectDTO2> tableView;

    @FXML
    public TableColumn<ObjectDTO2, String> textColumn;

    @FXML
    public TableColumn<ObjectDTO2, Integer> triesColumn;

    @FXML
    public TableColumn<ObjectDTO2, String> usernameColumn;


    @FXML
    ObservableList<ObjectDTO2> modelAllObjects = FXCollections.observableArrayList();

    private boolean isOver = false;
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
        try {
            Collection<Configuration> configurations = server.getAllConf();
            Random random = new Random();
            int num = random.nextInt(configurations.size());
            configuration = configurations.stream().toList().get(num);
            System.out.println(configuration);
            row = configuration.getRow();
            col = configuration.getColumn();
            String text = configuration.getMessage();
            label.setText(text);
            label.setOpacity(0);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Label cell = new Label();
                    cell.setText("ceva");
                    cell.setOpacity(0);
                    gameGridPane.add(cell, j, i,1,1);
                }
            }
            gameGridPane.add(label, col, row, 1, 1);

            game = new Game(rootUser.getId(), configuration.getId(), LocalDateTime.now().toString(), 0, "", false);
            initializeGrid();
        } catch (ServicesException e) {
            e.printStackTrace();
        }
    }

    public void initializeGrid() {
        for (Node node : gameGridPane.getChildren()) {
            if (node instanceof Control) {
                node.setOnMouseClicked(event -> cellClicked(node, event));
            }
        }
    }
    @FXML
    public void initialize(){
        textColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("message"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("date"));
        triesColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, Integer>("tries"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("username"));
        tableView.setItems(modelAllObjects);
    }


    @FXML
    private void cellClicked(Node cell, MouseEvent event) {
        if(isOver){
            return;
        }
        int rowIndex = GridPane.getRowIndex(cell) == null ? 0 : GridPane.getRowIndex(cell);
        int colIndex = GridPane.getColumnIndex(cell) == null ? 0 : GridPane.getColumnIndex(cell);
        System.out.println("Clicked cell: " + rowIndex + " " + colIndex);


        String position = rowIndex + "x" + colIndex+ " ";
        game.setProposedPositions(game.getProposedPositions() + position);
        try {
            game = server.play(game);
        } catch (ServicesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        if (game.getOver() &&  game.getTries()<4)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            label.setOpacity(1);
            alert.setTitle("Game over");
            alert.setHeaderText("Game over");
            alert.setContentText("You won!");
            alert.showAndWait();
            return;
        }
        if (game.getOver() && game.getTries()>=4)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game over");
            alert.setHeaderText("Game over");
            alert.setContentText("You lost!");
            alert.showAndWait();
        }
        }





    @Override
    public void endGame(Collection<Game> games) throws ServicesException {
        Platform.runLater(() -> {
            Collection<ObjectDTO2> dtoObjectConfiguration = games.stream()
                    .sorted((Game x, Game y) -> {
                        if (x.getTries() <= y.getTries())
                            return 1;
                        return -1;
                    })
                    //int tries, String message, String date, String username
                    .map(game -> {
                        try {
                            if(game.getTries() >=4)
                            {
                                return new ObjectDTO2(game.getTries(), "", game.getDate(), server.getUserById(game.getUserId()).getUsername());

                            }
                            else {
                                return new ObjectDTO2(game.getTries(), server.getConfById(game.getConfigurationId()).getMessage(), game.getDate(), server.getUserById(game.getUserId()).getUsername());
                            }
                            } catch (ServicesException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            modelAllObjects.setAll(dtoObjectConfiguration);
        });
    }
}
