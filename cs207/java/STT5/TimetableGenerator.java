import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.imageio.*;
class Room {
    String roomNumber;
    boolean isLab;
    boolean isOccupied;
    String assignedSection; // Track which section is using this room

    public Room(String roomNumber, boolean isLab) {
        this.roomNumber = roomNumber;
        this.isLab = isLab;
        this.isOccupied = false;
        this.assignedSection = null;
    }
}   

class RoomManager {
    private List<Room> rooms;
    private List<Room> labRooms;
    private Map<Integer, Map<String, Room>> semesterSectionRoomMap;
    private Map<Integer, List<Room>> availableRoomsBySemester;
    private static final int TOTAL_FLOORS = 4;
    private static final int ROOMS_PER_FLOOR = 5;
    
    public RoomManager() {
        rooms = new ArrayList<>();
        labRooms = new ArrayList<>();
        semesterSectionRoomMap = new HashMap<>();
        availableRoomsBySemester = new HashMap<>();
        
        // Initialize regular classrooms (C001 to C405)
        for (int floor = 0; floor <= TOTAL_FLOORS; floor++) {
            for (int room = 1; room <= ROOMS_PER_FLOOR; room++) {
                String roomNumber = String.format("C%d%02d", floor, room);
                if (room == ROOMS_PER_FLOOR) { // Make the last room on each floor a lab
                    labRooms.add(new Room(roomNumber, true));
                } else {
                    rooms.add(new Room(roomNumber, false));
                }
            }
        }
    }

    public String assignRoomToSection(int semester, String section) {
        // Initialize semester maps if they don't exist
        semesterSectionRoomMap.putIfAbsent(semester, new HashMap<>());
        availableRoomsBySemester.putIfAbsent(semester, new ArrayList<>(rooms));

        // Check if section already has a room assigned for this semester
        if (semesterSectionRoomMap.get(semester).containsKey(section)) {
            return semesterSectionRoomMap.get(semester).get(section).roomNumber;
        }

        // Get available rooms for this semester
        List<Room> availableRooms = availableRoomsBySemester.get(semester);
        
        // Check if we need to reset room availability for this semester
        boolean needsReset = true;
        for (Room room : availableRooms) {
            if (!room.isOccupied) {
                needsReset = false;
                break;
            }
        }
        
        // If all rooms are occupied, try to find rooms that aren't being used in the current timeslot
        if (needsReset) {
            availableRooms = new ArrayList<>();
            for (Room room : rooms) {
                if (!room.isLab && !isRoomOccupiedInCurrentTimeslot(room)) {
                    availableRooms.add(room);
                }
            }
            availableRoomsBySemester.put(semester, availableRooms);
        }

        // Try to find a room on the same floor as other sections from the same semester
        Room selectedRoom = findRoomOnPreferredFloor(semester, availableRooms);

        // If no room found on preferred floor, take any available room
        if (selectedRoom == null) {
            selectedRoom = findAnyAvailableRoom(availableRooms);
        }

        if (selectedRoom != null) {
            assignRoom(selectedRoom, semester, section);
            return selectedRoom.roomNumber;
        }

        return "No Room Available";
    }

    private boolean isRoomOccupiedInCurrentTimeslot(Room room) {
        // This method should be implemented to check if the room is being used
        // in the current timeslot by checking against your timetable
        // You'll need to pass additional parameters like day and time
        return room.isOccupied;
    }

    private Room findRoomOnPreferredFloor(int semester, List<Room> availableRooms) {
        String preferredFloor = findPreferredFloor(semester);
        if (preferredFloor != null) {
            for (Room room : availableRooms) {
                if (room.roomNumber.startsWith(preferredFloor) && !room.isOccupied && !room.isLab) {
                    return room;
                }
            }
        }
        return null;
    }

    private String findPreferredFloor(int semester) {
        Map<String, Integer> floorCount = new HashMap<>();
        String mostUsedFloor = null;
        int maxCount = 0;

        // Count rooms used by this semester on each floor
        for (Room room : semesterSectionRoomMap.get(semester).values()) {
            String floor = room.roomNumber.substring(0, 2);
            int count = floorCount.getOrDefault(floor, 0) + 1;
            floorCount.put(floor, count);
            
            if (count > maxCount) {
                maxCount = count;
                mostUsedFloor = floor;
            }
        }

        return mostUsedFloor;
    }

    private Room findAnyAvailableRoom(List<Room> availableRooms) {
        for (Room room : availableRooms) {
            if (!room.isOccupied && !room.isLab) {
                return room;
            }
        }
        return null;
    }

    private void assignRoom(Room room, int semester, String section) {
        room.isOccupied = true;
        room.assignedSection = section;
        semesterSectionRoomMap.get(semester).put(section, room);
        availableRoomsBySemester.get(semester).remove(room);
    }

    public String getLabRoom(int semester, String section) {
        Room regularRoom = semesterSectionRoomMap.get(semester).get(section);
        if (regularRoom != null) {
            // Try to get a lab room from the same floor
            String floor = regularRoom.roomNumber.substring(1, 2);
            Room labRoom = findAvailableLabOnFloor(floor);
            if (labRoom != null) {
                return assignLabRoom(labRoom, section);
            }
        }
        
        // If no lab room on the same floor is available, find any available lab
        Room anyLab = findAnyAvailableLab();
        if (anyLab != null) {
            return assignLabRoom(anyLab, section);
        }

        // If all labs are occupied, find a lab used by the same semester
        if (regularRoom != null) {
            Room sharedLab = findLabUsedBySameSemester(semester);
            if (sharedLab != null) {
                return sharedLab.roomNumber;
            }
        }

        return "No Lab Available";
    }

    private Room findAvailableLabOnFloor(String floor) {
        for (Room lab : labRooms) {
            if (!lab.isOccupied && lab.roomNumber.startsWith("C" + floor)) {
                return lab;
            }
        }
        return null;
    }

    private Room findAnyAvailableLab() {
        for (Room lab : labRooms) {
            if (!lab.isOccupied) {
                return lab;
            }
        }
        return null;
    }

    private Room findLabUsedBySameSemester(int semester) {
        for (Room lab : labRooms) {
            if (lab.assignedSection != null && 
                semesterSectionRoomMap.get(semester).containsKey(lab.assignedSection)) {
                return lab;
            }
        }
        return null;
    }

    private String assignLabRoom(Room lab, String section) {
        lab.isOccupied = true;
        lab.assignedSection = section;
        return lab.roomNumber;
    }

    public void reset() {
        semesterSectionRoomMap.clear();
        availableRoomsBySemester.clear();
        for (Room room : rooms) {
            room.isOccupied = false;
            room.assignedSection = null;
        }
        for (Room lab : labRooms) {
            lab.isOccupied = false;
            lab.assignedSection = null;
        }
    }

    public Map<String, Integer> getRoomUtilizationStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total_rooms", rooms.size() + labRooms.size());
        stats.put("occupied_rooms", (int) rooms.stream().filter(r -> r.isOccupied).count());
        stats.put("occupied_labs", (int) labRooms.stream().filter(r -> r.isOccupied).count());
        return stats;
    }
}

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
    String assignedRoom; // Add assigned room field
    String assignedLabRoom; // Add assigned lab room field

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

class TimeBlock {
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
    boolean isBreak;

    public TimeBlock(int startHour, int startMinute, int endHour, int endMinute, boolean isBreak) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.isBreak = isBreak;
    }

    public String getTimeRange() {
        return String.format("%02d:%02d-%02d:%02d", startHour, startMinute, endHour, endMinute);
    }

    public boolean overlaps(TimeBlock other) {
        int thisStart = startHour * 60 + startMinute;
        int thisEnd = endHour * 60 + endMinute;
        int otherStart = other.startHour * 60 + other.startMinute;
        int otherEnd = other.endHour * 60 + other.endMinute;
        
        return !(thisEnd <= otherStart || thisStart >= otherEnd);
    }
}

class TimeSlot {
    int day;
    TimeBlock timeBlock;
    String courseCode;
    String courseName;
    String faculty;
    String type;
    String section;
    String room;

    public TimeSlot(int day, TimeBlock timeBlock, String courseCode, String courseName, 
                   String faculty, String type, String section, String room) {
        this.day = day;
        this.timeBlock = timeBlock;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.faculty = faculty;
        this.type = type;
        this.section = section;
        this.room = room;
    }
}

class Timetable {
    private List<Course> courses;
    private List<TimeSlot> timeSlots;
    private String section;
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private RoomManager roomManager;
    private int semester;
    
    private static final List<TimeBlock> BREAKS = Arrays.asList(
        new TimeBlock(10, 30, 10, 45, true), // Morning break
        new TimeBlock(12, 30, 14, 0, true)   // Lunch break
    );
    
    private static final List<TimeBlock> AVAILABLE_SLOTS = Arrays.asList(
        new TimeBlock(9, 0, 10, 30, false),
        new TimeBlock(10, 45, 12, 30, false),
        new TimeBlock(14, 0, 15, 30, false),
        new TimeBlock(15, 30, 17, 0, false)
    );  

    public Timetable(List<Course> courses, String section, RoomManager roomManager, int semester) {
        this.courses = courses;
        this.section = section;
        this.timeSlots = new ArrayList<>();
        this.roomManager = roomManager;
        this.semester = semester;
        
        // Assign rooms to courses using semester-based assignment
        String regularRoom = roomManager.assignRoomToSection(semester, section);
        String labRoom = roomManager.getLabRoom(semester, section);
        
        for (Course course : courses) {
            course.assignedRoom = regularRoom;
            if (course.practicalHours > 0) {
                course.assignedLabRoom = labRoom;
            }
        }
    }

    public List<TimeSlot> getTimeSlots() {
        return new ArrayList<>(timeSlots);
    }

    private TimeBlock createTimeBlockForSession(String type) {
        switch (type) {
            case "Lecture":
                return new TimeBlock(0, 0, 1, 30, false); // 1.5 hours
            case "Tutorial":
            case "Lab":
                return new TimeBlock(0, 0, 1, 0, false); // 1 hour for both tutorials and labs
            default:
                return null;
        }
    }

    private boolean isTimeSlotAvailable(int day, TimeBlock proposedTime) {
        // Check if the proposed time overlaps with any breaks
        for (TimeBlock breakTime : BREAKS) {
            if (proposedTime.overlaps(breakTime)) {
                return false;
            }
        }

        // Check if the proposed time overlaps with any existing sessions
        for (TimeSlot existingSlot : timeSlots) {
            if (existingSlot.day == day && proposedTime.overlaps(existingSlot.timeBlock)) {
                return false;
            }
        }

        // Check if the proposed time fits within available slots
        boolean fitsInAvailableSlot = false;
        for (TimeBlock availableSlot : AVAILABLE_SLOTS) {
            if (proposedTime.startHour * 60 + proposedTime.startMinute >= 
                availableSlot.startHour * 60 + availableSlot.startMinute &&
                proposedTime.endHour * 60 + proposedTime.endMinute <= 
                availableSlot.endHour * 60 + availableSlot.endMinute) {
                fitsInAvailableSlot = true;
                break;
            }
        }
        return fitsInAvailableSlot;
    }

    public void generateTimetable() {
        Random rand = new Random();
        
        // Clear any existing time slots before generating new ones
        timeSlots.clear();

        // Schedule all sessions (lectures, tutorials, and labs)
        for (Course course : courses) {
            // Schedule lectures (1.5 hours each)
            for (int i = 0; i < course.lectureHours; i++) {
                scheduleSession(course, "Lecture", rand);
            }

            // Schedule tutorials (1 hour each)
            for (int i = 0; i < course.tutorialHours; i++) {
                scheduleSession(course, "Tutorial", rand);
            }

            // Schedule labs (1 hour each)
            for (int i = 0; i < course.practicalHours; i++) {
                scheduleSession(course, "Lab", rand);
            }
        }
    }

    private void scheduleSession(Course course, String type, Random rand) {
        boolean scheduled = false;
        int attempts = 0;
        
        while (!scheduled && attempts < 100) {
            int day = rand.nextInt(5);
            TimeBlock sessionBlock = null;
            
            for (TimeBlock availableSlot : AVAILABLE_SLOTS) {
                TimeBlock proposedSession;
                if (type.equals("Lecture")) {
                    proposedSession = new TimeBlock(
                        availableSlot.startHour,
                        availableSlot.startMinute,
                        availableSlot.startHour + 1,
                        availableSlot.startMinute + 30,
                        false
                    );
                } else { // Tutorial or Lab (both 1 hour)
                    proposedSession = new TimeBlock(
                        availableSlot.startHour,
                        availableSlot.startMinute,
                        availableSlot.startHour + 1,
                        availableSlot.startMinute,
                        false
                    );
                }
                
                if (isTimeSlotAvailable(day, proposedSession)) {
                    sessionBlock = proposedSession;
                    break;
                }
            }
            
            if (sessionBlock != null) {
                timeSlots.add(new TimeSlot(
                    day, 
                    sessionBlock, 
                    course.code, 
                    course.name,
                    course.faculty, 
                    type, 
                    section, 
                    type.equals("Lab") ? course.assignedLabRoom : course.assignedRoom
                ));
                scheduled = true;
            }
            attempts++;
        }
        
        if (!scheduled) {
            System.err.println("WARNING: Failed to schedule " + type + " for " + course.code);
        }
    }

    // Verify that all sessions are scheduled correctly
    public boolean verifyScheduling() {
        for (Course course : courses) {
            // Verify lectures
            int scheduledLectures = countSessionsByType(course.code, "Lecture");
            if (scheduledLectures != course.lectureHours) {
                System.err.println("ERROR: Mismatch in lecture hours for " + course.code +
                                " (Expected: " + course.lectureHours + ", Found: " + scheduledLectures + ")");
                return false;
            }
            
            // Verify tutorials
            int scheduledTutorials = countSessionsByType(course.code, "Tutorial");
            if (scheduledTutorials != course.tutorialHours) {
                System.err.println("ERROR: Mismatch in tutorial hours for " + course.code +
                                " (Expected: " + course.tutorialHours + ", Found: " + scheduledTutorials + ")");
                return false;
            }
            
            // Verify labs
            int scheduledLabs = countSessionsByType(course.code, "Lab");
            if (scheduledLabs != course.practicalHours) {
                System.err.println("ERROR: Mismatch in lab hours for " + course.code +
                                " (Expected: " + course.practicalHours + ", Found: " + scheduledLabs + ")");
                return false;
            }
        }
        return true;
    }
    
        private int countSessionsByType(String courseCode, String type) {
            return (int) timeSlots.stream()
                .filter(slot -> slot.courseCode.equals(courseCode) && slot.type.equals(type))
                .count();
        }

    private boolean isValidLabSlot(int day, TimeBlock proposedTime) {
        // First check if the slot fits within available working hours
        boolean fitsInAvailableSlot = false;
        for (TimeBlock availableSlot : AVAILABLE_SLOTS) {
            int proposedStart = proposedTime.startHour * 60 + proposedTime.startMinute;
            int proposedEnd = proposedTime.endHour * 60 + proposedTime.endMinute;
            int availableStart = availableSlot.startHour * 60 + availableSlot.startMinute;
            int availableEnd = availableSlot.endHour * 60 + availableSlot.endMinute;
            
            if (proposedStart >= availableStart && 
                proposedEnd <= availableEnd && 
                (proposedEnd - proposedStart) == 120) { // Ensure exactly 2 hours
                fitsInAvailableSlot = true;
                break;
            }
        }
        
        if (!fitsInAvailableSlot) {
            return false;
        }

        // Check for break overlaps
        for (TimeBlock breakTime : BREAKS) {
            if (proposedTime.overlaps(breakTime)) {
                return false;
            }
        }

        // Check for conflicts with existing sessions
        for (TimeSlot existingSlot : timeSlots) {
            if (existingSlot.day == day && proposedTime.overlaps(existingSlot.timeBlock)) {
                return false;
            }
        }

        return true;
    }

    private boolean fitsWithinSlot(TimeBlock proposed, TimeBlock available) {
        int proposedStart = proposed.startHour * 60 + proposed.startMinute;
        int proposedEnd = proposed.endHour * 60 + proposed.endMinute;
        int availableStart = available.startHour * 60 + available.startMinute;
        int availableEnd = available.endHour * 60 + available.endMinute;
        
        return proposedStart >= availableStart && 
            proposedEnd <= availableEnd && 
            (proposedEnd - proposedStart) == 60; // Ensure exactly 2 hours
    }

    // Add helper method to check if a lab was successfully scheduled
    public boolean verifyLabScheduling() {
        for (Course course : courses) {
            if (course.practicalHours > 0) {
                boolean found = false;
                for (TimeSlot slot : timeSlots) {
                    if (slot.courseCode.equals(course.code) && slot.type.equals("Lab")) {
                        found = true;
                        // Verify lab duration
                        int duration = (slot.timeBlock.endHour * 60 + slot.timeBlock.endMinute) -
                                    (slot.timeBlock.startHour * 60 + slot.timeBlock.startMinute);
                        if (duration != 120) { // 2 hours = 120 minutes
                            System.err.println("WARNING: Lab duration incorrect for " + course.code +
                                            " (Duration: " + duration + " minutes)");
                        }
                        break;
                    }
                }
                if (!found) {
                    System.err.println("ERROR: No lab slot found for " + course.code);
                    return false;
                }
            }
        }
        return true;
    }

    public void printTimetable() {
        System.out.println("\nTIMETABLE FOR SECTION " + section);
        System.out.printf("%-15s", "Time");
        for (String day : DAYS) {
            System.out.printf("%-30s", day);
        }
        System.out.println("\n" + "-".repeat(165));

        // Sort time slots by time and day
        List<TimeBlock> allTimeBlocks = new ArrayList<>(AVAILABLE_SLOTS);
        allTimeBlocks.addAll(BREAKS);
        Collections.sort(allTimeBlocks, (a, b) -> {
            int timeA = a.startHour * 60 + a.startMinute;
            int timeB = b.startHour * 60 + b.startMinute;
            return timeA - timeB;
        });

        for (TimeBlock block : allTimeBlocks) {
            System.out.printf("%-15s", block.getTimeRange());

            if (block.isBreak) {
                String breakText = block.startHour == 12 ? "LUNCH BREAK" : "BREAK";
                for (int day = 0; day < 5; day++) {
                    System.out.printf("%-30s", breakText);
                }
            } else {
                for (int day = 0; day < 5; day++) {
                    boolean slotFilled = false;
                    for (TimeSlot slot : timeSlots) {
                        if (slot.day == day && slot.timeBlock.overlaps(block)) {
                            String display = String.format("%s (%s) [%s]", 
                                slot.courseCode, slot.type, slot.room);
                            System.out.printf("%-30s", display);
                            slotFilled = true;
                            break;
                        }
                    }
                    if (!slotFilled) {
                        System.out.printf("%-30s", "---");
                    }
                }
            }
            System.out.println();
        }
    }

    public void exportToCSV(String filename, int semester) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Semester " + semester + " Timetable - Section " + section);
            writer.println();
            
            writer.print("Time,");
            writer.println(String.join(",", DAYS));

            List<TimeBlock> allTimeBlocks = new ArrayList<>(AVAILABLE_SLOTS);
            allTimeBlocks.addAll(BREAKS);
            Collections.sort(allTimeBlocks, (a, b) -> {
                int timeA = a.startHour * 60 + a.startMinute;
                int timeB = b.startHour * 60 + b.startMinute;
                return timeA - timeB;
            });

            for (TimeBlock block : allTimeBlocks) {
                StringBuilder line = new StringBuilder();
                line.append(block.getTimeRange()).append(",");

                if (block.isBreak) {
                    String breakText = block.startHour == 12 ? "LUNCH BREAK" : "BREAK";
                    for (int day = 0; day < 5; day++) {
                        line.append(breakText);
                        if (day < 4) line.append(",");
                    }
                } else {
                    for (int day = 0; day < 5; day++) {
                        boolean slotFilled = false;
                        for (TimeSlot slot : timeSlots) {
                            if (slot.day == day && slot.timeBlock.overlaps(block)) {
                                String entry = String.format("%s (%s) - %s [%s]", 
                                    slot.courseCode, 
                                    slot.type,
                                    slot.courseName,
                                    slot.room);
                                if (slot.faculty != null && !slot.faculty.isEmpty()) {
                                    entry += String.format(" <%s>", slot.faculty);
                                }
                                line.append(entry);
                                slotFilled = true;
                                break;
                            }
                        }
                        if (!slotFilled) {
                            line.append("-");
                        }
                        if (day < 4) line.append(",");
                    }
                }
                writer.println(line);
            }

            writer.println("\nCourse Details:");
            writer.println("Code,Name,Credits,L-T-P-S-C,Faculty,Department,Section,Regular Room,Lab Room");
            for (Course course : courses) {
                writer.printf("%s,%s,%d,%s,%s,%s,%s,%s,%s%n",
                    course.code,
                    course.name,
                    course.credits,
                    course.ltpsc,
                    course.faculty,
                    course.department,
                    section,
                    course.assignedRoom,
                    course.practicalHours > 0 ? course.assignedLabRoom : "N/A"
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}

    // ... (previous Course, TimeBlock, TimeSlot, and Timetable classes remain the same)

public class TimetableGenerator {
    private static final String BASE_OUTPUT_DIR = "generated_timetables";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
    private static final List<TimeBlock> AVAILABLE_SLOTS = Arrays.asList(
        new TimeBlock(8, 30, 9, 30, false),
        new TimeBlock(9, 30, 10, 30, false),
        new TimeBlock(10, 30, 11, 30, false),
        new TimeBlock(11, 30, 12, 30, false),
        new TimeBlock(12, 30, 13, 30, false),
        new TimeBlock(13, 30, 14, 30, false),
        new TimeBlock(14, 30, 15, 30, false),
        new TimeBlock(15, 30, 16, 30, false)
    );

    // File management methods
    private static void initializeDirectoryStructure() throws IOException {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String baseDir = String.format("%s/%s", BASE_OUTPUT_DIR, timestamp);
        
        // Create main directories
        createDirectory(baseDir);
        createDirectory(baseDir + "/CSE");
        createDirectory(baseDir + "/ECE");
        createDirectory(baseDir + "/DSAI");
        
        // Create subdirectories for each department
        createDirectory(baseDir + "/CSE/csv");
        createDirectory(baseDir + "/CSE/images");
        createDirectory(baseDir + "/ECE/csv");
        createDirectory(baseDir + "/ECE/images");
        createDirectory(baseDir + "/DSAI/csv");
        createDirectory(baseDir + "/DSAI/images");
        
        System.out.println("Created output directory structure at: " + baseDir);
    }

    private static void createDirectory(String dir) throws IOException {
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private static String getOutputFilePath(String department, int semester, String section, String type, String timestamp) {
        String baseDir = String.format("%s/%s/%s", BASE_OUTPUT_DIR, timestamp, department);
        String subDir = type.equals("csv") ? "csv" : "images";
        String extension = type.equals("csv") ? "csv" : "png";
        
        String filename;
        if (department.equals("CSE") && !section.isEmpty()) {
            filename = String.format("%s_sem%d_section%s.%s", 
                department.toLowerCase(), semester, section, extension);
        } else {
            filename = String.format("%s_sem%d.%s", 
                department.toLowerCase(), semester, extension);
        }
        
        return String.format("%s/%s/%s", baseDir, subDir, filename);
    }


    private static Map<Integer, Map<String, List<Course>>> parseCSECoursesFromCSV(String filepath) {
        // Existing parseCoursesFromCSV logic for CSE (with sections A and B)
        return parseCoursesFromCSV(filepath, true);
    }

    private static Map<Integer, List<Course>> parseOtherDepartmentCoursesFromCSV(String filepath) {
        Map<Integer, Map<String, List<Course>>> tempResult = parseCoursesFromCSV(filepath, false);
        Map<Integer, List<Course>> result = new HashMap<>();
        
        // Convert the nested structure to a simpler one without sections
        for (Map.Entry<Integer, Map<String, List<Course>>> entry : tempResult.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get("A")); // Use only the 'A' section's courses
        }
        
        return result;
    }

    private static Map<Integer, Map<String, List<Course>>> parseCoursesFromCSV(String filepath, boolean createSections) {
        Map<Integer, Map<String, List<Course>>> semesterSectionCourses = new HashMap<>();
        int currentSemester = 0;
        int courseCounter = 1;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); // Skip header
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

                if (currentSemester == 0 || values.length < 1) continue;

                try {
                    if (values[0].isEmpty() || !values[0].matches("\\d+")) continue;
                    
                    // Parse course details
                    Course course = parseCourseFromValues(values, currentSemester, courseCounter, "A");
                    
                    // Add to section A
                    semesterSectionCourses.get(currentSemester).putIfAbsent("A", new ArrayList<>());
                    semesterSectionCourses.get(currentSemester).get("A").add(course);
                    
                    // If creating sections (CSE only), create section B copy
                    if (createSections) {
                        Course courseB = parseCourseFromValues(values, currentSemester, courseCounter, "B");
                        semesterSectionCourses.get(currentSemester).putIfAbsent("B", new ArrayList<>());
                        semesterSectionCourses.get(currentSemester).get("B").add(courseB);
                    }
                    
                    courseCounter++;
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Skipping invalid line " + lineNumber);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return semesterSectionCourses;
    }

    private static Course parseCourseFromValues(String[] values, int currentSemester, int courseCounter, String section) {
        String code = values.length > 1 ? values[1].trim() : String.format("TEMP%d_%d", currentSemester, courseCounter);
        String name = values.length > 2 ? values[2].trim() : "Unnamed Course " + code;
        int credits = values.length > 3 && !values[3].isEmpty() ? Integer.parseInt(values[3].trim()) : 2;
        String ltpsc = values.length > 4 ? values[4].trim() : credits + "-0-0-0-" + credits;
        String faculty = values.length > 5 ? values[5].trim().replace("\n", " / ").replace("(", "").replace(")", "") : "TBD";
        String department = values.length > 6 ? values[6].trim() : "TBD";
        
        return new Course(code, name, credits, ltpsc, faculty, department, section);
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please provide paths to three CSV files: CSE, ECE, and DSAI courses.");
            System.out.println("Usage: java TimetableGenerator <cse_csv> <ece_csv> <dsai_csv>");
            return;
        }

        String timestamp = LocalDateTime.now().format(DATE_FORMAT);

        try {
            // Initialize directory structure
            initializeDirectoryStructure();
            
            // Parse courses for each department
            Map<Integer, Map<String, List<Course>>> cseCourses = parseCSECoursesFromCSV(args[0]);
            Map<Integer, List<Course>> eceCourses = parseOtherDepartmentCoursesFromCSV(args[1]);
            Map<Integer, List<Course>> dsaiCourses = parseOtherDepartmentCoursesFromCSV(args[2]);

            RoomManager roomManager = new RoomManager();

            // Generate timetables for each department
            generateCSETimetables(cseCourses, roomManager, timestamp);
            generateDepartmentTimetables(eceCourses, roomManager, "ECE", timestamp);
            generateDepartmentTimetables(dsaiCourses, roomManager, "DSAI", timestamp);

            System.out.println("\nTimetable generation completed successfully!");
            System.out.println("Output files are located in: " + BASE_OUTPUT_DIR + "/" + timestamp);

        } catch (IOException e) {
            System.err.println("Error creating directory structure: " + e.getMessage());
            return;
        }
    }

    private static void generateCSETimetables(
            Map<Integer, Map<String, List<Course>>> cseCourses, 
            RoomManager roomManager, 
            String timestamp) {
        
        for (Map.Entry<Integer, Map<String, List<Course>>> semesterEntry : cseCourses.entrySet()) {
            int semester = semesterEntry.getKey();
            Map<String, List<Course>> sectionCourses = semesterEntry.getValue();
            
            for (Map.Entry<String, List<Course>> sectionEntry : sectionCourses.entrySet()) {
                generateTimetableForCourses(
                    sectionEntry.getValue(), 
                    semester, 
                    sectionEntry.getKey(), 
                    "CSE", 
                    roomManager,
                    timestamp
                );
            }
        }
    }

    private static void generateDepartmentTimetables(
            Map<Integer, List<Course>> departmentCourses, 
            RoomManager roomManager, 
            String departmentName,
            String timestamp) {
        
        for (Map.Entry<Integer, List<Course>> semesterEntry : departmentCourses.entrySet()) {
            generateTimetableForCourses(
                semesterEntry.getValue(), 
                semesterEntry.getKey(), 
                "", 
                departmentName, 
                roomManager,
                timestamp
            );
        }
    }

    private static void generateTimetableForCourses(
            List<Course> courses, 
            int semester, 
            String section, 
            String department, 
            RoomManager roomManager,
            String timestamp) {
        
        if (courses.isEmpty()) return;

        String sectionStr = section.isEmpty() ? "" : " - SECTION " + section;
        System.out.println("\n" + department + " SEMESTER " + semester + sectionStr + " TIMETABLE");
        System.out.println("=".repeat(160));

        printCourseDetails(courses, section);

        Timetable timetable = new Timetable(courses, section, roomManager, semester);
        timetable.generateTimetable();
        timetable.printTimetable();

        // Export timetable to CSV
        String csvPath = getOutputFilePath(department, semester, section, "csv", timestamp);
        timetable.exportToCSV(csvPath, semester);
        System.out.println("\nTimetable exported to: " + csvPath);

        // Export timetable image
        String imagePath = getOutputFilePath(department, semester, section, "image", timestamp);
        ImageGenerator.generateTimetableImage(
            timetable.getTimeSlots(), 
            AVAILABLE_SLOTS, 
            courses, 
            imagePath, 
            semester, 
            section
        );
        System.out.println("Timetable image exported to: " + imagePath);
    }

    private static void generateDepartmentTimetables(Map<Integer, List<Course>> departmentCourses, 
                                                   RoomManager roomManager, String departmentName) {
        for (Map.Entry<Integer, List<Course>> semesterEntry : departmentCourses.entrySet()) {
            generateTimetableForCourses(semesterEntry.getValue(), semesterEntry.getKey(), "", departmentName, roomManager);
        }
    }

    private static void generateTimetableForCourses(List<Course> courses, int semester, 
                                                  String section, String department, RoomManager roomManager) {
        if (courses.isEmpty()) return;

        String sectionStr = section.isEmpty() ? "" : " - SECTION " + section;
        System.out.println("\n" + department + " SEMESTER " + semester + sectionStr + " TIMETABLE");
        System.out.println("=".repeat(160));

        printCourseDetails(courses, section);

        Timetable timetable = new Timetable(courses, section, roomManager, semester);
        timetable.generateTimetable();
        timetable.printTimetable();

        // Export timetable to CSV and image
        String baseFilename = String.format("timetables/%s_semester_%d%s", 
            department.toLowerCase(), 
            semester, 
            section.isEmpty() ? "" : "_section_" + section
        );
        
        timetable.exportToCSV(baseFilename + ".csv", semester);
        System.out.println("\nTimetable exported to " + baseFilename + ".csv");

        ImageGenerator.generateTimetableImage(timetable.getTimeSlots(), AVAILABLE_SLOTS, 
            courses, baseFilename + ".png", semester, section);
        System.out.println("Timetable image exported to " + baseFilename + ".png");
    }

    private static void printCourseDetails(List<Course> courses, String section) {
        System.out.println("\nCourse Details:");
        System.out.println(String.format("%-10s %-40s %-8s %-12s %-30s %-20s %-15s %-15s %-15s",
            "Code", "Name", "Credits", "L-T-P-S-C", "Faculty", "Department", "Section", "Room", "Lab Room"));
        System.out.println("-".repeat(165));

        for (Course course : courses) {
            System.out.println(String.format("%-10s %-40s %-8d %-12s %-30s %-20s %-15s %-15s %-15s",
                course.code,
                course.name,
                course.credits,
                course.ltpsc,
                course.faculty,
                course.department,
                section,
                course.assignedRoom,
                course.practicalHours > 0 ? course.assignedLabRoom : "N/A"));
        }
        System.out.println();
    }
}

class ImageGenerator {
    private static final Color HEADER_BG = new Color(47, 73, 94);
    private static final Color CELL_BG = new Color(248, 249, 250);
    private static final Color LECTURE_COLOR = new Color(214, 234, 248);
    private static final Color TUTORIAL_COLOR = new Color(255, 243, 205);
    private static final Color LAB_COLOR = new Color(223, 240, 216);
    private static final Color BREAK_COLOR = new Color(232, 232, 232);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color HEADER_TEXT = new Color(255, 255, 255);
    
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font CELL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font LEGEND_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    
    private static final int CELL_HEIGHT = 100;
    private static final int CELL_WIDTH = 200;
    private static final int TIME_COLUMN_WIDTH = 90;
    private static final int LEGEND_HEIGHT = 50;
    private static final int COURSE_DETAIL_HEIGHT_PER_COURSE = 150;
    private static final int LEGEND_PADDING = 80;

    public static void generateTimetableImage(List<TimeSlot> timeSlots, List<TimeBlock> allTimeBlocks, 
                                            List<Course> courses, String outputPath, int semester, String section) {
        allTimeBlocks.sort((a, b) -> Integer.compare(a.startHour * 60 + a.startMinute, b.startHour * 60 + b.startMinute));

        int width = TIME_COLUMN_WIDTH + (CELL_WIDTH * 5);
        int timeTableHeight = CELL_HEIGHT * (allTimeBlocks.size() + 1);
        int coursesHeight = COURSE_DETAIL_HEIGHT_PER_COURSE * courses.size();
        int height = timeTableHeight + LEGEND_HEIGHT + coursesHeight + LEGEND_PADDING;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setColor(CELL_BG);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(HEADER_BG);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 26));
        g2d.fillRect(0, 0, width, CELL_HEIGHT);
        g2d.setColor(HEADER_TEXT);
        String title = String.format("Semester %d • Section %s", semester, section);
        FontMetrics titleMetrics = g2d.getFontMetrics();
        g2d.drawString(title, (width - titleMetrics.stringWidth(title)) / 2, CELL_HEIGHT / 2 + titleMetrics.getHeight() / 4);
        
        g2d.setFont(HEADER_FONT);
        String[] days = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int i = 0; i < days.length; i++) {
            int x = (i == 0) ? 0 : TIME_COLUMN_WIDTH + (i - 1) * CELL_WIDTH;
            int w = (i == 0) ? TIME_COLUMN_WIDTH : CELL_WIDTH;
            
            GradientPaint gradientPaint = new GradientPaint(
                x, CELL_HEIGHT, HEADER_BG,
                x, CELL_HEIGHT * 2, new Color(HEADER_BG.getRed(), HEADER_BG.getGreen(), HEADER_BG.getBlue(), 230)
            );
            g2d.setPaint(gradientPaint);
            g2d.fillRect(x, CELL_HEIGHT, w, CELL_HEIGHT);
            
            g2d.setColor(HEADER_TEXT);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(days[i], x + (w - fm.stringWidth(days[i])) / 2, 
                          CELL_HEIGHT + CELL_HEIGHT / 2 + fm.getHeight() / 4);
        }
        
        g2d.setFont(CELL_FONT);
        for (int blockIndex = 0; blockIndex < allTimeBlocks.size(); blockIndex++) {
            TimeBlock block = allTimeBlocks.get(blockIndex);
            int y = (blockIndex + 2) * CELL_HEIGHT;
            
            g2d.setColor(HEADER_BG);
            g2d.fillRect(0, y, TIME_COLUMN_WIDTH, CELL_HEIGHT);
            g2d.setColor(HEADER_TEXT);
            String time = String.format("%02d:%02d-%02d:%02d", 
                block.startHour, block.startMinute,
                block.endHour, block.endMinute);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(time, (TIME_COLUMN_WIDTH - fm.stringWidth(time)) / 2, 
                          y + CELL_HEIGHT / 2 + fm.getHeight() / 4);
            
            for (int day = 0; day < 5; day++) {
                int x = TIME_COLUMN_WIDTH + day * CELL_WIDTH;
                
                g2d.setColor(CELL_BG);
                g2d.fillRect(x + 1, y + 1, CELL_WIDTH - 2, CELL_HEIGHT - 2);
                
                boolean isBreakTime = isBreakTime(block);
                if (isBreakTime) {
                    g2d.setColor(BREAK_COLOR);
                    g2d.fillRect(x + 2, y + 2, CELL_WIDTH - 4, CELL_HEIGHT - 4);
                    g2d.setColor(TEXT_COLOR);
                    String breakText = (block.startHour == 12 || block.startHour == 13) ? "LUNCH BREAK" : "BREAK";
                    g2d.drawString(breakText, x + (CELL_WIDTH - fm.stringWidth(breakText)) / 2,
                                 y + CELL_HEIGHT / 2 + fm.getHeight() / 4);
                } else {
                    TimeSlot matchingSlot = findMatchingTimeSlot(timeSlots, day, block);
                    
                    if (matchingSlot != null) {
                        g2d.setColor(getSlotColor(matchingSlot.type));
                        g2d.fillRect(x + 2, y + 2, CELL_WIDTH - 4, CELL_HEIGHT - 4);
                        
                        g2d.setColor(TEXT_COLOR);
                        String[] lines = {
                            matchingSlot.courseCode,
                            "(" + matchingSlot.type.toUpperCase() + ")",
                            truncateText(matchingSlot.courseName, CELL_WIDTH - 15, fm),
                            "Room " + matchingSlot.room,
                            truncateText(matchingSlot.faculty, CELL_WIDTH - 15, fm)
                        };
                        
                        int textY = y + 15;
                        for (String line : lines) {
                            if (line != null && !line.isEmpty()) {
                                int textWidth = fm.stringWidth(line);
                                int textX = x + (CELL_WIDTH - textWidth) / 2;
                                g2d.drawString(line, textX, textY);
                                textY += fm.getHeight() + 1;
                            }
                        }
                    }
                }
                
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRect(x, y, CELL_WIDTH, CELL_HEIGHT);
            }
        }
        
        drawLegend(g2d, allTimeBlocks.size(), width);
        drawCourseDetails(g2d, courses, allTimeBlocks.size(), width);
        
        g2d.dispose();
        
        try {
            ImageIO.write(image, "PNG", new File(outputPath));
        } catch (IOException e) {
            System.err.println("Error saving timetable image: " + e.getMessage());
        }
    }
    
    private static boolean isBreakTime(TimeBlock block) {
        return (block.startHour == 12 && block.startMinute == 30) ||
               (block.startHour == 10 && block.startMinute == 30);
    }
    
    private static TimeSlot findMatchingTimeSlot(List<TimeSlot> timeSlots, int day, TimeBlock block) {
        return timeSlots.stream()
            .filter(slot -> slot.day == day && 
                          ((slot.timeBlock.startHour == block.startHour && 
                            slot.timeBlock.startMinute == block.startMinute) ||
                           slot.timeBlock.overlaps(block)))
            .findFirst()
            .orElse(null);
    }
    
    private static String truncateText(String text, int maxWidth, FontMetrics fm) {
        if (text == null || text.isEmpty()) return text;
        if (fm.stringWidth(text) <= maxWidth) return text;
        
        String ellipsis = "...";
        int ellipsisWidth = fm.stringWidth(ellipsis);
        StringBuilder truncated = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            if (fm.stringWidth(truncated.toString() + c + ellipsis) > maxWidth) {
                return truncated.toString() + ellipsis;
            }
            truncated.append(c);
        }
        
        return truncated.toString();
    }

    private static Color getSlotColor(String type) {
        switch (type.toLowerCase()) {
            case "lecture": return LECTURE_COLOR;
            case "tutorial": return TUTORIAL_COLOR;
            case "lab":
            case "practical": return LAB_COLOR;
            default: return CELL_BG;
        }
    }

    private static void drawLegend(Graphics2D g2d, int numTimeBlocks, int width) {
        int legendY = (numTimeBlocks + 2) * CELL_HEIGHT;
        g2d.setFont(LEGEND_FONT);
        
        g2d.setColor(new Color(248, 249, 250));
        g2d.fillRect(0, legendY, width, LEGEND_HEIGHT);
        
        g2d.setColor(HEADER_BG);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2d.drawString("Legend", 20, legendY + 30);
        
        int boxSize = 18;
        int legendX = 120;
        Color[] colors = {LECTURE_COLOR, TUTORIAL_COLOR, LAB_COLOR, BREAK_COLOR};
        String[] labels = {"Lecture", "Tutorial", "Lab/Practical", "Break"};
        
        g2d.setFont(LEGEND_FONT);
        for (int i = 0; i < colors.length; i++) {
            g2d.setColor(colors[i]);
            g2d.fillRect(legendX, legendY + 20, boxSize, boxSize);
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(labels[i], legendX + boxSize + 8, legendY + 34);
            legendX += 160;
        }
    }
    
    private static void drawCourseDetails(Graphics2D g2d, List<Course> courses, int numTimeBlocks, int width) {
        int courseY = (numTimeBlocks + 2) * CELL_HEIGHT + LEGEND_HEIGHT + 20;
        
        // Draw section header
        g2d.setColor(HEADER_BG);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        String headerText = "Course Details";
        FontMetrics headerFm = g2d.getFontMetrics();
        g2d.drawString(headerText, 20, courseY);
        g2d.drawLine(20, courseY + 5, width - 20, courseY + 5);
        courseY += 40;
    
        // Draw each course in a card-like format
        for (Course course : courses) {
            // Draw card background
            g2d.setColor(CELL_BG);
            g2d.fillRoundRect(20, courseY - 25, width - 40, 70, 15, 15);
            
            // Add a subtle border
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawRoundRect(20, courseY - 25, width - 40, 70, 15, 15);
    
            // Course code with colored background
            g2d.setColor(HEADER_BG);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2d.fillRoundRect(35, courseY - 20, 80, 25, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString(course.code, 45, courseY);
    
            // Course name
            g2d.setColor(HEADER_BG);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2d.drawString(course.name, 130, courseY);
            
            // Faculty and department info
            g2d.setColor(TEXT_COLOR);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            // Draw icons (using simple shapes since we can't import images)
            int iconY = courseY + 15;
            
            // Faculty icon
            g2d.setColor(HEADER_BG);
            g2d.fillOval(45, iconY - 8, 12, 12);
            g2d.setColor(TEXT_COLOR);
            g2d.drawString("Faculty: " + course.faculty, 65, iconY);
            
            // Department icon
            g2d.setColor(HEADER_BG);
            g2d.fillRect(245, iconY - 8, 12, 12);
            g2d.setColor(TEXT_COLOR);
            g2d.drawString("Dept: " + course.department, 265, iconY);
            
            // Room info
            g2d.setColor(HEADER_BG);
            int[] xPoints = {445, 451, 457};
            int[] yPoints = {iconY - 8, iconY - 2, iconY - 8};
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setColor(TEXT_COLOR);
            
            String roomInfo = "Room: " + course.assignedRoom;
            if (course.practicalHours > 0) {
                roomInfo += " | Lab: " + course.assignedLabRoom;
            }
            g2d.drawString(roomInfo, 465, iconY);
    
            courseY += 85; // Increased spacing between courses
        }
    }
}