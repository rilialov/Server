package db;

import app.App;
import app.Controller;

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
    private static final Controller controller = App.getController();
    private static final String USER_DIR = System.getProperty("user.dir");
    private static FileReader FR;

    private static void loadProperties() {
        File file = new File(USER_DIR + "\\db.properties");
        Properties properties = new Properties();
        try {
            FR = new FileReader(file);
            properties.load(FR);
        } catch (IOException e) {
            controller.log("Failed to read properties");
        }
        DB_URL = properties.getProperty("url");
        USER = properties.getProperty("user");
        PASS = properties.getProperty("pass");
    }

    void setConnection() {
        loadProperties();
        try {
            Class.forName("org.postgresql.Driver");
            controller.log("DB driver successfully connected");
        } catch (ClassNotFoundException e) {
            controller.log("DB driver is not found");
            return;
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            controller.log("Connection to database failed");
            return;
        }

        if (connection != null) {
            controller.log("Successfully connected to database");
        } else {
            controller.log("Failed to make connection to database");
        }
    }

    void setStatement() {
        if (connection != null) {
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                controller.log("Failed to create statement");
            }
        }
    }

    void execute(String sqlCommand) {
        try {
            statement.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            controller.log("Failed to execute sql command");
        }
    }

    ResultSet getQuery(String sqlCommand) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlCommand);
            resultSet.next();
        } catch (SQLException e) {
            controller.log("Failed to get query");
        }
        return resultSet;
    }

    void close() {
        if (connection != null) {
            try {
                statement.close();
                connection.close();
                controller.log("Connection closed");
                FR.close();
            } catch (SQLException | IOException e) {
                controller.log("Failed to close DBConnector");
            }
        }
    }
}
