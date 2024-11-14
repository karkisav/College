public class Student {
    private String studentId;
    private String name;
    private String contactDetails;

    public Student(String studentId, String name, String contactDetails) {
        this.studentId = studentId;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public void viewTimetable(Timetable timetable) {
        System.out.println("Viewing timetable for student.");
        timetable.displayTimetable();
    }

    public void login() {
        System.out.println("Student logged in.");
    }
}
