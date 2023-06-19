package org.example;

import org.example.utils.AbstractServer;
import org.example.utils.RpcConcurrentServer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            System.out.println("It has been created in StartRpcServer");
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    public static void main(String[] args) {

        Properties serverProps = new Properties();
        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("/examServer.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find examServer.properties" + e);
            return;
        }

        initialize();
        UserRepository userRepository = new UserRepository(serverProps);
        GameRepository gameRepository = new GameRepository(sessionFactory);
//        ConfigurationRepository configurationRepository = new ConfigurationRepository(sessionFactory);
        IServices travelServerImpl = new ServicesImpl(userRepository, gameRepository);
        int travelServerPort = defaultPort;
        try{
            travelServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch(NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }

        System.out.println("Starting server on port: " + travelServerPort);
        AbstractServer server = new RpcConcurrentServer(travelServerPort, travelServerImpl);
        try{
            server.start();
        } catch (org.example.utils.ServerException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                server.stop();
               } catch (org.example.utils.ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
