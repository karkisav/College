import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

class Course {
    String code;
    String name;
    int credits;
    String ltpsc;
    String faculty;
    String department;
    String sections;
    int lectureHours;
    int tutorialHours;
    int practicalHours;

    public Course(String code, String name, int credits, String ltpsc, String faculty, String department, String sections) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.ltpsc = ltpsc;
        this.faculty = faculty;
        this.department = department;
        this.sections = sections;

        if (ltpsc != null && !ltpsc.isEmpty()) {
            String[] parts = ltpsc.split("-");
            if (parts.length == 5) {
                this.lectureHours = Integer.parseInt(parts[0]);
                this.tutorialHours = Integer.parseInt(parts[1]);
                this.practicalHours = Integer.parseInt(parts[2]);
            }
        }
    }
}

class TimeSlot {
    int day; // 0-4 (Monday to Friday)
    int hour; // 9-16 (9 AM to 4 PM)
    String courseCode;
    String courseName;
    String type; // "Lecture", "Tutorial", or "Lab"
    String faculty;
    String section; // Added section information

    public TimeSlot(int day, int hour, String courseCode, String courseName, String faculty, String type, String section) {
        this.day = day;
        this.hour = hour;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.faculty = faculty;
        this.type = type;
        this.section = section;
    }
}

class Timetable {
    private List<Course> courses;
    private TimeSlot[][] schedule;
    private String section; // Added section identifier
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final int START_HOUR = 9;
    private static final int END_HOUR = 17;

    public Timetable(List<Course> courses, String section) {
        this.courses = courses;
        this.section = section;
        this.schedule = new TimeSlot[5][END_HOUR - START_HOUR];
    }

    public void generateTimetable() {
        Random rand = new Random();

        // Schedule labs first
        for (Course course : courses) {
            if (course.practicalHours > 0) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR - 2);
                    if (canScheduleLab(day, hour, course.practicalHours)) {
                        scheduleLab(day, hour, course.practicalHours, course);
                        scheduled = true;
                    }
                }
            }
        }

        // Schedule lectures
        for (Course course : courses) {
            for (int i = 0; i < course.lectureHours; i++) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR);
                    if (isSlotFree(day, hour)) {
                        schedule[day][hour] = new TimeSlot(day, hour, course.code, course.name, 
                            course.faculty, "Lecture", section);
                        scheduled = true;
                    }
                }
            }
        }

        // Schedule tutorials
        for (Course course : courses) {
            for (int i = 0; i < course.tutorialHours; i++) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR);
                    if (isSlotFree(day, hour)) {
                        schedule[day][hour] = new TimeSlot(day, hour, course.code, course.name, 
                            course.faculty, "Tutorial", section);
                        scheduled = true;
                    }
                }
            }
        }
    }

    private boolean canScheduleLab(int day, int startHour, int duration) {
        if (startHour + duration > END_HOUR - START_HOUR) return false;

        for (int i = 0; i < duration; i++) {
            if (!isSlotFree(day, startHour + i)) return false;
        }
        return true;
    }

    private void scheduleLab(int day, int startHour, int duration, Course course) {
        for (int i = 0; i < duration; i++) {
            schedule[day][startHour + i] = new TimeSlot(day, startHour + i, course.code, 
                course.name, course.faculty, "Lab", section);
        }
    }

    private boolean isSlotFree(int day, int hour) {
        return schedule[day][hour] == null;
    }

    public void printTimetable() {
        System.out.println("\nTIMETABLE FOR SECTION " + section);
        System.out.printf("%-10s", "Time");
        for (String day : DAYS) {
            System.out.printf("%-20s", day);
        }
        System.out.println("\n" + "-".repeat(110));

        for (int hour = 0; hour < END_HOUR - START_HOUR; hour++) {
            System.out.printf("%-10s", String.format("%02d:00", hour + START_HOUR));

            for (int day = 0; day < 5; day++) {
                TimeSlot slot = schedule[day][hour];
                if (slot == null) {
                    System.out.printf("%-20s", "---");
                } else {
                    String display = slot.courseCode + " (" + slot.type + ")";
                    System.out.printf("%-20s", display);
                }
            }
            System.out.println();
        }
    }

    public void exportToCSV(String filename, int semester) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("Semester " + semester + " Timetable - Section " + section);
            writer.println();
            
            // Write day headers
            writer.print("Time,");
            writer.println(String.join(",", DAYS));

            // Write schedule
            for (int hour = 0; hour < END_HOUR - START_HOUR; hour++) {
                StringBuilder line = new StringBuilder();
                line.append(String.format("%02d:00,", hour + START_HOUR));

                for (int day = 0; day < 5; day++) {
                    TimeSlot slot = schedule[day][hour];
                    if (slot == null) {
                        line.append("-");
                    } else {
                        // Format: CourseCode (Type) - CourseName [Faculty]
                        String entry = String.format("%s (%s) - %s", 
                            slot.courseCode != null ? slot.courseCode : "N/A",
                            slot.type,
                            slot.courseName != null ? slot.courseName : "N/A");
                        if (slot.faculty != null && !slot.faculty.isEmpty()) {
                            entry += String.format(" [%s]", slot.faculty);
                        }
                        line.append(entry);
                    }
                    line.append(day < 4 ? "," : "");
                }
                writer.println(line);
            }

            // Add course details section
            writer.println("\nCourse Details:");
            writer.println("Code,Name,Credits,L-T-P-S-C,Faculty,Department,Section");
            for (Course course : courses) {
                writer.printf("%s,%s,%d,%s,%s,%s,%s%n",
                    course.code != null ? course.code : "N/A",
                    course.name != null ? course.name : "N/A",
                    course.credits,
                    course.ltpsc != null ? course.ltpsc : "N/A",
                    course.faculty != null ? course.faculty : "N/A",
                    course.department != null ? course.department : "N/A",
                    section
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void exportToImage(String filename, int semester) {
        ImageGenerator.generateTimetableImage(schedule, courses, filename, semester, section);
    }
}

class ImageGenerator {
    private static final Color HEADER_BG = new Color(51, 51, 51);
    private static final Color CELL_BG = new Color(255, 255, 255);
    private static final Color LECTURE_COLOR = new Color(200, 230, 255);
    private static final Color TUTORIAL_COLOR = new Color(255, 230, 200);
    private static final Color LAB_COLOR = new Color(200, 255, 200);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color HEADER_TEXT = new Color(255, 255, 255);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font CELL_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font LEGEND_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final int CELL_HEIGHT = 60;
    private static final int CELL_WIDTH = 200;
    private static final int TIME_COLUMN_WIDTH = 80;
    private static final int LEGEND_HEIGHT = 200;

    public static void generateTimetableImage(TimeSlot[][] schedule, List<Course> courses, String outputPath, int semester, String section) {
        int width = TIME_COLUMN_WIDTH + (CELL_WIDTH * 5);
        int height = (CELL_HEIGHT * (17 - 9 + 1)) + LEGEND_HEIGHT;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fill background
        g2d.setColor(CELL_BG);
        g2d.fillRect(0, 0, width, height);
        
        // Draw title
        g2d.setColor(HEADER_BG);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.fillRect(0, 0, width, CELL_HEIGHT);
        g2d.setColor(HEADER_TEXT);
        String title = String.format("Semester %d Timetable - Section %s", semester, section);
        FontMetrics titleMetrics = g2d.getFontMetrics();
        g2d.drawString(title, (width - titleMetrics.stringWidth(title)) / 2, CELL_HEIGHT / 2 + titleMetrics.getHeight() / 4);
        
        // Draw day headers
        g2d.setFont(HEADER_FONT);
        String[] days = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int i = 0; i < days.length; i++) {
            int x = (i == 0) ? 0 : TIME_COLUMN_WIDTH + (i - 1) * CELL_WIDTH;
            int w = (i == 0) ? TIME_COLUMN_WIDTH : CELL_WIDTH;
            
            g2d.setColor(HEADER_BG);
            g2d.fillRect(x, CELL_HEIGHT, w, CELL_HEIGHT);
            
            g2d.setColor(HEADER_TEXT);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(days[i], x + (w - fm.stringWidth(days[i])) / 2, 
                          CELL_HEIGHT + CELL_HEIGHT / 2 + fm.getHeight() / 4);
        }
        
        // Draw time slots and schedule
        g2d.setFont(CELL_FONT);
        for (int hour = 0; hour < 17 - 9; hour++) {
            // Draw time
            int y = (hour + 2) * CELL_HEIGHT;
            g2d.setColor(HEADER_BG);
            g2d.fillRect(0, y, TIME_COLUMN_WIDTH, CELL_HEIGHT);
            g2d.setColor(HEADER_TEXT);
            String time = String.format("%02d:00", hour + 9);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(time, (TIME_COLUMN_WIDTH - fm.stringWidth(time)) / 2, 
                          y + CELL_HEIGHT / 2 + fm.getHeight() / 4);
            
            // Draw schedule cells
            for (int day = 0; day < 5; day++) {
                int x = TIME_COLUMN_WIDTH + day * CELL_WIDTH;
                TimeSlot slot = schedule[day][hour];
                
                // Draw cell background
                g2d.setColor(CELL_BG);
                g2d.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                
                if (slot != null) {
                    // Set background color based on type
                    switch (slot.type) {
                        case "Lecture":
                            g2d.setColor(LECTURE_COLOR);
                            break;
                        case "Tutorial":
                            g2d.setColor(TUTORIAL_COLOR);
                            break;
                        case "Lab":
                            g2d.setColor(LAB_COLOR);
                            break;
                    }
                    g2d.fillRect(x + 1, y + 1, CELL_WIDTH - 2, CELL_HEIGHT - 2);
                    
                    // Draw text
                    g2d.setColor(TEXT_COLOR);
                    String[] lines = {
                        slot.courseCode,
                        "(" + slot.type + ")",
                        slot.faculty
                    };
                    
                    int textY = y + CELL_HEIGHT / 4;
                    for (String line : lines) {
                        if (line != null && !line.isEmpty()) {
                            g2d.drawString(line, x + 5, textY);
                            textY += fm.getHeight();
                        }
                    }
                }
            }
        }
        
        // Draw legend
        int legendY = (17 - 9 + 2) * CELL_HEIGHT;
        g2d.setFont(LEGEND_FONT);
        g2d.setColor(HEADER_BG);
        g2d.fillRect(0, legendY, width, 40);
        g2d.setColor(HEADER_TEXT);
        g2d.drawString("Legend:", 10, legendY + 25);
        
        // Draw color boxes for class types
        int boxSize = 20;
        int legendX = 100;
        g2d.setColor(LECTURE_COLOR);
        g2d.fillRect(legendX, legendY + 10, boxSize, boxSize);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Lecture", legendX + boxSize + 10, legendY + 25);
        
        legendX += 150;
        g2d.setColor(TUTORIAL_COLOR);
        g2d.fillRect(legendX, legendY + 10, boxSize, boxSize);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Tutorial", legendX + boxSize + 10, legendY + 25);
        
        legendX += 150;
        g2d.setColor(LAB_COLOR);
        g2d.fillRect(legendX, legendY + 10, boxSize, boxSize);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Lab", legendX + boxSize + 10, legendY + 25);
        
        // Draw course details
        g2d.setColor(TEXT_COLOR);
        int courseY = legendY + 60;
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Course Details:", 10, courseY);
        courseY += 25;
        
        g2d.setFont(LEGEND_FONT);
        for (Course course : courses) {
            String details = String.format("%s - %s (%s)", course.code, course.name, course.faculty);
            g2d.drawString(details, 20, courseY);
            courseY += 20;
        }
        
        g2d.dispose();
        
        try {
            ImageIO.write(image, "PNG", new File(outputPath));
        } catch (IOException e) {
            System.err.println("Error saving timetable image: " + e.getMessage());
        }
    }
}

public class TimetableGenerator {
    private static Map<Integer, Map<String, List<Course>>> parseCoursesFromCSV(String filepath) {
        // Map<Semester, Map<Section, List<Course>>>
        Map<Integer, Map<String, List<Course>>> semesterSectionCourses = new HashMap<>();
        int currentSemester = 0;
        int courseCounter = 1;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Skip header
            br.readLine();
            lineNumber++;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.replace("`", "'").trim();
                if (line.isEmpty()) continue;
                
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim().replace("\"", "");
                }
                
                if (line.trim().startsWith("Sem")) {
                    try {
                        String semStr = line.split(",")[0].replace("Sem", "").trim();
                        currentSemester = Integer.parseInt(semStr);
                        semesterSectionCourses.putIfAbsent(currentSemester, new HashMap<>());
                        courseCounter = 1;
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid semester format at line " + lineNumber);
                    }
                    continue;
                }

                if (currentSemester == 0 || values.length < 1) {
                    continue;
                }

                try {
                    if (values[0].isEmpty() || !values[0].matches("\\d+")) {
                        continue;
                    }
                    
                    String code = values.length > 1 ? values[1].trim() : "";
                    if (code.isEmpty()) {
                        code = String.format("TEMP%d_%d", currentSemester, courseCounter++);
                    }

                    String name = values.length > 2 ? values[2].trim() : "Unnamed Course";
                    if (name.isEmpty()) {
                        name = "Unnamed Course " + code;
                    }

                    int credits = 2;
                    if (values.length > 3 && !values[3].isEmpty()) {
                        try {
                            credits = Integer.parseInt(values[3].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid credits format for " + name);
                        }
                    }

                    String ltpsc = values.length > 4 ? values[4].trim() : "";
                    if (ltpsc.isEmpty()) {
                        ltpsc = credits + "-0-0-0-" + credits;
                    }

                    String faculty = values.length > 5 ? values[5].trim().replace("\n", " / ") : "TBD";
                    faculty = faculty.replace("(", "").replace(")", "");

                    String department = values.length > 6 ? values[6].trim() : "TBD";
                    if (department.isEmpty()) department = "TBD";

                    // Create course for both sections
                    Course course = new Course(code, name, credits, ltpsc, faculty, department, "A");
                    semesterSectionCourses.get(currentSemester).putIfAbsent("A", new ArrayList<>());
                    semesterSectionCourses.get(currentSemester).get("A").add(course);

                    Course courseB = new Course(code, name, credits, ltpsc, faculty, department, "B");
                    semesterSectionCourses.get(currentSemester).putIfAbsent("B", new ArrayList<>());
                    semesterSectionCourses.get(currentSemester).get("B").add(courseB);
                    
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Skipping invalid line " + lineNumber);
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return semesterSectionCourses;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the path to the courses CSV file as a command line argument.");
            return;
        }

        String csvFilePath = args[0];
        Map<Integer, Map<String, List<Course>>> semesterSectionCourses = parseCoursesFromCSV(csvFilePath);

        try {
            Files.createDirectories(Paths.get("timetables"));
        } catch (IOException e) {
            System.err.println("Error creating timetables directory: " + e.getMessage());
            return;
        }

        // Generate timetables for each semester and section
        for (Map.Entry<Integer, Map<String, List<Course>>> semesterEntry : semesterSectionCourses.entrySet()) {
            int semester = semesterEntry.getKey();
            Map<String, List<Course>> sectionCourses = semesterEntry.getValue();
            
            for (Map.Entry<String, List<Course>> sectionEntry : sectionCourses.entrySet()) {
                String section = sectionEntry.getKey();
                List<Course> courses = sectionEntry.getValue();
                
                if (courses.isEmpty()) {
                    continue;
                }

                System.out.println("\nSEMESTER " + semester + " - SECTION " + section + " TIMETABLE");
                System.out.println("=".repeat(110));
                
                // Print course details
                System.out.println("\nCourse Details:");
                System.out.println(String.format("%-10s %-40s %-8s %-12s %-30s %-20s %-15s",
                    "Code", "Name", "Credits", "L-T-P-S-C", "Faculty", "Department", "Section"));
                System.out.println("-".repeat(135));
                
                for (Course course : courses) {
                    System.out.println(String.format("%-10s %-40s %-8d %-12s %-30s %-20s %-15s",
                        course.code,
                        course.name,
                        course.credits,
                        course.ltpsc,
                        course.faculty,
                        course.department,
                        section));
                }
                System.out.println();

                Timetable timetable = new Timetable(courses, section);
                timetable.generateTimetable();
                timetable.printTimetable();
                
                // Export to CSV
                String filename = String.format("timetables/semester_%d_section_%s_timetable.csv", semester, section);
                timetable.exportToCSV(filename, semester);
                System.out.println("\nTimetable exported to " + filename);

                // Export to PNG
                String imageFilename = String.format("timetables/semester_%d_section_%s_timetable.png", semester, section);
                timetable.exportToImage(imageFilename, semester);
                System.out.println("Timetable image exported to " + imageFilename);
            }
        }
    }
}