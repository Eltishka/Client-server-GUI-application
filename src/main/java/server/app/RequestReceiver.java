package server.app;

import dataexchange.Request;
import dataexchange.RequestWithSender;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import server.app.authorization.AuthorizedClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RequestReceiver implements Runnable{
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);
    private final AuthorizedClient client;
    private final ConcurrentLinkedDeque<RequestWithSender> requestsQueue;

    public RequestReceiver(AuthorizedClient client, ConcurrentLinkedDeque<RequestWithSender> requestsQueue){
        this.client = client;
        this.requestsQueue = requestsQueue;

    }
    @SneakyThrows
    public void run() {
        while(this.client.getSocket().isConnected() && !this.client.getSocket().isClosed()) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(this.client.getSocket().getInputStream());
                requestsQueue.add(new RequestWithSender((Request) inputStream.readObject(), client));
            } catch (ClassNotFoundException e) {
                logger.error("Не сущетсвующий класс был передан клиентом", e);
                this.client.getSocket().close();
            } catch(SocketException e){
                logger.info("Клиент откючился", this.client.getSocket().getInetAddress());
            } catch(IOException e) {
                logger.error("Ошибка ввода/вывода", e);
                this.client.getSocket().close();

            }
        }
    }
}
