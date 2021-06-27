package model;

import db.DBConnector;
import db.Fabric;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Trainer {
    private String firstName;
    private String lastName;

    public Trainer(int trainer_id) {
        DBConnector connector = Fabric.getConnector();
        ResultSet resultSet = connector.getQuery("SELECT * FROM trainers WHERE trainer_id = " + trainer_id + ";");

        if (resultSet != null) {
            try {
                firstName = resultSet.getString(2);
                lastName = resultSet.getString(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
