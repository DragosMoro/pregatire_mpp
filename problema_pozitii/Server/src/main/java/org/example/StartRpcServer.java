package org.example;

import org.example.utils.AbstractServer;
import org.example.utils.RpcConcurrentServer;

import java.io.IOException;

import java.rmi.ServerException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }


        IGameTableRepositry gameTableRepository = new GameTableRepository(serverProps);
        IGameRoundRepository gameRoundRepository = new GameRoundRepository(serverProps);
        IUserRepository userRepository = new UserRepository(serverProps);
        IGameRepository gameRepository = new GameRepository(serverProps);
        IService serverImpl = new ServiceImpl(gameTableRepository, gameRoundRepository, userRepository, gameRepository);



        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new RpcConcurrentServer(chatServerPort, serverImpl);
        try {
            server.start();
        } catch (org.example.utils.ServerException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.stop();
            } catch (org.example.utils.ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}