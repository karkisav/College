public class Faculty {
    private String facultyId;
    private String name;
    private String contactDetails;
    private String[] specializations;

    public Faculty(String facultyId, String name, String contactDetails, String[] specializations) {
        this.facultyId = facultyId;
        this.name = name;
        this.contactDetails = contactDetails;
        this.specializations = specializations;
    }

    public void viewTimetable(Timetable timetable) {
        System.out.println("Viewing timetable for faculty.");
        timetable.displayTimetable();
    }

    public void requestForChange() {
        System.out.println("Requesting change in timetable.");
    }

    public void login() {
        System.out.println("Faculty logged in.");
    }
}
