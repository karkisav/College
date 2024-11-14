public class Coordinator {
    private String coordinatorId;
    private String name;
    private String contactDetails;

    public Coordinator(String coordinatorId, String name, String contactDetails) {
        this.coordinatorId = coordinatorId;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public void inputRoomAvailability(Timetable timetable, String roomAvailability) {
        timetable.setRoomAvailability(roomAvailability);
        System.out.println("Room availability updated.");
    }

    public void inputNumberOfBatches(Timetable timetable, int numBatches) {
        timetable.setNumberOfBatches(numBatches);
        System.out.println("Number of batches updated.");
    }

    public void inputSubjectsPerBatch(Batch batch, String[] subjects) {
        batch.setSubjectList(subjects);
        System.out.println("Subjects per batch updated.");
    }

    public void assignTeacher(Batch batch, String teacherName) {
        batch.assignTeacher(teacherName);
        System.out.println("Teacher assigned to batch: " + teacherName);
    }

    public void inputTimeForClassesOrLabs(Timetable timetable, String timeSchedule) {
        timetable.setTimeForClassesOrLabs(timeSchedule);
        System.out.println("Class/lab times updated.");
    }

    public void login() {
        System.out.println("Coordinator logged in.");
    }
}
