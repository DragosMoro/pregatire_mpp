package client;

import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.rpcprotocol.ServicesRpcProxy;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private static int defaultServerPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws IOException{
        System.out.println("In start");
        Properties properties = new Properties();
        try{
            properties.load(Main.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            properties.list(System.out);
        } catch(IOException e){
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String serverIp = properties.getProperty("client.server.host", defaultServer);
        int serverPort = defaultServerPort;

        try{
            serverPort = Integer.parseInt(properties.getProperty("client.server.port"));
        } catch(NumberFormatException e){
            System.err.println("Wrong port number " + e.getMessage());
            System.out.println("Using default port: " + defaultServerPort);
        }

        System.out.println("Using server IP " + serverIp);
        System.out.println("Using server port " + serverPort);

        IService service = new ServicesRpcProxy(serverIp, serverPort);
        LoginController loginController = new LoginController(service);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/view/loginView.fxml"));
        fxmlLoader.setController(loginController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

//    static Service getService() {
//        Properties serverProps = new Properties();
//        try {
//            serverProps.load(new FileReader("db.config"));
//
//            System.out.println("Properties set. ");
//
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config " + e);
//            return null;
//        }
//        RefereeRepository refereeRepository = new RefereeRepository(serverProps);
//        ParticipantRepository participantRepository = new ParticipantRepository(serverProps);
//        TestRepository testRepository = new TestRepository(serverProps);
//        return new Service(refereeRepository, participantRepository, testRepository);
//    }

    public static void main(String[] args) {
        launch();
    }
}
