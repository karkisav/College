import java.util.*;

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
    int day;  // 0-4 (Monday to Friday)
    int hour; // 9-16 (9 AM to 4 PM)
    String courseCode;
    String type; // "Lecture", "Tutorial", or "Lab"

    public TimeSlot(int day, int hour, String courseCode, String type) {
        this.day = day;
        this.hour = hour;
        this.courseCode = courseCode;
        this.type = type;
    }
}

class Timetable {
    private List<Course> courses;
    private TimeSlot[][] schedule;
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final int START_HOUR = 9;
    private static final int END_HOUR = 17;
    
    public Timetable(List<Course> courses) {
        this.courses = courses;
        this.schedule = new TimeSlot[5][END_HOUR - START_HOUR];
    }

    public void generateTimetable() {
        Random rand = new Random();
        
        // First, schedule labs as they need continuous slots
        for (Course course : courses) {
            if (course.practicalHours > 0) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR - 2); // -2 to ensure space for 3-hour labs
                    
                    if (canScheduleLab(day, hour, course.practicalHours)) {
                        scheduleLab(day, hour, course.practicalHours, course.code);
                        scheduled = true;
                    }
                }
            }
        }

        // Then schedule lectures
        for (Course course : courses) {
            for (int i = 0; i < course.lectureHours; i++) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR);
                    
                    if (isSlotFree(day, hour)) {
                        schedule[day][hour] = new TimeSlot(day, hour, course.code, "Lecture");
                        scheduled = true;
                    }
                }
            }
        }

        // Finally schedule tutorials
        for (Course course : courses) {
            for (int i = 0; i < course.tutorialHours; i++) {
                boolean scheduled = false;
                while (!scheduled) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR);
                    
                    if (isSlotFree(day, hour)) {
                        schedule[day][hour] = new TimeSlot(day, hour, course.code, "Tutorial");
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

    private void scheduleLab(int day, int startHour, int duration, String courseCode) {
        for (int i = 0; i < duration; i++) {
            schedule[day][startHour + i] = new TimeSlot(day, startHour + i, courseCode, "Lab");
        }
    }

    private boolean isSlotFree(int day, int hour) {
        return schedule[day][hour] == null;
    }

    public void printTimetable() {
        // Print header
        System.out.printf("%-10s", "Time");
        for (String day : DAYS) {
            System.out.printf("%-20s", day);
        }
        System.out.println("\n" + "-".repeat(110));

        // Print schedule
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
        System.out.println("\n");
    }
}

public class TimetableGenerator {
    public static void main(String[] args) {
        // Sample data for each semester
        Map<Integer, List<Course>> semesterCourses = new HashMap<>();
        
        // Semester 1 courses
        List<Course> sem1Courses = Arrays.asList(
            new Course("STAT101", "Statistics", 2, "2-0-0-0-2", "Dr. Ramesh Athe", "DSAI", "Combined"),
            new Course("DSAI101", "Introduction to DS and AI", 2, "2-0-0-0-2", "Dr. Abdul Wahid", "CSE", "Combined"),
            new Course("PROB101", "Probability", 2, "2-0-0-0-2", "Dr. Lakshman", "HSS", "Combined"),
            new Course("DD101", "Digital Design", 2, "3-0-2-0-2", "Dr. Jagadeesha R Bhat", "ECE", "Combined"),
            new Course("CSE101", "Problem Solving through Programming", 4, "3-0-2-0-4", "Dr. Sunil P V", "CSE", "2 sections"),
            new Course("ENG101", "English Language and Communication", 3, "3-0-0-0-3", "Dr. Rajesh", "HSS", "2 sections")
        );
        
        // Add courses to semesters
        semesterCourses.put(1, sem1Courses);
        
        // Generate and print timetables for each semester
        for (Map.Entry<Integer, List<Course>> entry : semesterCourses.entrySet()) {
            System.out.println("SEMESTER " + entry.getKey() + " TIMETABLE");
            System.out.println("=".repeat(110));
            
            Timetable timetable = new Timetable(entry.getValue());
            timetable.generateTimetable();
            timetable.printTimetable();
        }
    }
}