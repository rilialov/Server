package db;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnector {
    private static String DB_URL;
    private static String USER;
    private static String PASS;
    private static Connection connection = null;
    private static Statement statement;

    private static FileReader FR;

    private static void loadProperties() {
        File file = new File("C:\\Java\\db.properties");
        Properties properties = new Properties();
        try {
            FR = new FileReader(file);
            properties.load(FR);
        } catch (IOException e) {
            System.err.println("Failed to read properties");
        }
        DB_URL = properties.getProperty("url");
        USER = properties.getProperty("user");
        PASS = properties.getProperty("pass");
    }

    public void setConnection() {
        loadProperties();
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver is not found.");
            return;
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Connection Failed");
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.err.println("Failed to make connection to database");
        }
    }

    public void setStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Failed to create statement");
        }
    }

    public void execute(String sqlCommand) {
        try {
            statement.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.err.println("Failed to execute sql command");
        }
    }

    public ResultSet getQuery(String sqlCommand) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlCommand);
            resultSet.next();
        } catch (SQLException e) {
            System.err.println("Failed to get query");
        }
        return resultSet;
    }

    public void close() {
        if (connection != null) {
            try {
                statement.close();
                connection.close();
                System.out.println("Connection closed");
                FR.close();
            } catch (SQLException | IOException e) {
                System.err.println("Failed to close DBConnector");
            }
        }
    }
}
