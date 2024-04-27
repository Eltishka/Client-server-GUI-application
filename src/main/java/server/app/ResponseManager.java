package server.app;

import dataexchange.RequestWithSender;
import dataexchange.ResponseWithReceiver;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@AllArgsConstructor
public class ResponseManager implements Runnable{
    private final ExecutorService responseSendPool;
    private final ConcurrentLinkedDeque<ResponseWithReceiver> responseQueue;


    @SneakyThrows
    public void run(){
        while(true){
            ResponseWithReceiver responseWithReceiver = responseQueue.poll();
            if(responseWithReceiver != null){
                responseSendPool.execute(new ResponseSender(responseWithReceiver.receiver, responseWithReceiver.response));
            }
        }
    }
}
