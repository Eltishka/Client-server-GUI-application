package server.app;

import dataexchange.*;
import lombok.AllArgsConstructor;
import server.app.authorization.AuthorizedClient;
import server.app.authorization.UserPermission;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.RecursiveAction;

@AllArgsConstructor
public class ExecuteRequestTask extends RecursiveAction {

    private final RequestWithSender requestWithSender;
    private final ConcurrentLinkedDeque<ResponseWithReceiver> responseQueue;


    @Override
    protected void compute() {
        Request request = requestWithSender.request;
        AuthorizedClient client = requestWithSender.sender;
        RequestWithPermission requestWithPermission = new RequestWithPermission(request, UserPermission.DefaultUser);
        Response response = client.getCommandExecuter().executeCommand(requestWithPermission);
        responseQueue.add(new ResponseWithReceiver(client.getSocket(), response.getResponse()));
    }
}
