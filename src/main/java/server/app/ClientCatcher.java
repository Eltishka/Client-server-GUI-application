package server.app;

import dataexchange.Response;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import server.Invoker;
import server.app.authorization.Authorizer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
public class ClientCatcher implements Runnable{

    private final ServerSocket serverSocket;
    private final ExecutorService requestReceivePool;
    private final ExecutorService responseSendPool;
    private final ConcurrentLinkedDeque requestQueue;
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);

    @SneakyThrows
    public void run(){
        while (!serverSocket.isClosed()) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                logger.error("Ошибка при подключении клиента", e);
                throw e;
            }
            logger.info("Подключился клиент по адрессу {}", client.getInetAddress());
            this.responseSendPool.execute(new ResponseSender(client, new Response(Invoker.getAccess().getCommandMapClone())));
            Thread authorizer = new Thread(new Authorizer(client, requestReceivePool, requestQueue));
            authorizer.start();
        }
    }
}
