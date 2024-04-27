package server.app.authorization;

import dataexchange.Request;
import dataexchange.RequestWithPermission;
import dataexchange.Response;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import server.CommandExecuter;
import server.app.RequestReceiver;
import server.app.ResponseSender;
import server.app.Server;
import server.app.authorization.AuthorizedClient;
import server.database.UserStorageManagerImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

public class Authorizer implements Runnable{
    private final Socket client;
    private final CommandExecuter commandExecuter;
    private final ExecutorService requestReceivePool;
    private final ConcurrentLinkedDeque requestQueue;
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);


    public Authorizer(Socket client, ExecutorService requestReceivePool,
                      ConcurrentLinkedDeque requestQueue){
        this.client = client;
        this.requestReceivePool = requestReceivePool;
        this.requestQueue = requestQueue;
        this.commandExecuter = new CommandExecuter();
    }

    @SneakyThrows
    @Override
    public void run(){
        Response response = new Response();
        boolean authorized = false;
        String userName = "";
        while (true){
            try {
                ObjectInputStream inputStream = new ObjectInputStream(this.client.getInputStream());
                Request request = (Request)inputStream.readObject();
                this.commandExecuter.setUserName(request.userName);
                RequestWithPermission requestWithPermission = new RequestWithPermission(request, UserPermission.System);
                response = this.commandExecuter.executeCommand(requestWithPermission);
                if(Boolean.parseBoolean((String) response.getResponse()[0])){
                    userName = request.userName;
                    authorized = true;
                    break;
                }
                new ResponseSender(client, response).run();
            } catch (ClassNotFoundException e) {
                logger.error("Не сущетсвующий класс был передан клиентом", e);
                this.client.close();
                break;
            } catch (IOException e) {
                logger.error("Ошибка ввода/вывода", e);
                this.client.close();
                break;
            }
        }

        if(authorized){
            new ResponseSender(client, response).run();
            AuthorizedClient authorizedClient = new AuthorizedClient(client, commandExecuter, userName);
            this.requestReceivePool.execute(new RequestReceiver(authorizedClient, this.requestQueue));

        }
    }
}
