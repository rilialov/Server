package model;

import db.DBConnector;
import db.Fabric;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Form {
    private int course_id = 0;
    private int trainer_id = 0;
    private String date;
    private int student_id = 0;

    public Form(int form_id) {
        DBConnector connector = Fabric.getConnector();
        ResultSet resultSet = connector.getQuery("SELECT * FROM forms WHERE form_id = " + form_id + ";");

        if (resultSet != null) {
            try {
                course_id = resultSet.getInt(2);
                trainer_id = resultSet.getInt(3);
                student_id = resultSet.getInt(4);
                date = resultSet.getString(5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getData () {
        String[] array = new String[5];
        Course course = getCourse();
        array[0] = course.getCourseCode() + " - " + course.getCourseName();
        Trainer trainer = getTrainer();
        array[1] = trainer.getFirstName() + " " + trainer.getLastName();
        Student student = getStudent();
        array[2] = student.getFirstName();
        array[3] = student.getLastName();
        array[4] = date;
        return array;
    }

    private Course getCourse() {
        return new Course(course_id);
    }

    private Trainer getTrainer() {
        return new Trainer(trainer_id);
    }

    private Student getStudent() {
        return new Student(student_id);
    }

    public int getStudent_id() {
        return student_id;
    }
}
