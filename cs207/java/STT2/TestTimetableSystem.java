import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestTimetableSystem {
    public static void main(String[] args) {
        // Create sample data and test the system
        try {
            // 1. Create timetable with basic settings
            Timetable timetable = new Timetable(
                LocalTime.of(9, 0),  // First slot at 9:00
                LocalTime.of(17, 0), // Last slot at 17:00
                60                   // 60-minute slots
            );

            // 2. Add rooms
            Room room1 = new Room("R101", false, 60, true);
            Room room2 = new Room("R102", false, 40, true);
            Room lab1 = new Room("L101", true, 30, true);
            
            timetable.addRoom(room1);
            timetable.addRoom(room2);
            timetable.addRoom(lab1);

            // 3. Create subjects
            Subject java = new Subject("CS101", "Java Programming", 3, 1, 2, 4, true);
            Subject python = new Subject("CS102", "Python Programming", 3, 1, 2, 4, true);
            Subject algorithms = new Subject("CS103", "Algorithms", 4, 2, 0, 4, false);

            // 4. Create faculty members
            Faculty prof1 = new Faculty("F001", "John Smith", "123-456-7890", 
                "john.smith@university.edu", "Computer Science", 20, true);
            prof1.addSpecialization("Java Programming");
            prof1.addSpecialization("Algorithms");

            Faculty prof2 = new Faculty("F002", "Jane Doe", "098-765-4321", 
                "jane.doe@university.edu", "Computer Science", 20, true);
            prof2.addSpecialization("Python Programming");
            prof2.addSpecialization("Algorithms");

            // 5. Create a batch
            Batch batch1 = new Batch("CS2023A", "Computer Science", 3, 40, 6);
            batch1.getSubjectList().add(java);
            batch1.getSubjectList().add(python);
            batch1.getSubjectList().add(algorithms);

            // 6. Assign teachers to subjects for the batch
            batch1.assignTeacher(java, prof1);
            batch1.assignTeacher(python, prof2);
            batch1.assignTeacher(algorithms, prof1);

            // 7. Generate timetable
            List<Batch> batches = new ArrayList<>();
            batches.add(batch1);
            timetable.generateTimeSlots(batches);

            // 8. Export the timetable
            timetable.exportToCSV("test_timetable.csv");

            // 9. Print success message
            System.out.println("Test completed successfully!");
            System.out.println("Timetable has been exported to 'test_timetable.csv'");

            // 10. Print some sample data from the timetable
            System.out.println("\nSample Timetable Entries:");
            timetable.getAllTimeSlots().stream()
                .limit(5)  // Show first 5 entries
                .forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}