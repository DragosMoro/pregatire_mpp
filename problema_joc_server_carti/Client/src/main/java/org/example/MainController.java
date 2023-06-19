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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class MainController implements IObserver {


    private IServices server;
    private User rootUser;
    private Game game;

    private Configuration configuration;

    private List<String> serverChoices = new ArrayList<>();
    @FXML
    ObservableList<ObjectDTO2> modelAllObjects = FXCollections.observableArrayList();

    @FXML
    public Label configurationLabel;

    @FXML
    public TableColumn<ObjectDTO2, String> dateColumn;

    @FXML
    public Button playButton;

    @FXML
    public TextField playTextField;

    @FXML
    public TableColumn<ObjectDTO2, Integer> pointsColumn;

    @FXML
    public Label pointsLabel;

    @FXML
    public Label serverChoiceLabel;

    @FXML
    public TableView<ObjectDTO2> tableView;

    @FXML
    public TableColumn<ObjectDTO2,String> usernameColumn;


    public MainController(){
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

            serverChoiceLabel.setText("");
            configurationLabel.setText(configuration.getConfigurationPattern());
            //int userId, int configurationId, int points, String date, String choosedLetters, String choice, String serverChoice, int tries, boolean over)
            game = new Game(rootUser.getId(), configuration.getId(), 0, date, "", "", "", 0, false);
            String [] splitConfigurationPattern = configuration.getConfigurationPattern().split(" ");
            for(String s : splitConfigurationPattern){
                String [] split = s.split(",");
                serverChoices.add(split[0]);
                game.setPoints(game.getPoints() + Integer.parseInt(split[1]));
                }

            String points = String.valueOf(game.getPoints());
            pointsLabel.setText(points);
        }
        catch (ServicesException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void onPlayButtonAction() {
            String srvChoice = serverChoices.get(0);
            serverChoiceLabel.setText(srvChoice);
            game.setServerChoice(srvChoice);
        String choice = playTextField.getText();
        game.setChoice(choice);
        if(game.getTries() == 0)
        {
            game.setChoosedLetters(choice);
        }
        else {
            game.setChoosedLetters(game.getChoosedLetters() + " " + choice);
        }
        try{
            game = server.play(game);
            pointsLabel.setText(String.valueOf(game.getPoints()));
            playTextField.setText("");
            serverChoices.remove(srvChoice);
        } catch (ServicesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        if (game.getOver())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game over");
            alert.setHeaderText("Game over");
            alert.setContentText("You won!");
            alert.showAndWait();
        }


    }
    @FXML
    public void initialize(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("username"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, String>("date"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<ObjectDTO2, Integer>("points"));
        tableView.setItems(modelAllObjects);
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
                    //String username, int points, String date
                    .map(game -> {
                        try {

                                return new ObjectDTO2(server.getUserById(game.getUserId()).getUsername(),game.getPoints(), game.getDate());
                            } catch (ServicesException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            modelAllObjects.setAll(dtoObjectConfiguration);
        });
   }

}
