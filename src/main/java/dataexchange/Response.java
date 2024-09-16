package dataexchange;

import lombok.Getter;
import lombok.Setter;
import server.Invoker;

import java.io.Serial;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Класс ответа команды
 * @author Piromant
 */
public class Response implements Serializable {


    public final static long serialVersionUID = -8481424740265021668L;
    private final Object[] response;
    @Getter
    @Setter
    private Integer responseCode = -1;

    public Response(Object... response) {
        this.response = response;
    }

    public Response(Response copy){
        this.response = copy.getResponse();
        this.responseCode = copy.getResponseCode();
    }

    public Object[] getResponse(){
        return this.response;
    }

    @Override
    public String toString(){
        String s = "";
        for(Object i: response){
            s += i.toString() + "\n";
        }
        return s;
    }
}
