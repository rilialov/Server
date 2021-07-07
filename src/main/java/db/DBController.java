package db;

import model.Form;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBController {
    private static final DBConnector dbConnector = new DBConnector();

    public DBController() {
        dbConnector.setConnection();
        dbConnector.setStatement();
    }

    public int getFormID(String login) {
        int id = 0;
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM users WHERE login = '" + login + "';");
        if (resultSet != null) {
            try {
                id = resultSet.getInt(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public int[] getFormData(int form_id) {
        int[] ids = new int[3];
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM forms WHERE form_id = " + form_id + ";");
        if (resultSet != null) {
            try {
                ids[0] = resultSet.getInt(2);
                ids[1] = resultSet.getInt(3);
                ids[2] = resultSet.getInt(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    public String getFormDate(int form_id) {
        String date = null;
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM forms WHERE form_id = " + form_id + ";");
        if (resultSet != null) {
            try {
                date = resultSet.getString(5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public String[] getCourseData(int course_id) {
        String[] courseData = new String[2];
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM courses WHERE course_id = " + course_id + ";");

        if (resultSet != null) {
            try {
                courseData[0] = resultSet.getString(3);
                courseData[1] = resultSet.getString(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return courseData;
    }

    public String[] getTrainerData(int trainer_id) {
        String[] trainerData = new String[2];
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM trainers WHERE trainer_id = " + trainer_id + ";");

        if (resultSet != null) {
            try {
                trainerData[0] = resultSet.getString(2);
                trainerData[1] = resultSet.getString(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trainerData;
    }

    public String[] getStudentData(int student_id) {
        String[] studentData = new String[2];
        ResultSet resultSet = dbConnector.getQuery("SELECT * FROM students WHERE student_id = " + student_id + ";");

        if (resultSet != null) {
            try {
                studentData[0] = resultSet.getString(2);
                studentData[1] = resultSet.getString(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return studentData;
    }

    public void updateForm(String[] array, Form form) {
        dbConnector.execute("UPDATE students SET first_name_ed = '" + array[0] +
                "', last_name_ed = '" + array[1] +
                "', phone_ed = '" + array[2] +
                "', email_ed = '" + array[3] +
                "' WHERE student_id = " + form.getStudent_id() + ";");
    }

    public void close() {
        dbConnector.close();
    }
}
