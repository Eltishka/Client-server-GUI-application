package dataexchange;

import java.net.Socket;

public class ResponseWithReceiver{
    public final Socket receiver;
    public final Response response;

    public ResponseWithReceiver(Socket receiver, Object... response){
        this.response = new Response(response);
        this.receiver = receiver;
    }
}
