package db;

import model.Form;
import model.Course;
import model.Student;
import model.Trainer;

public class FormBuilder {
    private final DBController db;
    private final Form form;
    private Course course;
    private Trainer trainer;
    private Student student;
    private final String date;

    public FormBuilder(int formID, DBController db) {
        this.db = db;
        int[] ids = db.getFormData(formID);
        date = db.getFormDate(formID);
        form = new Form(ids[0], ids[1], ids[2]);
        setForm();
    }

    private void setForm() {
        setCourse(form.getCourse_id());
        setTrainer(form.getTrainer_id());
        setStudent(form.getStudent_id());
    }

    private void setCourse(int id) {
        String[] data = db.getCourseData(id);
        course = new Course(data[0], data[1]);
    }

    private void setTrainer(int id) {
        String[] data = db.getTrainerData(id);
        trainer = new Trainer(data[0], data[1]);
    }

    private void setStudent(int id) {
        String[] data = db.getStudentData(id);
        student = new Student(data[0], data[1]);
    }

    public String[] getData () {
        String[] array = new String[5];
        array[0] = course.getCourseCode() + " - " + course.getCourseName();
        array[1] = trainer.getFirstName() + " " + trainer.getLastName();
        array[2] = student.getFirstName();
        array[3] = student.getLastName();
        array[4] = date;
        return array;
    }

    public Form getForm() {
        return form;
    }
}
