package server.app;

import dataexchange.Request;
import dataexchange.Response;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ResponseSender implements Runnable{
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);
    private final Socket client;
    private final Response response;

    public ResponseSender(Socket client, Response response){
        this.client = client;
        this.response = response;

    }
    @SneakyThrows
    public void run() {
        try {
            BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(writer);

            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Ошибка ввода/вывода", e);
            if(!this.client.isClosed())
                this.client.close();
            throw e;
        }
    }
}
