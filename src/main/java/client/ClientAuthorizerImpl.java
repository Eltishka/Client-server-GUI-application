package client;

import dataexchange.Request;
import dataexchange.Response;
import hashing.Hasher;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientAuthorizerImpl implements ClientAuthorizer{
    private String userName;
    private Scanner sc;
    private SaffetyClientBridge bridge;

    public ClientAuthorizerImpl(Scanner sc, SaffetyClientBridge bridge){
        this.sc = sc;
        this.bridge = bridge;
    }

    @Override
    public String auth(){
        Response response = new Response();
        while (true) {

            System.out.println("Необходима авторизация: авторизуйтесь при помощи команды login");
            System.out.println("Или зарегистрируйтесь при помощи команды registerCommand");
            String commandName = sc.nextLine();
            if(commandName.equals("login")){
                System.out.print("Введите логин: ");
                String userName = sc.nextLine();
                System.out.print("Введите пароль: ");
                String password = Hasher.hashPasswordWith512(sc.nextLine());
                Request request = new Request("login" + " " + password, new ArrayList<>(), userName, true);
                bridge.sendRequestSafety(request);
                response = bridge.receiveResponseSafety();


                if(Boolean.parseBoolean((String)response.getResponse()[0])){
                    this.userName = userName;
                    break;
                }

            }
            else if(commandName.equals("register")){
                System.out.print("Введите логин: ");
                String userName = sc.nextLine();
                System.out.print("Введите пароль: ");
                String password = Hasher.hashPasswordWith512(sc.nextLine());
                Request request = new Request("register" + " " + password, new ArrayList<>(), userName, true);
                bridge.sendRequestSafety(request);
                response = bridge.receiveResponseSafety();
                if(Boolean.parseBoolean((String)response.getResponse()[0])) {
                    this.userName = userName;
                    break;
                }

            }
            System.out.println(response);

        }
        return this.userName;
    }
}
