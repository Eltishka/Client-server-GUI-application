package сommands.authorizationscommands;

import dataexchange.Response;
import lombok.AllArgsConstructor;
import server.app.authorization.UserPermission;
import server.database.UserStorageManager;
import сommands.Command;

@AllArgsConstructor
public abstract class AuthorizationCommand implements Command {
    protected final UserStorageManager storage;
    protected final String userName;
    protected final String password;
    protected final UserPermission permission;

    public abstract Response execute();
    public abstract String getHelp();
}
