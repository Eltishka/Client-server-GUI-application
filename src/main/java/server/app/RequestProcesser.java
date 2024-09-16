package server.app;

import dataexchange.Request;
import dataexchange.RequestWithSender;
import dataexchange.Response;
import dataexchange.ResponseWithReceiver;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import server.CommandExecuter;
import server.utilities.Pair;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RequestProcesser implements Runnable{
    private ForkJoinPool pool;
    private ConcurrentLinkedDeque<ResponseWithReceiver> responseQueue;
    private ConcurrentLinkedDeque<RequestWithSender> requestQueue;

    public RequestProcesser(ConcurrentLinkedDeque<RequestWithSender> requestQueue,
                            ConcurrentLinkedDeque<ResponseWithReceiver> responseQueue){
        this.pool = new ForkJoinPool();
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }


    public void run(){
        while(true){
            RequestWithSender requestWithSender = requestQueue.poll();
            if(requestWithSender != null){
                pool.invoke(new ExecuteRequestTask(requestWithSender, responseQueue));
            }
        }
    }

}

