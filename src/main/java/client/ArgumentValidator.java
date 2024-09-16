package client;

import objectspace.Vehicle;

import java.util.ArrayList;

public class ArgumentValidator {
    public static boolean checkId(String[] commandToCheck){
        if(commandToCheck.length == 1){
            System.out.println("id должен быть введен через пробел после команды");
            return false;
        }
        else if(!commandToCheck[1].matches("[1-9]\\d*\\s*")) {
            System.out.println("id должен быть целым неотрицательным числом");
            return false;
        }
        try {
            Integer.parseInt(commandToCheck[1]);
        } catch (Exception e) {
            System.out.println("Слишком большое значение id, невозможно  выполнить команду");
            return false;
        }
        return true;
    }

    public static boolean checkName(String arg){
        return !arg.matches("\\s*");
    }

    public static boolean checkCoordinates(String arg){
        try {
            Double x = Double.parseDouble(arg.split(" ")[0]);
            Long y = Long.parseLong(arg.split(" ")[1]);
            if(arg.split(" ").length > 2)
                throw new Exception();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkCoordX(String arg){
        try {
            Double x = Double.parseDouble(arg);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean checkCoordY(String arg){
        try {
            Long.parseLong(arg);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean checkEnginePower(String arg){
        try {
            Long power = Long.parseLong(arg);
            if(power <= 0)
                throw new Exception();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkFullObject(ArrayList<String> arg){
        System.out.println(arg);
        try {
            Vehicle.parseVehicle(arg.toArray(String[]::new));
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
