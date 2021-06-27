package db;

public class Fabric {
    private static final DBConnector connector = new DBConnector();

    public Fabric() {
    }

    public static DBConnector getConnector() {
        return connector;
    }
}
