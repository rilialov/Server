package db;

public class DB {
    private static final DBConnector connector = new DBConnector();

    public DB() {
    }

    public static DBConnector getConnector() {
        return connector;
    }
}
