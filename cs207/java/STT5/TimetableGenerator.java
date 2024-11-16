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
    int seminarHours;

    public Course(String code, String name, int credits, String ltpsc, String faculty, String department, String sections) {
        this.code = code != null ? code : name.replaceAll("\\s+", "").toUpperCase();  // Generate a code if none provided
        this.name = name;
        this.credits = credits;
        this.ltpsc = ltpsc;
        this.faculty = faculty;
        this.department = department;
        this.sections = sections;
        
        // Parse L-T-P-S-C format
        if (ltpsc != null && !ltpsc.isEmpty()) {
            String[] parts = ltpsc.split("-");
            if (parts.length == 5) {
                this.lectureHours = Integer.parseInt(parts[0]);
                this.tutorialHours = Integer.parseInt(parts[1]);
                this.practicalHours = Integer.parseInt(parts[2]);
                this.seminarHours = Integer.parseInt(parts[3]);
            } else {
                setDefaultHours();
            }
        } else {
            setDefaultHours();
        }
    }

    private void setDefaultHours() {
        // Default values if L-T-P-S-C not specified
        this.lectureHours = credits > 0 ? credits - 1 : 0;
        this.tutorialHours = 0;
        this.practicalHours = credits > 0 ? 2 : 0;
        this.seminarHours = 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%d credits)", name, credits);
    }
}

class TimeSlot {
    int day;  // 0-4 (Monday to Friday)
    int hour; // 9-16 (9 AM to 4 PM)
    String courseCode;
    String courseName;
    String type; // "Lecture", "Tutorial", "Lab", or "Seminar"
    String faculty;

    public TimeSlot(int day, int hour, String courseCode, String courseName, String type, String faculty) {
        this.day = day;
        this.hour = hour;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.type = type;
        this.faculty = faculty;
    }
}

class Timetable {
    private List<Course> courses;
    private TimeSlot[][] schedule;
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final int START_HOUR = 9;
    private static final int END_HOUR = 17;
    private static final int LUNCH_HOUR = 12;
    
    public Timetable(List<Course> courses) {
        this.courses = courses;
        this.schedule = new TimeSlot[5][END_HOUR - START_HOUR];
    }

    public void generateTimetable() {
        Random rand = new Random();
        
        // Reserve lunch hour
        for (int day = 0; day < 5; day++) {
            schedule[day][LUNCH_HOUR - START_HOUR] = new TimeSlot(day, LUNCH_HOUR - START_HOUR, "LUNCH", "Lunch Break", "Break", "");
        }

        // First, schedule labs as they need continuous slots
        for (Course course : courses) {
            if (course.practicalHours > 0) {
                boolean scheduled = false;
                int attempts = 0;
                while (!scheduled && attempts < 100) {
                    int day = rand.nextInt(5);
                    int hour = rand.nextInt(END_HOUR - START_HOUR - course.practicalHours);
                    
                    if (canScheduleLab(day, hour, course.practicalHours)) {
                        scheduleLab(day, hour, course);
                        scheduled = true;
                    }
                    attempts++;
                }
                if (!scheduled) {
                    System.out.println("Warning: Could not schedule lab for " + course.name);
                }
            }
        }

        // Schedule lectures with proper distribution
        for (Course course : courses) {
            int lecturesScheduled = 0;
            int attempts = 0;
            while (lecturesScheduled < course.lectureHours && attempts < 100) {
                int day = rand.nextInt(5);
                // Prefer morning slots for lectures
                int hour = rand.nextInt(END_HOUR - START_HOUR - 3) + 1;
                
                if (isSlotFree(day, hour) && !hasLectureOnSameDay(day, course)) {
                    schedule[day][hour] = new TimeSlot(day, hour, course.code, course.name, "Lecture", course.faculty);
                    lecturesScheduled++;
                }
                attempts++;
            }
            if (lecturesScheduled < course.lectureHours) {
                System.out.println("Warning: Could not schedule all lectures for " + course.name);
            }
        }

        // Schedule tutorials
        for (Course course : courses) {
            if (course.tutorialHours > 0) {
                for (int i = 0; i < course.tutorialHours; i++) {
                    boolean scheduled = false;
                    int attempts = 0;
                    while (!scheduled && attempts < 100) {
                        int day = rand.nextInt(5);
                        // Prefer afternoon slots for tutorials
                        int hour = rand.nextInt(3) + (LUNCH_HOUR - START_HOUR + 1);
                        
                        if (isSlotFree(day, hour) && !hasTutorialOnSameDay(day, course)) {
                            schedule[day][hour] = new TimeSlot(day, hour, course.code, course.name, "Tutorial", course.faculty);
                            scheduled = true;
                        }
                        attempts++;
                    }
                    if (!scheduled) {
                        System.out.println("Warning: Could not schedule tutorial for " + course.name);
                    }
                }
            }
        }
    }

    private boolean hasLectureOnSameDay(int day, Course course) {
        for (int hour = 0; hour < END_HOUR - START_HOUR; hour++) {
            TimeSlot slot = schedule[day][hour];
            if (slot != null && 
                slot.courseCode != null && 
                slot.courseCode.equals(course.code) && 
                slot.type.equals("Lecture")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasTutorialOnSameDay(int day, Course course) {
        for (int hour = 0; hour < END_HOUR - START_HOUR; hour++) {
            TimeSlot slot = schedule[day][hour];
            if (slot != null && 
                slot.courseCode != null && 
                slot.courseCode.equals(course.code) && 
                slot.type.equals("Tutorial")) {
                return true;
            }
        }
        return false;
    }

    private boolean canScheduleLab(int day, int startHour, int duration) {
        if (startHour + duration > END_HOUR - START_HOUR) return false;
        
        // Check for lunch hour conflict
        if (startHour <= LUNCH_HOUR - START_HOUR && 
            startHour + duration > LUNCH_HOUR - START_HOUR) return false;
        
        for (int i = 0; i < duration; i++) {
            if (!isSlotFree(day, startHour + i)) return false;
        }
        return true;
    }

    private void scheduleLab(int day, int startHour, Course course) {
        for (int i = 0; i < course.practicalHours; i++) {
            schedule[day][startHour + i] = new TimeSlot(
                day, 
                startHour + i, 
                course.code, 
                course.name,
                "Lab", 
                course.faculty
            );
        }
    }

    private boolean isSlotFree(int day, int hour) {
        return schedule[day][hour] == null;
    }

    public void printTimetable() {
        // Print header
        System.out.printf("%-10s", "Time");
        for (String day : DAYS) {
            System.out.printf("%-30s", day);
        }
        System.out.println("\n" + "-".repeat(160));

        // Print schedule
        for (int hour = 0; hour < END_HOUR - START_HOUR; hour++) {
            System.out.printf("%-10s", String.format("%02d:00", hour + START_HOUR));
            
            for (int day = 0; day < 5; day++) {
                TimeSlot slot = schedule[day][hour];
                if (slot == null) {
                    System.out.printf("%-30s", "---");
                } else {
                    String display = String.format("%s%n%s", 
                        slot.courseCode != null ? slot.courseCode : slot.courseName,
                        slot.type);
                    if (slot.type.equals("Break")) {
                        display = "LUNCH BREAK";
                    }
                    System.out.printf("%-30s", display);
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }
}

public class TimetableGenerator {
    public static void main(String[] args) {
        Map<Integer, List<Course>> semesterCourses = new HashMap<>();
        
        // Semester 1 courses
        List<Course> sem1Courses = Arrays.asList(
            new Course(null, "Statistics", 2, null, "Dr. Ramesh Athe", "DSAI", "Combined"),
            new Course(null, "Introduction to DS and AI", 2, null, "Dr. Abdul Wahid", "CSE", "Combined"),
            new Course(null, "Open elective I", 2, null, null, null, null),
            new Course(null, "Probability", 2, null, "Dr. Lakshman", "HSS", "Combined"),
            new Course(null, "Digital Design", 2, "3-0-2-0-2", "Dr. Jagadeesha R Bhat", "ECE", "Combined"),
            new Course(null, "Problem Solving through Programming", 4, "3-0-2-0-4", "Dr. Sunil P V", "CSE", "2 sections"),
            new Course(null, "English Language and Communication", 3, "3-0-0-0-3", "Dr. Rajesh", "HSS", "2 sections")
        );

        // Semester 3 courses
        List<Course> sem3Courses = Arrays.asList(
            new Course("CS201", "Discrete Mathematics", 4, "3-1-0-0-4", "Dr. Animesh Roy", "CSE", "2 sections"),
            new Course("CS207", "OOP", 4, "3-0-2-0-4", "Dr. Pramod", "CSE", "2 sections"),
            new Course("CS208", "Computer Architecture", 4, "3-0-2-0-4", "Dr. Prabhu Prasad B M", "CSE", "2 sections"),
            new Course("CS202", "DAA", 5, "3-1-2-0-4", "Dr. Malay", "CSE", "2 sections"),
            new Course("MA201", "Probability", 4, "3-1-0-0-4", "Dr. Anand", "HSS", "2 sections"),
            new Course(null, "Industrial Social Psychology", 3, "3-0-0-0-3", "Dr. Navyashree", "HSS", "2 sections")
        );

        // Semester 5 courses
        List<Course> sem5Courses = Arrays.asList(
            new Course("CS309", "Statistics for Computer Science", 4, "3-1-0-0-4", "Dr. Ramesh Athe", "DSAI and CSE", "2 sections"),
            new Course("CS303", "Computer Networks", 5, "3-1-2-0-5", "Dr. C B Akki", "CSE", "2 sections"),
            new Course("CS304", "Artificial Intelligence", 4, "3-1-0-0-4", "Dr. Krishendu", "CSE", "2 sections"),
            new Course(null, "Graph Theory", 4, "4-0-0-0-4", "Dr. Pavan", "CSE", "2 sections"),
            new Course(null, "Cryptography and Information Security", 4, "4-0-0-0-4", "Dr. Rajendra Hegadi", "DSAI", null),
            new Course("HS101", "Environmental studies", 2, "0-0-0-8-2", null, null, null)
        );

        // Semester 7 courses (Electives)
        List<Course> sem7Courses = Arrays.asList(
            new Course(null, "Elective 1", 4, "4-0-0-0-4", null, null, null),
            new Course(null, "Elective 2", 4, "4-0-0-0-4", null, null, null),
            new Course(null, "Elective 3", 4, "4-0-0-0-4", null, null, null),
            new Course(null, "Elective 4", 4, "4-0-0-0-4", null, null, null),
            new Course(null, "Mini project II", 2, "0-0-0-8-2", null, null, null)
        );
        
        // Add all semesters
        semesterCourses.put(1, sem1Courses);
        semesterCourses.put(3, sem3Courses);
        semesterCourses.put(5, sem5Courses);
        semesterCourses.put(7, sem7Courses);
        
        // Generate and print timetables for each semester
        for (Map.Entry<Integer, List<Course>> entry : semesterCourses.entrySet()) {
            System.out.println("SEMESTER " + entry.getKey() + " TIMETABLE");
            System.out.println("=".repeat(160));
            
            Timetable timetable = new Timetable(entry.getValue());
            timetable.generateTimetable();
            timetable.printTimetable();
        }
    }
}