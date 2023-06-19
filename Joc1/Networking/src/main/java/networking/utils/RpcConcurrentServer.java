package networking.utils;

import networking.rpcprotocol.ClientRpcReflectionWorker;
import networking.utils.AbsConcurrentServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.IService;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private final IService server;
    private static final Logger logger = LogManager.getLogger();

    public RpcConcurrentServer(int port, IService server){
        super(port);
        this.server = server;
        logger.info("RpcConcurrentServer created");
    }

    @Override
    protected Thread createWorker(Socket client){
        ClientRpcReflectionWorker worker = new ClientRpcReflectionWorker(server, client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        logger.info("Stopping services ...");
    }
}