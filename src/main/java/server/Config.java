package server;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Properties properties;
    @Getter
    private final String vehiclesDatabasePath;
    @Getter
    private final String vehiclesDatabaseUser;
    @Getter
    private final String vehiclesDatabasePassword;
    @Getter
    private final String vehiclesDatabaseName;
    @Getter
    private final String userDatabasePath;
    @Getter
    private final String userDatabaseUser;
    @Getter
    private final String userDatabasePassword;
    @Getter
    private final String userDatabaseName;

    private Config(){

        this.vehiclesDatabasePath = (String) properties.get("vehiclesDatabasePath");
        this.vehiclesDatabaseUser = (String) properties.get("vehiclesDatabaseUser");
        this.vehiclesDatabasePassword = (String) properties.get("vehiclesDatabasePassword");
        this.userDatabasePath = (String) properties.get("userDatabasePath");
        this.userDatabaseUser = (String) properties.get("userDatabaseUser");
        this.userDatabasePassword = (String) properties.get("userDatabasePassword");
        this.userDatabaseName = (String) properties.get("userDatabaseName");
        this.vehiclesDatabaseName = (String) properties.get("vehiclesDatabaseName");
    }

    public static void createConfig(String propertiesPath) throws IOException {
        properties = new Properties();
        properties.load(new FileReader(propertiesPath));
    }
    public static Config getConfig() {
        return new Config();
    }
}
