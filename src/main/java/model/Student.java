package model;

import db.DBConnector;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
    private String firstName;
    private String lastName;

    public Student(int student_id) {
        DBConnector connector = DB.getConnector();
        ResultSet resultSet = connector.getQuery("SELECT * FROM students WHERE student_id = " + student_id + ";");

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
