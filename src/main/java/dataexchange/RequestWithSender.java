package dataexchange;

import server.app.authorization.AuthorizedClient;

import java.util.ArrayList;

public class RequestWithSender {
    public final AuthorizedClient sender;
    public final Request request;


    public RequestWithSender(Request request, AuthorizedClient sender) {
        this.sender = sender;
        this.request = request;
    }
}
