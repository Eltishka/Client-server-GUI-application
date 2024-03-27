package client;

import objectspace.FuelType;
import objectspace.VehicleType;
import objectspace.exceptions.VehicleException;
import сommands.CommandHashMap;
import dataexchange.Request;
import сommands.CommandUsingElement;
import сommands.CommandWithId;

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

    private Client client;

    private CommandHashMap commandMap;
    /**
     * Конструктор, получающий доступ к исполнителю команд
     */
    public Terminal(String server_address, int server_port){
        this.sc = new Scanner(System.in);
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
                Request request = new Request(command, element, true);
                this.client.sendRequest(request);
            }
            catch (VehicleException e) {
                System.out.println(e.getMessage() + " " + e.getCause().getMessage());
            }
            catch (NoSuchElementException e){
                sc.close();
                System.out.println("Программа завершена");
                Request request = new Request("exit", element, true);
                this.client.sendRequest(request);
            }
            System.out.println(this.client.receiveResponse());
        }
    }


    /**
     * Метод запуска
     */
    public void start(){
        this.commandMap = (CommandHashMap) this.client.start().getResponse()[0];
        System.out.println("Здравсвтуйте, для получения справки по командам введите help");
        run();
    }


}
