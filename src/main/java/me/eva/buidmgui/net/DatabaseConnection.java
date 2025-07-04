package me.eva.buidmgui.net;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.SQLException;

public class DatabaseConnection {

    private final Connection connection;

    public DatabaseConnection(String host, String database, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?useSSL=false", host, database), user, password);
    }

    public void close() throws SQLException {
        connection.close();
    }
}
