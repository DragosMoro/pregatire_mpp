package org.example;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;


public class MainController implements IObserver {


    private IServices server;
    private User rootUser;
    private Game game;

    private Configuration configuration;

    @FXML
    public TableColumn<ObjectDTO,String> dateColumn;


    @FXML
    public TableColumn<ObjectDTO, Integer> guessedPositionsColumn;

    @FXML
    public TableColumn<ObjectDTO, Integer> pointsColumn;

    @FXML
    public TableView<ObjectDTO> tableView;

    @FXML
    public TableColumn<ObjectDTO,String> usernameColumn;
    @FXML
    public Button guessButton;

    @FXML
    public TextField guessTextField;

    @FXML
    public Label pointsLabel;

    @FXML
    public Label wordLabel;


    @FXML
    ObservableList<ObjectDTO> modelAllObjects = FXCollections.observableArrayList();

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

            String date = LocalDateTime.now().toString().substring(11,19);

            game = new Game(rootUser.getId(), configuration.getId(), date, 0, 0, 0, 0, false, "_________", "");
            pointsLabel.setText("0");
            wordLabel.setText(game.getWordStatus());
        }
        catch (ServicesException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO, String>("username"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO, String>("date"));
        guessedPositionsColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO, Integer>("guessedPositions"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO, Integer>("score"));
        tableView.setItems(modelAllObjects);
    }
    @FXML
    public void onGuessButtonAction(){
        int position = Integer.parseInt(guessTextField.getText());
        game.setGuess(position);
        int correctAnswer = game.getGuessedPositions();
        try{
            game = server.play(game);
        } catch (ServicesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        if(game.getGuessedPositions() > correctAnswer)
        {
            pointsLabel.setText(String.valueOf(game.getScore()));
            wordLabel.setText(game.getWordStatus());

        }
        else if(game.getGuessedPositions() == correctAnswer)
        {

            pointsLabel.setText(String.valueOf(game.getScore()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Information");
            alert.setContentText(game.getMessage());
            alert.showAndWait();


        }

        if (game.getIsOver() &&  game.getTries()<=4)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game over");
            alert.setHeaderText("Game over");
            alert.setContentText("You won!");
            alert.showAndWait();
            return;
        }
        else if (game.getIsOver() && game.getTries()>4)
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
            Collection<ObjectDTO> dtoObjectConfiguration = games.stream()
                    .sorted((Game x, Game y) -> {
                        if (x.getTries() <= y.getTries())
                            return 1;
                        return -1;
                    })
                    //String username, String date, int score, int guessedPositions
                    .map(game -> {
                        try {
                            return new ObjectDTO(server.getUserById(game.getUserId()).getUsername(), game.getDate(), game.getScore(), game.getGuessedPositions());
                        } catch (ServicesException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            modelAllObjects.setAll(dtoObjectConfiguration);
        });
   }

}
