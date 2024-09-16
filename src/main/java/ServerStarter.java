import server.Config;
import server.Invoker;
import server.app.Server;
import сommands.*;
import сommands.authorizationscommands.Login;
import сommands.authorizationscommands.Register;

import java.io.IOException;
import java.util.Scanner;

public class ServerStarter {
    public static void main(String[] args) throws IOException {

        Invoker.register("add", Add.class);
        Invoker.register("add_if_max", AddIfMax.class);
        Invoker.register("add_if_min", AddIfMin.class);
        Invoker.register("clear", Clear.class);
        Invoker.register("average_of_engine_power", AverageOfEnginePower.class);
        Invoker.register("execute_script", ExecuteScript.class);
        Invoker.register("exit", Exit.class);
        Invoker.register("filter_contains_name", FilterContainsName.class);
        Invoker.register("help", Help.class);
        Invoker.register("history", History.class);
        Invoker.register("info", Info.class);
        Invoker.register("print_field_descending_engine_power", PrintFieldDescendingEnginePower.class);
        Invoker.register("remove_by_id", RemoveById.class);
        Invoker.register("show", Show.class);
        Invoker.register("update", Update.class);
        Invoker.register("login", Login.class);
        Invoker.register("register", Register.class);
        Scanner sc = new Scanner(System.in);
        //System.out.println("Введите абсолютный путь до конфига: ");
        String path = "C:\\Users\\Piromant\\Desktop\\Програ\\lab6\\src\\main\\resources\\config.txt";
        Config.createConfig(path);

        Server server = new Server(2027);
        server.run();


    }
}
