import java.io.*;
import java.util.*;

class Course {
    String slNo;
    String courseCode;
    String courseName;
    int credits;
    String faculty;
    String department;
    String sections;
    int semester;
    boolean[] segments = new boolean[4];

    public Course(String[] data, int currentSemester) {
        this.semester = currentSemester;
        this.slNo = "";
        this.courseCode = "";
        this.courseName = "";
        this.credits = 0;
        this.faculty = "";
        this.department = "";
        this.sections = "";

        try {
            if (data.length > 2) this.slNo = data[2].trim();
            if (data.length > 3) this.courseCode = data[3].trim();
            if (data.length > 4) this.courseName = data[4].trim();
            if (data.length > 5 && !data[5].trim().isEmpty()) {
                try {
                    this.credits = Integer.parseInt(data[5].trim());
                } catch (NumberFormatException e) {
                    if (data.length > 6 && !data[6].trim().isEmpty()) {
                        String[] parts = data[6].trim().split("-");
                        if (parts.length == 5) {
                            this.credits = Integer.parseInt(parts[4]);
                        }
                    }
                }
            }
            if (data.length > 7) this.faculty = data[7].trim();
            if (data.length > 8) this.department = data[8].trim();
            if (data.length > 9) this.sections = data[9].trim();

            for (int i = 0; i < 4; i++) {
                if (data.length > 10 + i) {
                    this.segments[i] = "TRUE".equalsIgnoreCase(data[10 + i].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Error processing course data: " + String.join(",", data));
        }
    }

    public boolean hasSections() {
        return sections.toLowerCase().contains("section");
    }

    public List<String> getSectionsList() {
        if (!hasSections()) return Collections.singletonList("Combined");
        return Arrays.asList("A", "B");
    }

    public List<String> getFacultyList() {
        if (faculty.isEmpty()) return Collections.emptyList();
        return Arrays.asList(faculty.split("\n"));
    }

    @Override
    public String toString() {
        return String.format("Course[sem=%d, code=%s, name=%s, credits=%d, faculty=%s]",
                semester, courseCode, courseName, credits, faculty);
    }
}

class TimeSlot {
    int day;
    int period;
    String section;
    
    private static final String[] TIME_SLOTS = {
        "09:00 - 10:00",
        "10:00 - 11:00",
        "11:15 - 12:15",
        "12:15 - 01:15",
        "01:15 - 02:15",
        "02:45 - 03:45",
        "03:45 - 04:45",
        "04:45 - 05:30"
    };
    
    public TimeSlot(int day, int period, String section) {
        this.day = day;
        this.period = period;
        this.section = section;
    }
    
    public String getTimeString() {
        return TIME_SLOTS[period];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot that = (TimeSlot) o;
        return day == that.day && period == that.period && Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, period, section);
    }
    
    @Override
    public String toString() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return String.format("%s %s Section %s", days[day], getTimeString(), section);
    }
}


public class TimetableGenerator {
    private static final int DAYS = 5;
    private static final int PERIODS = 8;

    private Map<Integer, List<Course>> coursesBySemester = new HashMap<>();
    private Map<Integer, Map<String, List<TimeSlot>>> timetableBySemester = new HashMap<>();
    private Map<String, Set<TimeSlot>> facultySchedule = new HashMap<>();
    private int[] dayCounts = new int[DAYS]; // Track how many courses each day has

    public void loadCoursesFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int currentSemester = 0;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;
                if (lineCount < 3) continue;

                String[] data = line.split(",");
                if (data.length < 3) continue;

                String slNo = data[2].trim();
                if (slNo.toLowerCase().startsWith("sem")) {
                    try {
                        currentSemester = Integer.parseInt(slNo.toLowerCase().replace("sem", "").trim());
                        coursesBySemester.putIfAbsent(currentSemester, new ArrayList<>());
                        System.out.println("\nProcessing Semester " + currentSemester);
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Could not parse semester number from: " + slNo);
                    }
                    continue;
                }

                if (currentSemester > 0 && !slNo.isEmpty()) {
                    Course course = new Course(data, currentSemester);
                    if (course.credits > 0) {
                        coursesBySemester.get(currentSemester).add(course);
                        System.out.println("Loaded: " + course);
                    }
                }
            }

            for (Map.Entry<Integer, List<Course>> entry : coursesBySemester.entrySet()) {
                System.out.printf("Semester %d: %d courses loaded%n", entry.getKey(), entry.getValue().size());
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean generateTimetable() {
        timetableBySemester.clear();
        facultySchedule.clear();
        Arrays.fill(dayCounts, 0); // Reset day counts

        for (Map.Entry<Integer, List<Course>> semesterEntry : coursesBySemester.entrySet()) {
            int semester = semesterEntry.getKey();
            List<Course> courses = semesterEntry.getValue();

            System.out.printf("%nGenerating timetable for Semester %d...%n", semester);

            if (courses.isEmpty()) {
                System.out.println("No courses for semester " + semester);
                continue;
            }

            // Initialize available slots
            List<TimeSlot> availableSlots = new ArrayList<>();
            for (int day = 0; day < DAYS; day++) {
                for (int period = 0; period < PERIODS; period++) {
                    availableSlots.add(new TimeSlot(day, period, "A"));
                    availableSlots.add(new TimeSlot(day, period, "B"));
                }
            }

            Map<String, List<TimeSlot>> semesterTimetable = new HashMap<>();

            // Sort courses by constraints
            courses.sort((c1, c2) -> {
                int c1Constraints = (c1.sections.contains("section") ? 1 : 0) +
                                    (!c1.faculty.isEmpty() ? 1 : 0);
                int c2Constraints = (c2.sections.contains("section") ? 1 : 0) +
                                    (!c2.faculty.isEmpty() ? 1 : 0);
                return c2Constraints - c1Constraints;
            });

            // Assign slots for each course
            for (Course course : courses) {
                int periodsNeeded = course.credits;
                List<TimeSlot> courseSlots = new ArrayList<>();

                availableSlots.sort(Comparator.comparingInt(slot -> dayCounts[slot.day]));

                for (TimeSlot slot : new ArrayList<>(availableSlots)) {
                    if (courseSlots.size() >= periodsNeeded) break;

                    if (isSlotAvailable(slot, course)) {
                        courseSlots.add(slot);
                        availableSlots.remove(slot);
                        dayCounts[slot.day]++; // Increment the count for the assigned day

                        if (!course.faculty.isEmpty()) {
                            facultySchedule
                                .computeIfAbsent(course.faculty, k -> new HashSet<>())
                                .add(slot);
                        }
                    }
                }

                if (courseSlots.size() < periodsNeeded) {
                    System.out.printf("Could not allocate slots for: %s (Semester %d)%n",
                            course.courseName, semester);
                    return false;
                }

                semesterTimetable.put(course.courseCode + "-" + course.courseName, courseSlots);
            }

            timetableBySemester.put(semester, semesterTimetable);
        }

        return true;
    }

    private boolean isSlotAvailable(TimeSlot slot, Course course) {
        if (!course.faculty.isEmpty()) {
            Set<TimeSlot> facultySlots = facultySchedule.get(course.faculty);
            if (facultySlots != null && facultySlots.contains(slot)) {
                return false;
            }
        }

        Map<String, List<TimeSlot>> semesterTimetable = timetableBySemester.get(course.semester);
        if (semesterTimetable != null) {
            for (List<TimeSlot> slots : semesterTimetable.values()) {
                if (slots.contains(slot)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void printTimetable() {
        System.out.println("\nGenerated Timetable:");
        for (Map.Entry<Integer, Map<String, List<TimeSlot>>> semesterEntry : timetableBySemester.entrySet()) {
            int semester = semesterEntry.getKey();
            System.out.println("\nSemester " + semester + ":");
            for (Map.Entry<String, List<TimeSlot>> courseEntry : semesterEntry.getValue().entrySet()) {
                System.out.printf("  %s: %s%n", courseEntry.getKey(), courseEntry.getValue());
            }
        }
    }

    public static void main(String[] args) {
        TimetableGenerator generator = new TimetableGenerator();
        generator.loadCoursesFromCSV("courses.csv");
        if (generator.generateTimetable()) {
            generator.printTimetable();
        } else {
            System.out.println("Failed to generate timetable.");
        }
    }
}