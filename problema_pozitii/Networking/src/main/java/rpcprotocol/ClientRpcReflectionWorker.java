package rpcprotocol;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


public class ClientRpcReflectionWorker implements Runnable, IObserver {
    private final IService service;
    private final Socket connection;

    private static final Logger logger= LogManager.getLogger();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientRpcReflectionWorker(IService service, Socket connection) {
        logger.info("Creating worker");
        this.service = service;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
            logger.info("Worker created");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error in worker (reading): "+e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Error in worker (sleeping): "+e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error in worker (closing connection): "+e);
        }
    }

    private static final Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        logger.traceEntry("method entered: "+handlerName+" with parameters "+request);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            logger.info("Method invoked: "+handlerName+" with response "+response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            logger.error("Error in worker (invoking method handleRequest): "+e);
        }
        return response;
    }

    private Response handleLOGIN(Request request){
        logger.traceEntry("method entered: handleLOGIN with parameters "+request);
        User employee = (User) request.data();
        try {
            User foundEmployee = service.login(employee, this);
            logger.info("Employee logged in");
            return new Response.Builder().type(ResponseType.OK).data(foundEmployee).build();
        } catch (ServicesException e) {
            connected=false;
            logger.error("Error in worker (solving method handleLOGIN): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleLOGOUT(Request request){
        logger.traceEntry("method entered: handleLOGOUT with parameters "+request);
        User user=(User) request.data();
        try {
            service.logout(user, this);
            connected=false;
            logger.info("User logged out");
            return okResponse;
        } catch (ServicesException e) {
            logger.error("Error in worker (solving method handleLOGOUT): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleSTART_GAME(Request request){
        logger.traceEntry("method entered: handleSTART_GAME with parameters "+request);
        User user = (User) request.data();
        try {
            service.startGame(user, this);
            logger.info("Game started");
            return okResponse;
        } catch (ServicesException e) {
            connected=false;
            logger.error("Error in worker (solving method handleSTART_GAME): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException{
        logger.traceEntry("method entered: sendResponse with parameters "+response);
        output.writeObject(response);
        output.flush();
        logger.info("Response sent");
    }



    @Override
    public void updateGameStarted(List<User> users, List<Integer> positions) throws ServicesException {

    }

    @Override
    public void updateGameRound(User user, int diceNumber, int position, int money) throws ServicesException {

    }
}
