package сommands.authorizationscommands;

import dataexchange.Response;
import server.app.ResponseSender;
import server.app.authorization.UserPermission;
import server.database.UserStorageManager;

import java.sql.SQLException;

public class Register extends AuthorizationCommand {
    public Register(UserStorageManager storage, String userName, String password, UserPermission permission) {
        super(storage, userName, password, permission);
    }

    @Override
    public Response execute() {
        if (!this.permission.equals(UserPermission.System))
            return new Response("Вы уже залогинины! Для регистрации нового пользователя перезапустите приложение");
        boolean authorized = false;
        authorized = this.storage.register(userName, password);
        if (!authorized && this.storage.checkUserExisted(userName))
            return new Response("Пользователь с таким именем уже существует!");
        else if (!authorized)
            return new Response("Невозможно зарегистрироваться...");


        return new Response("true");
    }

    @Override
    public String getHelp() {
        return null;
    }
}