package client;

import dataexchange.Request;
import dataexchange.Response;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private final String server_address;
    private final int server_port;
    private SocketChannel socketChannel;

    public Client(String server_address, int server_port){
        this.server_address = server_address;
        this.server_port = server_port;
    }
    public Response start(){
        try {
            this.socketChannel = SocketChannel.open();
            this.socketChannel.connect(new InetSocketAddress(server_address, server_port));
            this.socketChannel.configureBlocking(false);
            System.out.println("Подключение... Пожалуйста, подождите");
            while (!this.socketChannel.finishConnect()){}
            System.out.println("Подключение завершено");
            return receiveResponse();
        } catch (ConnectException e){
            System.out.println("Ошибка подключения: Сервер недоступен");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendRequest(Request request) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(120000);
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            outputStream.writeObject(request);
            outputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(data);

            while (buffer.hasRemaining()) {
                this.socketChannel.write(buffer);
            }

        } catch (SocketException e){
            System.out.println("Ошибка: разорвано подключение с сервером");
            System.out.println("Попытка переподключения...");

            this.start();
            this.sendRequest(request);
        } catch (IOException e) {
            System.out.println("Не удается отправить запрос серверу, проверьте адрес и порт...");
        }
    }

    public Response receiveResponse(){
        Response response = new Response();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(10486);
            while (this.socketChannel.read(buffer) == 0){}
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            response = (Response)objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            buffer.clear();
        } catch (SocketException e){
            System.out.println("Ошибка: разорвано подключение с сервером");
            System.out.println("Попытка переподключения...");
            this.start();
            System.out.println("Переподключение выполнено. Пожалуйста, повторите ввод:");
        } catch (IOException e) {
            System.out.println("Не удается получить ответ сервера...");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Сервер передает не определенный объект. Скорее всего версии серврера и клиента отличаются");;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
