package me.eva.buidmgui.net;

import me.eva.buidmgui.Main;
import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.model.EntityConfig;
import me.eva.buidmgui.model.EntityLocation;
import me.eva.buidmgui.model.EntityOrganisation;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
            Main.LOGGER.error(e.getMessage());
            return new String[][]{};
        }
    }

    public String[][] getEntityLocationsRaw() {
        try {
            ResultSet rows = connection.prepareStatement("SELECT * FROM ENTITYLOCATION").executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rows.next()) {
                list.add(new String[]{
                        rows.getString("ENTITY_NAME"),
                        rows.getString("LOCATION_ID"),
                        rows.getString("LOCATION_NAME"),
                        rows.getString("LOCATION_STREET"),
                        rows.getString("LOCATION_POSTCODE"),
                        rows.getString("LOCATION_CITY"),
                        rows.getString("LOCATION_COUNTRY"),
                        rows.getString("ACTIVE"),
                });
            }
            String[][] rowsArray = new String[list.size()][8];
            list.toArray(rowsArray);
            return rowsArray;
        } catch (SQLException e) {
            Main.LOGGER.error(e.getMessage());
            return new String[][]{};
        }
    }

    public String[][] getEntityOrganisationsRaw() {
        try {
            ResultSet rows = connection.prepareStatement("SELECT * FROM ENTITYORGANISATION").executeQuery();
            List<String[]> list = new ArrayList<>();
            while (rows.next()) {
                list.add(new String[]{
                        rows.getString("ENTITY_NAME"),
                        rows.getString("ORGID"),
                        rows.getString("ORGNAME"),
                        rows.getString("ORGLEVEL"),
                        rows.getString("ORGTYPE"),
                        rows.getString("PARENTID"),
                        rows.getString("ACTIVE"),
                        rows.getString("OEKEY"),
                });
            }
            String[][] rowsArray = new String[list.size()][8];
            list.toArray(rowsArray);
            return rowsArray;
        } catch (SQLException e) {
            Main.LOGGER.error(e.getMessage());
            return new String[][]{};
        }
    }

    private ArrayList<EntityConfig> readEntityConfigs() throws SQLException {
        System.out.println("Getting entity configs...");

        PreparedStatement entityStatement = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION WHERE PARAMETER_NAME='ENTITYNAME'");
        ArrayList<EntityConfig> uniqueEntities = new ArrayList<>();

        HashMap<String, String> parameters = new HashMap<>();

        ResultSet entityResultSet = entityStatement.executeQuery();
        while (entityResultSet.next()) {
            String entityName = entityResultSet.getString("ENTITYNAME");
            String mappedName = entityResultSet.getString("PARAMETER_VALUE");

            if (uniqueEntities.stream().anyMatch(entityConfig -> entityConfig.getEntityName().equals(entityName))) {
                continue;
            }
            MainPage.getInstance().writeLineToConsole("SELECT * FROM ENTITYCONFIGURATION WHERE ENTITYNAME='" + mappedName + "'");

            ResultSet params = connection.prepareStatement("SELECT * FROM ENTITYCONFIGURATION WHERE ENTITYNAME='" + mappedName + "'").executeQuery();

            while (params.next()) {
                String paramName = params.getString("PARAMETER_NAME");
                String paramValue = params.getString("PARAMETER_VALUE");

                parameters.put(paramName, paramValue);
            }

            EntityConfig entityConfig = new EntityConfig(entityName, mappedName);
            entityConfig.getParameters().putAll(parameters);

            uniqueEntities.add(entityConfig);
        }

        return uniqueEntities;
    }

    public ArrayList<EntityLocation> readEntityLocations(ArrayList<EntityConfig> entityConfigs) throws SQLException {
        ArrayList<EntityLocation> entityLocations = new ArrayList<>();

        for (EntityConfig entityConfig : entityConfigs) {
            MainPage.getInstance().writeLineToConsole("SELECT * FROM ENTITYLOCATION WHERE ENTITY_NAME='" + entityConfig.getFriendlyName() + "'");

            PreparedStatement locationsStatement = connection.prepareStatement("SELECT * FROM ENTITYLOCATION WHERE ENTITY_NAME='" + entityConfig.getFriendlyName() + "'");
            ResultSet locationsResultSet = locationsStatement.executeQuery();

            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("LOCATION_ID");
                String locationName = locationsResultSet.getString("LOCATION_NAME");
                String locationStreet = locationsResultSet.getString("LOCATION_STREET");
                String locationPostcode = locationsResultSet.getString("LOCATION_POSTCODE");
                String locationCity = locationsResultSet.getString("LOCATION_CITY");
                String locationCountry = locationsResultSet.getString("LOCATION_COUNTRY");
                int active = locationsResultSet.getInt("ACTIVE");

                entityLocations.add(new EntityLocation(entityConfig, locationId, locationName, locationStreet, locationPostcode, locationCity, locationCountry, active));
            }
        }
        return entityLocations;
    }

    private ArrayList<EntityOrganisation> readEntityOrganisations(ArrayList<EntityConfig> entityConfigs) throws SQLException {
        ArrayList<EntityOrganisation> organisations = new ArrayList<>();

        for (EntityConfig entityConfig : entityConfigs) {
            MainPage.getInstance().writeLineToConsole("SELECT * FROM ENTITYORGANISATION WHERE ENTITY_NAME='" + entityConfig.getFriendlyName() + "'");

            PreparedStatement locationsStatement = connection.prepareStatement("SELECT * FROM ENTITYORGANISATION WHERE ENTITY_NAME='" + entityConfig.getFriendlyName() + "'");
            ResultSet locationsResultSet = locationsStatement.executeQuery();
            while (locationsResultSet.next()) {
                int orgId = locationsResultSet.getInt("ORGID");
                String orgName = locationsResultSet.getString("ORGNAME");
                int orgLevel = locationsResultSet.getInt("ORGLEVEL");
                String orgType = locationsResultSet.getString("ORGTYPE");
                int parentId = locationsResultSet.getInt("PARENTID");
                int active = locationsResultSet.getInt("ACTIVE");

                organisations.add(new EntityOrganisation(entityConfig, orgId, orgName, orgLevel, orgType, parentId, active));
            }
        }
        return organisations;
    }

    public ArrayList<EntityConfig> getEntityConfigs() throws SQLException {
        ArrayList<EntityConfig> entityConfigs = readEntityConfigs();
        ArrayList<EntityLocation> entityLocations = readEntityLocations(entityConfigs);
        ArrayList<EntityOrganisation> entityOrganisations = readEntityOrganisations(entityConfigs);

        for (EntityConfig entityConfig : entityConfigs) {
            ArrayList<EntityLocation> joiner = new ArrayList<>();
            for (EntityLocation entityLocation : entityLocations) {
                if (entityLocation.getEntityName().equals(entityConfig.getFriendlyName())) {
                    joiner.add(entityLocation);
                }
            }
            entityConfig.getLocations().addAll(joiner);
        }

        for (EntityConfig entityConfig : entityConfigs) {
            ArrayList<EntityOrganisation> joiner = new ArrayList<>();
            for (EntityOrganisation entityOrganisation : entityOrganisations) {
                if (entityOrganisation.getEntityName().equals(entityConfig.getFriendlyName())) {
                    joiner.add(entityOrganisation);
                }
            }
            entityConfig.getOrganisations().addAll(joiner);
        }

        for (EntityConfig entityConfig : entityConfigs) {
            System.out.println(entityConfig.getFriendlyName());
            for (EntityLocation location : entityConfig.getLocations()) {
                System.out.println("\t" + location.getEntityName());
            }
        }

        return entityConfigs;
    }

    public void writeEntityParameterChange(String entityName, String parameterName, String parameterValue) throws SQLException {
        int changedRows = connection.prepareStatement(String.format("UPDATE ENTITYCONFIGURATION SET PARAMETER_VALUE=%s WHERE ENTITYNAME=%s AND PARAMETER_NAME=%s", parameterValue, entityName, parameterName)).executeUpdate();
        if (changedRows == 0) {
            System.out.println("No changes were made.");
        }
    }
}
