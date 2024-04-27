package server.database;

import lombok.NonNull;
import objectspace.Coordinates;
import objectspace.FuelType;
import objectspace.Vehicle;
import objectspace.VehicleType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import server.app.Server;
import server.utilities.VehicleOwnerPair;
import server.utilities.Pair;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseStorageManager implements VehicleStorageManager{
    private Set<VehicleOwnerPair<Vehicle, String>> storage;
    private final Connection connection;
    private final String tableName;
    private static DatabaseStorageManager db;
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);

    private DatabaseStorageManager(String url, String user, String password, String tableName) throws SQLException {
        this.storage = Collections.synchronizedSet(new LinkedHashSet<>());
        this.connection = DriverManager.getConnection(url, user, password);
        this.tableName = tableName;
        this.loadCollection();
    }

    private void loadCollection() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
        ResultSet resultSet = statement.executeQuery();
        this.storage.clear();
        while(resultSet.next()){
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Coordinates coords = new Coordinates(resultSet.getDouble("coord_x"), resultSet.getLong("coord_y"));
            Date creationDate = resultSet.getDate("creation_date");
            Long enginePower = resultSet.getLong("engine_power");
            VehicleType vehicleType = VehicleType.valueOf(resultSet.getString("vehicle_type"));
            FuelType fuelType = FuelType.valueOf(resultSet.getString("fuel_type"));
            String owner = resultSet.getString("owner");
            this.storage.add(new VehicleOwnerPair<>(new Vehicle(id, name, coords, creationDate, enginePower, vehicleType, fuelType), owner));
        }
    }

    public static DatabaseStorageManager getAccess(String url, String user, String password, String tableName) throws SQLException{
        if(db == null)
            db = new DatabaseStorageManager(url, user, password, tableName);
        return db;
    }

    @Override
    public synchronized boolean add(Vehicle el, String owner) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO " + tableName + " (name, coord_x, coord_y, creation_date, engine_power, vehicle_type, fuel_type, owner) values (?, ?, ?, ?, ?, ?, ?, ?);");
            fillStatementWithEl(el, owner, statement);
            statement.executeUpdate();

            statement = this.connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();
            Integer id;
            if (resultSet.next())
                id = resultSet.getInt("id");
            else
                return false;
            el.setId(id);
            return this.storage.add(new VehicleOwnerPair<>(el, owner));

        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
        }

        return false;
    }

    private void fillStatementWithEl(Vehicle el, String owner, PreparedStatement statement) throws SQLException {
        statement.setString(1, el.getName());
        statement.setDouble(2, el.getCoordinates().getX());
        statement.setLong(3, el.getCoordinates().getY());
        statement.setTimestamp(4, new Timestamp(el.getCreationDate().getTime()));
        statement.setLong(5, el.getEnginePower());
        statement.setObject(6, el.getType(), Types.OTHER);
        statement.setObject(7, el.getFuelType(), Types.OTHER);
        statement.setString(8, owner);
    }

    private boolean checkPermission(int id, @NonNull String userName){
        if(userName.equals("admin"))
            return true;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ? AND user_name = ?");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getString("owner") == userName;
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
        }
        return false;
    }

    @Override
    public synchronized boolean update(Vehicle el, String userName) throws UserPermissionException {
        try {
            if(!checkPermission(el.getId(), userName))
                throw new UserPermissionException();
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE Vehicles SET name = ?, coord_x = ?, coord_y = ?, creation_date = ?, engine_power = ?, vehicle_type = ?, fuel_type = ? WHERE id = ?"
            );
            fillStatementWithEl(el, userName, statement);
            statement.setInt(8, el.getId());
            if(statement.executeUpdate() > 0){
                this.storage.remove(new VehicleOwnerPair<>(el, userName));
                return this.storage.add(new VehicleOwnerPair<>(el, userName));
            }


        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);

        }
        return false;
    }

    @Override
    public synchronized boolean remove(@NonNull Vehicle el, @NonNull String userName) throws UserPermissionException {
        try {
            if(!checkPermission(el.getId(), userName))
                throw new UserPermissionException();
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?;");
            statement.setInt(1, el.getId());
            if(statement.executeUpdate() > 0)
                return this.storage.remove(el);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized Collection getCollection() {
        return new LinkedHashSet<Pair<Vehicle, String>>(this.storage);
    }

    @Override
    public void clear(@NonNull String userName) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE owner = ?;");
            statement.setString(1, userName);
            statement.executeUpdate();
            this.loadCollection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int size() {
        return this.storage.size();
    }

}
