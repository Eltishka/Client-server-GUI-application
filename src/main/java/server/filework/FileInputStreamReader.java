package server.filework;

import server.utilities.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * 
 * Класс, реализующий интерфейс FileReader, читает файл при помощи InputStreamReader
 * @see FileReader
 * @author Piromant
 */
public class FileInputStreamReader implements FileReader {
    InputStreamReader reader;

    public FileInputStreamReader() {}

    @Override
    public void close() throws IOException{
        if (reader != null) {
            reader.close();
        }
    }

    @Override
    public void openFile(String fileName) throws FileNotFoundException, NullPointerException, SecurityException{
        this.reader = new InputStreamReader(new FileInputStream(fileName));
    }
    private String readLine() throws IOException{
        String str = null;
        int ch = this.reader.read();
        str = (ch != -1) ? "" : null;

        while (ch != (int)'\r' && ch != -1) {

            if(ch != (int)'\n') {
                str += (char) ch;
            }
            else if(!str.equals("")) {
                break;
            }
            ch = this.reader.read();

        }
        return str;
    }

    /**
     * Метод, читающий аргументы для созадния элемента коллекции
     * @return Массив строк - аргументов
     * @throws IOException
     */
    private ArrayList<String> readArguments() throws IOException{
        ArrayList<String> args = new ArrayList<String>();
        for(int i = 0; i < 5; i++){
            args.add(this.readLine());
        }
        return args;
    }


    @Override
    public LinkedList<Pair<String, ArrayList<String>>> readCommandsFromFile(String fileName) throws NullPointerException, IOException,
                                                                                                    SecurityException {

        LinkedList<Pair<String, ArrayList<String>>> commandList = new LinkedList<Pair<String, ArrayList<String>>>();
        this.openFile(fileName);

        String command = "";
        ArrayList<String> argument;
        while (true) {
            command = this.readLine();
            argument = new ArrayList<String>();
            if(command == "exit" || command == null)
                break;
            if (command.equals("add") || command.contains("update"))
                argument = this.readArguments();
            commandList.add(new Pair(command, argument));
        }
        this.close();
        return commandList;

    }

    @Override
    public String readWholeFile(String fileName) throws IOException, NullPointerException {

        this.openFile(fileName);
        String res = "";
        String str = readLine();
        while (str != null){
            res = res.concat(str + '\n');
            str = readLine();
        }
        this.close();
        return res;
    }
}
