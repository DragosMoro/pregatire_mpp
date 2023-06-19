package networking.rpcprotocol;

import model.Game;
import model.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.IObserver;
import service.IService;
import service.MyException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class ClientRpcReflectionWorker implements Runnable, IObserver{

    private final IService service;
    private final Socket connection;

    private static final Logger logger = LogManager.getLogger();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IService service, Socket connection){
        logger.info("Creating worker");
        this.service = service;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
            logger.info("Worker created");
        } catch(IOException e){
            logger.error(e);
        }
    }

    @Override
    public void run(){
        while(connected){
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if(response != null){
                    sendResponse(response);
                }
            } catch(IOException | ClassNotFoundException e){
                logger.error("Error in worker (reading): " + e);
            }
            /*
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                logger.error("Error in worker (sleeping): " + e);
            }*/
        }
        try{
            input.close();
            output.close();
            connection.close();
        } catch(IOException e){
            logger.error("Error in worker (closing connection): " + e);
        }
    }

    private void sendResponse(Response response) throws IOException{
        logger.traceEntry("method entered: sendResponse with parameters " + response);
        output.writeObject(response);
        output.flush();
        logger.info("Response sent");
    }

    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response = null;
        String handlerName = "handle_" + (request).type();
        logger.traceEntry("method entered: " + handlerName + " with parameters " + request);
        try{
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            logger.info("Method invoked: " + handlerName + " with response " + response);
        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
            logger.error("Error in worker (invoking method handleRequest): " + e);
        }
        return response;
    }

    private Response handle_GET_ACTIVE_USERS(Request request) {
        logger.trace("method entered: handleGET_ACTIVE_USERS with parameters " + request);
        try{
            List<User> users = service.getActiveUsers();
            logger.info("Users found");
            return new Response.Builder().type(ResponseType.GET_ACTIVE_USERS).data(users).build();
        } catch(MyException e){
            logger.error("Error in worker (solving method handleGET_ACTIVE_USERS): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_SEND_NUMBER(Request request) {
        logger.trace("method entered: handleSEND_NUMBER with parameters " + request);
        int number = (int) request.data();
        try{
            service.sendNumber(number);
            logger.info("Number sent");
            return new Response.Builder().type(ResponseType.OK).build();
        } catch(MyException e){
            logger.error("Error in worker (solving method handleSEND_NUMBER): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handle_SAVE_STATE(Request request) {
        logger.trace("method entered: handleSAVE_STATE with parameters " + request);
        GameState state = (GameState) request.data();
        try {
            service.saveGameState(state);
            logger.info("State saved");
            return new Response.Builder().type(ResponseType.OK).build();
        } catch(MyException e) {
            logger.error("Error in worker (solving method handle_SAVE_STATE " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_GET_ALL_USERS(Request request) {
        logger.trace("method entered: handleGET_ALL_USERS with parameters " + request);
        try{
            List<User> users = service.getAllUsers();
            logger.info("Users found");
            return new Response.Builder().type(ResponseType.GET_ALL_USERS).data(users).build();
        } catch(MyException e){
            logger.error("Error in worker (solving method handleGET_ALL_USERS): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handle_LOGIN(Request request){
        logger.trace("method entered: handleLOGIN with parameters " + request);
        User user = (User) request.data();
        try{
            User foundUser = service.login(user, this);
            logger.info("User logged in");
            return new Response.Builder().type(ResponseType.OK).data(foundUser).build();
        } catch(MyException e){
            connected = false;
            logger.error("Error in worker (solving method handleLOGIN): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_LOGOUT(Request request) {
        logger.traceEntry("method entered: handleLOGOUT with parameters " + request);
        User user = (User) request.data();
        try{
            service.logout(user, null);
            connected = false;
            logger.info("Participant logged out");
            return okResponse;
        } catch(MyException e){
            connected = false;
            logger.error("Error in worker (solving method handleLOGOUT): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_GAME_FINISHED(Request request) {
        logger.traceEntry("method entered: handleGAME_FINISHED with parameters " + request);
        Game game = (Game) request.data();
        try {
            service.gameFinished(game);
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleGAME_FINISHED): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_ADD_PLAYER(Request request) {
        logger.traceEntry("method entered: handle_ADD_PLAYER with parameters " + request);
        User user = (User) request.data();
        try {
            Boolean res = service.addPlayer(user, this);
            return new Response.Builder().type(ResponseType.ADD_PLAYER).data(res).build();
        } catch (MyException e) {
            connected = false;
            logger.error("Error in worker (solving method handle_ADD_PLAYER): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_GET_PLAYER(Request request) {
        logger.traceEntry("method entered: handle_GET_PLAYER with parameters " + request);
        int id = (int) request.data();
        try {
            User user = service.getPlayer(id);
            return new Response.Builder().type(ResponseType.GET_PLAYER).data(user).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handle_GET_PLAYER): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void refreshAll(Game nullValue){
        Game game = null;
        try {
            game = service.getGame();
            System.out.println(game);
        } catch (MyException e) {
            e.printStackTrace();
        }
        Response resp= new Response.Builder().type(ResponseType.START_GAME).data(game).build();
        logger.info("RefreshAll invoked");
        try {
            sendResponse(resp);
            logger.info("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error in worker (sending response): "+e);
        }
    }

    @Override
    public void playerUpdate(int Number) {
        Response resp = new Response.Builder().type(ResponseType.SEND_NUMBER).data(Number).build();
        logger.info("PlayerUpdate invoked");
        try {
            sendResponse(resp);
            logger.info("Response sent");
        } catch (IOException e) {
           e.printStackTrace();
           logger.error("Error in worker (sending response): " + e);
        }
    }

}
