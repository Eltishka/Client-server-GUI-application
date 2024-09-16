package client;

import dataexchange.Request;
import dataexchange.Response;

import java.io.IOException;
import java.net.SocketException;

public class SafetyClientBridgeImpl implements SaffetyClientBridge{

    Client client;
    Terminal terminal;
    public SafetyClientBridgeImpl(Terminal terminal, Client client){
        this.client = client;
        this.terminal = terminal;
    }

    @Override
    public void sendRequestSafety(Request request){
        try {
            this.client.sendRequest(request);
        }
        catch (SocketException e){
            System.out.println("Ошибка: разорвано подключение с сервером");
            System.out.println("Попытка переподключения...");
            terminal.start();
            this.sendRequestSafety(request);
        } catch (IOException e) {
            System.out.println("Не удается отправить запрос серверу, проверьте адрес и порт...");
        }
    }

    @Override
    public Response receiveResponseSafety(){
        Response response = new Response();
        try{
            response = this.client.receiveResponse();
        }
        catch (SocketException e){
            System.out.println("Ошибка: разорвано подключение с сервером");
            System.out.println("Попытка переподключения...");
            terminal.start();
            System.out.println("Переподключение выполнено. Пожалуйста, повторите ввод:");
        } catch (IOException e) {
            System.out.println("Не удается получить ответ сервера...");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Сервер передает не определенный объект. Скорее всего версии серврера и клиента отличаются");;
        } catch (Exception e) {
            System.exit(0);
        }
        return response;
    }
}
