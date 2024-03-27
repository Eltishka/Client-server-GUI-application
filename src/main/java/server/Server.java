package server;
import dataexchange.Request;
import dataexchange.Response;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private ServerSocket serverSocket;
    private CommandExecuter commandExecuter;
    private Socket client;
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Server.class);

    public Server(int port){
        this.commandExecuter = CommandExecuter.getAccess();
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("Сервер запущен на порту {}", port);
        } catch(BindException e){
            System.out.println("Порт занят. Выберете другой порт");
            logger.error("Ошибка при создании серверного сокета", e);
            System.exit(0);
        } catch (IOException e){
            logger.error("Ошибка при создании серверного сокета", e);
        }

    }

    @SneakyThrows
    private void catchClient(){
        this.client = serverSocket.accept();
        logger.info("Подключился клиент по адрессу {}", this.client.getInetAddress());
    }

    private void sendResponse(Response response) throws IOException {
        BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(writer);
        outputStream.writeObject(response);
        outputStream.flush();
    }

    private Request receiveRequest() throws IOException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
            return (Request) inputStream.readObject();
        } catch (ClassNotFoundException e){
            logger.error("Не сущетсвующий класс был передан клиентом", e);
            return null;
        }
    }

    @SneakyThrows
    public void run(){
        logger.info("Сервер запущен");
        while (true) {
            try {
                this.catchClient();
                this.sendResponse(new Response(Invoker.getAccess().getCommandMapClone()));
                while (true) {
                    Request request = this.receiveRequest();
                    Response response = this.commandExecuter.executeCommand(request);
                    this.sendResponse(response);
                    System.out.println(request.command_name);
                }
            } catch(SocketException e){
                logger.warn("Соединение разорвано", e);
                this.client.close();
            } catch (Exception e){
                this.commandExecuter.externalSave();
                logger.error("Непредвиденная ошибка", e);
            }
        }
    }

}
