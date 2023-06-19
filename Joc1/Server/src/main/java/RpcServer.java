import networking.utils.AbstractServer;
import networking.utils.RpcConcurrentServer;
import networking.utils.ServerException;
import repository.*;
import server.ServicesImplementation;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class RpcServer{
    private static int defaultPort = 55555;

    public static void main(String[] args){

        Properties serverProps = new Properties();
        try{
            serverProps.load(RpcServer.class
                    .getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch(IOException e){
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        UserRepository userRepo = new UserDBRepository(serverProps);
        GameStateRepository gameStateRepo = new GameStateORMRepository();
        //UserRepository userRepo = new UserORMRepository();
        GameRepository gameRepo = new GameDBRepository(serverProps);
        System.out.println(userRepo.findByName("test"));
        IService serviceImplementation = new ServicesImplementation(userRepo, gameStateRepo, gameRepo);

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch(NumberFormatException nef){
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, serviceImplementation);
        try{
            server.start();
        } catch(ServerException e){
            System.err.println("Error starting the server" + e.getMessage());
        } finally{
            try{
                server.stop();
            } catch(ServerException e){
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}