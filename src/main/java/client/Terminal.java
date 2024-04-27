package client;

import dataexchange.Response;
import hashing.Hasher;
import objectspace.FuelType;
import objectspace.VehicleType;
import objectspace.exceptions.VehicleException;
import сommands.CommandHashMap;
import dataexchange.Request;
import сommands.CommandUsingElement;
import сommands.CommandWithId;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
/**
 *
 * Класс для работы с пользователем, принимания команд в текстовом виде и передачи их в исполнитель команд
 * @author Piromant
 */
public class Terminal implements Runnable{
    /**
     * Сканер для считывания введенных данных
     */
    private Scanner sc;
    private String userName;
    private Client client;

    private CommandHashMap commandMap;
    /**
     * Конструктор, получающий доступ к исполнителю команд
     */
    public Terminal(String server_address, int server_port){
        this.sc = new Scanner(System.in);
        System.out.println(server_address);
        this.client = new Client(server_address, server_port);


    }

    public boolean checkIfNeedElement(String commandName){
        return CommandUsingElement.class.isAssignableFrom(this.commandMap.get(commandName));
    }

    public boolean checkIfNeedId(String commandName){
        return CommandWithId.class.isAssignableFrom(this.commandMap.get(commandName));
    }

    /**
     *
     * @param msg Сообщение, которое говорит пользователю что и как вводить
     * @param checker Чекер для проверки аргументов на валидность
     * @see ArgumentChecker
     * @return Строка - валидный аргумент
     */
    private String getArgumentWithRules(String msg, ArgumentChecker<String> checker){
        String arg = "";
        System.out.println(msg);
        arg = this.sc.nextLine();
        while (!checker.check(arg)){
            System.out.println("Неверный формат ввода. Попробуйте еще раз.");
            System.out.println(msg);
            arg = this.sc.nextLine();
        }
        return arg;
    }

    /**
     * Метод, читающий информацию для элемента коллекции
     * @return Массив строк - аргументов для создания нового элемента
     */
    private ArrayList<String> readElement(){
        ArrayList<String> args = new ArrayList<String>();

        args.add(getArgumentWithRules("Введите имя (непустая строка)",
                                            arg -> !arg.matches("\\s*")));

        args.add(getArgumentWithRules("Введите координаты в формате: x y (x - число с дробной частью типа double, оба больше нуля, y - целое формата long)",
                                            arg -> ArgumentValidator.checkCoordinates(arg)));

        args.add(getArgumentWithRules("Введите силу двигателя (неотрицательное целое число больше нуля и мень 2^63):",
                                            arg -> ArgumentValidator.checkEnginePower(arg)));

        List<VehicleType> possibleTypes = Arrays.asList(VehicleType.values());
        ArrayList<String> possibleTypesStr = new ArrayList<>();
        Iterator<VehicleType> it = possibleTypes.iterator();
        while(it.hasNext()){
            possibleTypesStr.add(it.next().toString());
        }
        args.add(getArgumentWithRules("Введите типа средства передвижения из представленных" + possibleTypesStr.toString() + ":",
                                            arg -> possibleTypesStr.contains(arg.trim())));

        List<FuelType> possibleFuelTypes = Arrays.asList(FuelType.values());
        ArrayList<String> possibleFuelTypesStr = new ArrayList<>();
        Iterator<FuelType> fuelIt = possibleFuelTypes.iterator();
        while(fuelIt.hasNext()){
            possibleFuelTypesStr.add(fuelIt.next().toString());
        }
        args.add(getArgumentWithRules("Введите типа топлива из представленных" + possibleFuelTypesStr.toString() + ":",
                                            arg -> possibleFuelTypesStr.contains(arg.trim())));

        return args;
    }

    /**
     * Проверяет на валидность аргумент команды, если тот должен быть id
     * @param commandToCheck команда, аргумент которой нужно проверить
     * @return true, если id валиден, false в ином случае
     */

    /**
     * Основной метод для работы с пользователем и чтения введенных команд
     */

    public void run() {
        String command = "";
        while(!command.equals("exit")) {
            ArrayList<String> element = new ArrayList<>();
            try {
                command = sc.nextLine();
                if(command.equals(""))
                    continue;
                String[] commandToCheck = command.split(" ");
                if(this.checkIfNeedId(commandToCheck[0])){
                    if(!ArgumentValidator.checkId(commandToCheck))
                        continue;
                }
                if (this.checkIfNeedElement(commandToCheck[0]))
                    element = this.readElement();
                Request request = new Request(command, element, userName, true);
                this.sendRequestSafety(request);
            }
            catch (VehicleException e) {
                System.out.println(e.getMessage() + " " + e.getCause().getMessage());
            }
            catch (NoSuchElementException e){
                sc.close();
                System.out.println("Программа завершена");
                Request request = new Request("exit", element, userName, true);
                this.sendRequestSafety(request);
            }
            System.out.println(this.receiveResponseSafety());
        }
    }


    /**
     * Метод запуска
     */
    public void start(){

        try {
            this.commandMap = (CommandHashMap) this.client.start().getResponse()[0];
        } catch (ClassNotFoundException e) {
            System.out.println("Сервер передает не определенный объект. Скорее всего версии серврера и клиента отличаются");;
            System.exit(0);
        }
        this.auth();
        System.out.println("Здравсвтуйте " + userName + ", для получения справки по командам введите help");
        run();
    }


    private void auth(){
        Response response = new Response();
        while (true) {

            System.out.println("Необходима авторизация: авторизуйтесь при помощи команды login");
            System.out.println("Или зарегистрируйтесь при помощи команды register");
            String commandName = sc.nextLine();
            if(commandName.equals("login")){
                System.out.print("Введите логин: ");
                String userName = sc.nextLine();
                System.out.print("Введите пароль: ");
                String password = Hasher.hashPasswordWith512(sc.nextLine());
                Request request = new Request("login" + " " + password, new ArrayList<>(), userName, true);
                this.sendRequestSafety(request);
                response = this.receiveResponseSafety();


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
                this.sendRequestSafety(request);
                response = this.receiveResponseSafety();
                if(Boolean.parseBoolean((String)response.getResponse()[0])) {
                    this.userName = userName;
                    break;
                }

            }
            System.out.println(response);
        }
    }

    private void sendRequestSafety(Request request){
        try {
            this.client.sendRequest(request);
        }
        catch (SocketException e){
            System.out.println("Ошибка: разорвано подключение с сервером");
            System.out.println("Попытка переподключения...");
            this.start();
            this.sendRequestSafety(request);
        } catch (IOException e) {
            System.out.println("Не удается отправить запрос серверу, проверьте адрес и порт...");
        }
    }

    private Response receiveResponseSafety(){
        Response response = new Response();
        try{
            response = this.client.receiveResponse();
        }
        catch (SocketException e){
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
