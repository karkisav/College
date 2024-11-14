import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get Timetable details
        System.out.print("Enter number of batches: ");
        int numberOfBatches = scanner.nextInt();
        System.out.print("Enter number of students per batch: ");
        int studentsPerBatch = scanner.nextInt();
        System.out.print("Enter number of classrooms: ");
        int numberOfClassrooms = scanner.nextInt();
        System.out.print("Enter number of labs: ");
        int numberOfLabs = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter room availability: ");
        String roomAvailability = scanner.nextLine();

        Timetable timetable = new Timetable(numberOfBatches, studentsPerBatch, numberOfClassrooms, numberOfLabs, roomAvailability);

        for (int i = 0; i < numberOfBatches; i++) {
            System.out.print("Enter Batch Name: ");
            String batchName = scanner.nextLine();
            System.out.print("Enter Year (e.g., 1, 2): ");
            int year = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Branch: ");
            String branch = scanner.nextLine();

            System.out.print("Enter number of subjects: ");
            int numSubjects = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            String[] subjects = new String[numSubjects];
            for (int j = 0; j < numSubjects; j++) {
                System.out.print("Enter Subject " + (j + 1) + ": ");
                subjects[j] = scanner.nextLine();
            }

            System.out.print("Enter Teacher Assigned: ");
            String teacher = scanner.nextLine();

            Batch batch = new Batch(batchName, subjects, year, branch);
            batch.assignTeacher(teacher);
            timetable.addBatch(batch);
        }

        // Display the timetable
        timetable.displayTimetable();

        // Save to CSV
        CSVUtils.writeToCSV("timetable.csv", timetable.toCSVData());
    }
}