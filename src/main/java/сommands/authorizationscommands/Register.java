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
        if (!authorized && this.storage.checkUserExisted(userName)) {
            Response response = new Response("Пользователь с таким именем уже существует!");
            response.setResponseCode(7);
            return response;
        }
        else if (!authorized) {
            Response response = new Response("Невозможно зарегистрироваться...");
            response.setResponseCode(8);
            return response;
        }


        return new Response("true");
    }

    @Override
    public String getHelp() {
        return null;
    }
}