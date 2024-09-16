package server.database;

import lombok.SneakyThrows;
import objectspace.Coordinates;
import objectspace.FuelType;
import objectspace.Vehicle;
import objectspace.VehicleType;
import org.slf4j.LoggerFactory;
import server.app.Server;

import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;

public class UserStorageManagerImpl implements UserStorageManager{
    private final Connection connection;
    private String tableName;
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);


    public UserStorageManagerImpl(String url, String user, String password, String tableName) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        this.tableName = tableName;
    }

    @Override
    @SneakyThrows
    public boolean register(String userName, String password)  {
        if(this.checkUserExisted(userName))
            return false;
        PreparedStatement statement = null;
        try {
            synchronized (this.connection) {
                statement = connection.prepareStatement("INSERT INTO Users(user_name, password) VALUES (?, ?);");
                statement.setString(1, userName);
                statement.setString(2, password);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }
    }

    @Override
    @SneakyThrows
    public boolean checkPassword(String userName, String password) {
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            synchronized (this.connection) {
                statement = connection.prepareStatement("SELECT * FROM Users WHERE user_name = ?");
                statement.setString(1, userName);
                resultSet = statement.executeQuery();
            }
            if(resultSet.next())
                return password.equals(resultSet.getString("password"));
            return false;
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }

    }

    @Override
    @SneakyThrows
    public boolean checkUserExisted(String userName) {
        PreparedStatement statement = null;
        try {
            synchronized (this.connection) {
                statement = connection.prepareStatement("SELECT * FROM Users WHERE user_name = ?");
                statement.setString(1, userName);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }

    }
}
