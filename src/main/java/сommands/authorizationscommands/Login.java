package сommands.authorizationscommands;

import dataexchange.Response;
import server.app.authorization.UserPermission;
import server.database.UserStorageManager;

import java.sql.SQLException;


public class Login extends AuthorizationCommand{
    public Login(UserStorageManager storage, String userName, String password, UserPermission permission) {
        super(storage, userName, password, permission);
    }

    @Override
    public Response execute() {
        if(!this.permission.equals(UserPermission.System))
            return new Response("Вы уже залогинины!");
        boolean authorized = false;
        authorized = this.storage.checkPassword(this.userName, this.password);

        if (authorized)
            return new Response("true");

        return new Response("Неверное имя пользователя или пароль");

    }

    @Override
    public String getHelp() {
        return null;
    }
}
