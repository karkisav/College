import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private int numberOfBatches;
    private int numberOfStudentsPerBatch;
    private int numberOfClassrooms;
    private int numberOfLabs;
    private String roomAvailability;
    private boolean approved;
    private String timeForClassesOrLabs;
    private List<Batch> batches;

    // Default constructor
    public Timetable() {
        batches = new ArrayList<>();
    }

    // Parameterized constructor
    public Timetable(int numberOfBatches, int numberOfStudentsPerBatch, int numberOfClassrooms, int numberOfLabs, String roomAvailability) {
        this.numberOfBatches = numberOfBatches;
        this.numberOfStudentsPerBatch = numberOfStudentsPerBatch;
        this.numberOfClassrooms = numberOfClassrooms;
        this.numberOfLabs = numberOfLabs;
        this.roomAvailability = roomAvailability;
        this.batches = new ArrayList<>();
    }

    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    public void displayTimetable() {
        System.out.println("Timetable Details:");
        System.out.println("Number of Batches: " + numberOfBatches);
        System.out.println("Number of Students per Batch: " + numberOfStudentsPerBatch);
        System.out.println("Number of Classrooms: " + numberOfClassrooms);
        System.out.println("Number of Labs: " + numberOfLabs);
        System.out.println("Room Availability: " + roomAvailability);
        System.out.println("Approved: " + approved);
        for (Batch batch : batches) {
            System.out.println("Batch: " + batch.getNameOfBatch() + ", Year: " + batch.getYear() + ", Branch: " + batch.getBranch());
        }
    }

    public String toCSVData() {
        StringBuilder csvData = new StringBuilder();
        csvData.append("Batch,Year,Branch,Subjects,Teacher\n");
        for (Batch batch : batches) {
            csvData.append(batch.getNameOfBatch()).append(",");
            csvData.append(batch.getYear()).append(",");
            csvData.append(batch.getBranch()).append(",");
            csvData.append(String.join(" | ", batch.getSubjectList())).append(",");
            csvData.append(batch.getTeacherAssigned()).append("\n");
        }
        return csvData.toString();
    }

    // Other getters and setters if needed
}