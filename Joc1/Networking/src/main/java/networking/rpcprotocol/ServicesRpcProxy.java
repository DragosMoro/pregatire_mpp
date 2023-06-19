package networking.rpcprotocol;


import model.Game;
import model.GameState;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.IObserver;
import service.IService;
import service.MyException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IService{

    private final String host;
    private final int port;

    private IObserver userObserver;
    private IObserver playerObserver;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private static final Logger logger = LogManager.getLogger();

    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port){
        logger.info("ServicesRpcProxy created");
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<>();
    }
    private void closeConnection(){
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            userObserver = null;
            logger.info("Closed connection");
        } catch(IOException e){
            logger.error("Error closing connection: " + e);
        }
    }

    private void sendRequest(Request request) throws MyException{
        try{
            System.out.println("Sending request");
            output.writeObject(request);
            output.flush();
            logger.info("Request sent: " + request);
        } catch(IOException e){
            logger.error("Error sending object " + e);
            throw new MyException("Error sending object " + e);
        }
    }

    private Response readResponse() throws MyException{
        Response response = null;
        try{
            response = qresponses.take();
            logger.info("Response received: " + response);
        } catch(InterruptedException e){
            logger.error("Reading response error " + e);
            throw new MyException("Reading response error " + e);
        }
        return response;
    }
    private void initializeConnection() throws MyException{
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
            logger.info("Connection initialized");
        } catch(IOException e){
            logger.error("Error connecting to server " + e);
            throw new MyException("Error connecting to server " + e);
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.START_GAME ||
                response.type() == ResponseType.SEND_NUMBER;
    }

    public void logout(User user, IObserver client) throws MyException{
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR){
            logger.error("Error logging out" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else{
            logger.info("Logged out");
        }
    }
    public List<User> getActiveUsers() throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_ACTIVE_USERS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            logger.error("Error getting participants" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else{
            logger.info("Got participants");
        }
        return (List<User>) response.data();
    }

    @Override
    public User getPlayer(int id) throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_PLAYER).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error getting player" +  response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            return  (User) response.data();
        }
    }

    @Override
    public Game getGame() throws MyException {
       throw new RuntimeException("getGame not implemented in ServiceRPCProxy");
    }

    @Override
    public void gameFinished(Game game) throws MyException {
        Request req = new Request.Builder().type(RequestType.GAME_FINISHED).data(game).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error finishing game" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        }
    }

    @Override
    public void sendNumber(int number) throws MyException {
        Request req = new Request.Builder().type(RequestType.SEND_NUMBER).data(number).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error sending number " + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        }
    }

    @Override
    public void saveGameState(GameState state) throws MyException {
        Request req = new Request.Builder().type(RequestType.SAVE_STATE).data(state).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            logger.error("Error saving game state" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        }
    }

    @Override
    public Boolean addPlayer(User player, IObserver gameObserver) throws MyException {
        this.playerObserver = gameObserver;
        Request req = new Request.Builder().type(RequestType.ADD_PLAYER).data(player).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error getting adding player" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else{
            //this.playerObserver = gameObserver;
            return (Boolean) response.data();
        }
    }

    @Override
    public List<User> getAllUsers() throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_USERS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            logger.error("Error getting participants" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else{
            logger.info("Got participants");
        }
        return (List<User>) response.data();
    }

    @Override
    public User login(User user, IObserver userObserver) throws MyException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            this.userObserver = userObserver;
            logger.info("Logged in " + user.toString());
            return (User) response.data();
        }
        if(response.type() == ResponseType.ERROR){
            logger.error("Error logging in" + response.data().toString());
            String err = response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
        return null;
    }

    private void handleUpdate(Response response){
        if(response.type() == ResponseType.START_GAME){
            playerObserver.refreshAll((Game) response.data());
        } else if (response.type() == ResponseType.SEND_NUMBER) {
            playerObserver.playerUpdate((int) response.data());
        }
    }


    private class ReaderThread implements Runnable{
        public void run(){
            while(!finished){
                try{
                    Object response = input.readObject();
                    logger.info("response received " + response);
                    if(isUpdate((Response) response)){
                        handleUpdate((Response) response);
                    } else{
                        try{
                            qresponses.put((Response) response);
                        } catch(InterruptedException e){
                            logger.error("Queue putting response error: " + e);
                        }
                    }
                } catch(IOException | ClassNotFoundException e){
                    if(e instanceof SocketException)
                        logger.info("Socket closed: " + e);
                    else
                        logger.error("Reading error: " + e);
                }
            }
        }
    }

}
