import java.io.FileWriter;
import java.io.IOException;

public class CSVUtils {
    public static void writeToCSV(String fileName, String data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
            System.out.println("CSV file has been generated successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to CSV: " + e.getMessage());
        }
    }
}