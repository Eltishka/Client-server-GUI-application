package server.database;

import lombok.NonNull;
import lombok.SneakyThrows;
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

    @SneakyThrows
    private void loadCollection() {
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
    @SneakyThrows
    public boolean add(Vehicle el, String owner) {
        try {
            ResultSet resultSet;
            synchronized (this.connection) {
                PreparedStatement statement = this.connection.prepareStatement("INSERT INTO " + tableName + " (name, coord_x, coord_y, creation_date, engine_power, vehicle_type, fuel_type, owner) values (?, ?, ?, ?, ?, ?, ?, ?);");
                fillStatementWithEl(el, owner, statement);
                statement.executeUpdate();
                statement = this.connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1");
                resultSet = statement.executeQuery();
            }
            Integer id;
            if (resultSet.next())
                id = resultSet.getInt("id");
            else
                return false;
            el.setId(id);
            return this.storage.add(new VehicleOwnerPair<>(el, owner));

        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }

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


    @SneakyThrows
    private boolean checkPermission(int id, @NonNull String userName){
        ResultSet resultSet;
        if(userName.equals("admin"))
            return true;
        try {
            synchronized (this.connection) {
                PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ? AND owner = ?");
                statement.setInt(1, id);
                statement.setString(2, userName);
                resultSet = statement.executeQuery();
            }
            if (resultSet.next())
                return Objects.equals(resultSet.getString("owner"), userName);
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }
        return false;
    }

    @Override
    @SneakyThrows
    public boolean update(Vehicle el, String userName) throws UserPermissionException {
        try {
            
            int updateCount = 0;
            synchronized (this.connection) {
                if (!checkPermission(el.getId(), userName))
                    throw new UserPermissionException();
                PreparedStatement statement = this.connection.prepareStatement(
                        "UPDATE Vehicles SET name = ?, coord_x = ?, coord_y = ?, creation_date = ?, engine_power = ?, vehicle_type = ?, fuel_type = ? WHERE id = ?"
                );
                fillStatementWithEl(el, userName, statement);
                statement.setInt(8, el.getId());
                updateCount = statement.executeUpdate();
            }
            if (updateCount > 0) {
                this.storage.remove(new VehicleOwnerPair<>(el, userName));
                return this.storage.add(new VehicleOwnerPair<>(el, userName));
            }

        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;

        }
        return false;
    }

    @Override
    public boolean remove(@NonNull Vehicle el, @NonNull String userName) throws UserPermissionException {
        try {
            int updateCount = 0;
            if(!checkPermission(el.getId(), userName))
                throw new UserPermissionException();
            synchronized (this.connection) {
                PreparedStatement statement = this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?;");
                statement.setInt(1, el.getId());
                updateCount = statement.executeUpdate();
            }
            if (updateCount > 0) {
                return this.storage.remove(new VehicleOwnerPair<Vehicle, String>(el, userName));
            }
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Collection getCollection() {
        return new LinkedHashSet<Pair<Vehicle, String>>(this.storage);
    }

    @Override
    @SneakyThrows
    public void clear(@NonNull String userName) {
        try {
            synchronized (this.connection) {
                PreparedStatement statement = this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE owner = ?;");
                statement.setString(1, userName);
                statement.executeUpdate();
            }
            this.loadCollection();
        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }
    }

    @Override
    public int size() {
        return this.storage.size();
    }

}
