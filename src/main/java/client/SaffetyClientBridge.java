package client;

import dataexchange.Request;
import dataexchange.Response;

import java.io.IOException;
import java.net.SocketException;

public interface SaffetyClientBridge {
    void sendRequestSafety(Request request);

    Response receiveResponseSafety();
}
