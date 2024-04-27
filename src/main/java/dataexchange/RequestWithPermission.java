package dataexchange;

import lombok.AllArgsConstructor;
import server.app.authorization.UserPermission;

@AllArgsConstructor
public class RequestWithPermission {
    public final Request request;
    public final UserPermission permission;

}
