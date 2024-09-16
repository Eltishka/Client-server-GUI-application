package gui;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorizationException extends Exception{
    public AuthorizationException(String msg, Exception cause){
        super(msg, cause);
    }

    public AuthorizationException(String msg){
        super(msg);
    }
}
