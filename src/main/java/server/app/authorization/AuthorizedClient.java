package server.app.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.CommandExecuter;

import java.net.Socket;

@AllArgsConstructor
@Getter
public class AuthorizedClient {
    private final Socket socket;
    private final CommandExecuter commandExecuter;
    private final String userName;
}
