package me.eva.buidmgui.net;

import me.eva.buidmgui.Main;
import me.eva.buidmgui.model.EntityConfig;

import javax.security.auth.login.Configuration;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseConnection {

    private final Connection connection;

    public DatabaseConnection(String host, String database, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?useSSL=false&allowPublicKeyRetrieval=true", host, database), user, password);
        Main.LOGGER.info("Database connection established.");
    }

    public void close() throws SQLException {
        connection.close();
    }

    public static String resolveHost(String connector) {
        switch (connector.toLowerCase()) {
            case "dev":
                return "localhost";
            case "test":
                return "test";
            case "prod":
                return "prod";
            default:
                return connector;
        }
    }

    public String[][] getEntityConfigsRaw() {
        try {
            ResultSet rows = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION").executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rows.next()) {
                list.add(new String[]{
                        rows.getString("ENTITYNAME"),
                        rows.getString("PARAMETER_NAME"),
                        rows.getString("PARAMETER_VALUE"),
                        rows.getString("ACTIVE"),
                });
            }
            String[][] rowsArray = new String[list.size()][4];
            list.toArray(rowsArray);
            return rowsArray;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.getMessage());
            return new String[][]{};
        }
    }

    public String[][] getEntityLocationsRaw() {
        try {
            ResultSet rows = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION").executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rows.next()) {
                list.add(new String[]{
                        rows.getString("ENTITYNAME"),
                        rows.getString("LOCATION_ID"),
                        rows.getString("LOCATION_NAME"),
                        rows.getString("LOCATION_STREET"),
                        rows.getString("LOCATION_POSTCODE"),
                        rows.getString("LOCATION_CITY"),
                        rows.getString("LOCATION_COUNTRY"),
                        rows.getString("ACTIVE"),
                });
            }
            String[][] rowsArray = new String[list.size()][4];
            list.toArray(rowsArray);
            return rowsArray;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.getMessage());
            return new String[][]{};
        }
    }

    public ArrayList<EntityConfig> getEntityConfigs() throws SQLException {
        Main.LOGGER.info("Getting entity configs...");

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION WHERE PARAMETER_NAME='ENTITYNAME'");

        ArrayList<EntityConfig> uniqueEntities = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String entityName = resultSet.getString("ENTITYNAME");
            String mappedName = resultSet.getString("PARAMETER_VALUE");

            if(uniqueEntities.stream().anyMatch(entityConfig -> entityConfig.getEntityName().equals(entityName))) {
                continue;
            }
            EntityConfig entityConfig = new EntityConfig(entityName);
            System.out.println("SELECT * FROM ENTITYCONFIGURATION WHERE ENTITYNAME='" + mappedName + "'");
            ResultSet params = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION WHERE ENTITYNAME='" + mappedName + "'").executeQuery();

            while (params.next()) {
                String paramName = params.getString("PARAMETER_NAME");
                String paramValue = params.getString("PARAMETER_VALUE");

                entityConfig.getParameters().put(paramName, paramValue);
            }
            uniqueEntities.add(entityConfig);
        }

        ResultSet countResult = connection.prepareStatement("SELECT COUNT(*) FROM ENTITYCONFIGURATION").executeQuery();
        countResult.next();
        Main.LOGGER.info("Loaded " + countResult.getInt("COUNT(*)") + " entity configs.");

        return uniqueEntities;
    }
}
