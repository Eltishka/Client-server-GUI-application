package server.app;
import dataexchange.Request;
import dataexchange.RequestWithSender;
import dataexchange.Response;
import dataexchange.ResponseWithReceiver;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import server.CommandExecuter;
import server.Invoker;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private Socket client;
    private final ConcurrentLinkedDeque<RequestWithSender> requestsQueue;
    private final ConcurrentLinkedDeque<ResponseWithReceiver> responseQueue;
    private ExecutorService requestReceivePool;
    private ExecutorService responseSendPool;
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Server.class);

    public Server(int port) throws IOException {

        this.requestsQueue = new ConcurrentLinkedDeque<>();
        this.responseQueue = new ConcurrentLinkedDeque<>();
        this.requestReceivePool = Executors.newCachedThreadPool();
        this.responseSendPool = Executors.newCachedThreadPool();
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("Сервер запущен на порту {}", port);
        } catch(BindException e){
            System.out.println("Порт занят. Выберете другой порт");
            logger.error("Ошибка при создании серверного сокета. Порт занят", e);
            this.serverSocket = new ServerSocket(2056);
            System.exit(0);
        } catch (IOException e){
            logger.error("Ошибка при создании серверного сокета", e);
        }

    }


    @SneakyThrows
    public void run(){
        logger.info("Сервер запущен");
        try {
            Thread clientCatcher = new Thread(new ClientCatcher(serverSocket, requestReceivePool, responseSendPool, requestsQueue));
            clientCatcher.start();
            Thread requestExecutor = new Thread(new RequestProcesser(requestsQueue, responseQueue));
            requestExecutor.start();
            Thread responseSender = new Thread(new ResponseManager(responseSendPool, responseQueue));
            responseSender.start();

        } catch (Exception e){
            logger.error("Непредвиденная ошибка", e);

        }

    }

}
