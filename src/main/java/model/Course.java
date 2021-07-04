package model;

import db.DBConnector;
import db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    private String courseCode;
    private String courseName;

    public Course(int course_id) {
        DBConnector connector = DB.getConnector();
        ResultSet resultSet = connector.getQuery("SELECT * FROM courses WHERE course_id = " + course_id + ";");

        if (resultSet != null) {
            try {
                courseCode = resultSet.getString(3);
                courseName = resultSet.getString(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }
}
