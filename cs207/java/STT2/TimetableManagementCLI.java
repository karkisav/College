import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

// Subject class with added fields
class Subject {
    private String subjectCode;
    private String subjectName;
    private int lectureHours;
    private int tutorialHours;
    private int practicalHours;
    private int credits;
    private boolean requiresLab;

    public Subject(String subjectCode, String subjectName, int lectureHours, 
                  int tutorialHours, int practicalHours, int credits, boolean requiresLab) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.lectureHours = lectureHours;
        this.tutorialHours = tutorialHours;
        this.practicalHours = practicalHours;
        this.credits = credits;
        this.requiresLab = requiresLab;
    }

    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public int getLectureHours() { return lectureHours; }
    public int getTutorialHours() { return tutorialHours; }
    public int getPracticalHours() { return practicalHours; }
    public int getCredits() { return credits; }
    public boolean requiresLab() { return requiresLab; }
}

// Enhanced Faculty class
class Faculty {
    private String facultyId;
    private String name;
    private String contactDetails;
    private String email;
    private String department;
    private List<String> specializations;
    private Map<DayOfWeek, List<TimeSlot>> schedule;
    private int maxWeeklyHours;
    private boolean isAvailableForLabs;

    public Faculty(String facultyId, String name, String contactDetails, 
                  String email, String department, int maxWeeklyHours, boolean isAvailableForLabs) {
        this.facultyId = facultyId;
        this.name = name;
        this.contactDetails = contactDetails;
        this.email = email;
        this.department = department;
        this.maxWeeklyHours = maxWeeklyHours;
        this.isAvailableForLabs = isAvailableForLabs;
        this.specializations = new ArrayList<>();
        this.schedule = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new ArrayList<>());
        }
    }

    public String getFacultyId() { return facultyId; }
    public String getName() { return name; }
    public String getContactDetails() { return contactDetails; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public List<String> getSpecializations() { return specializations; }
    public boolean isAvailableForLabs() { return isAvailableForLabs; }
    public int getMaxWeeklyHours() { return maxWeeklyHours; }
    public Map<DayOfWeek, List<TimeSlot>> getSchedule() { return schedule; }

    public void addSpecialization(String specialization) {
        specializations.add(specialization);
    }

    public boolean isAvailableAt(TimeSlot newSlot) {
        return schedule.get(newSlot.getDay()).stream()
            .noneMatch(existingSlot -> 
                newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
                newSlot.getEndTime().isAfter(existingSlot.getStartTime()));
    }

    public void addTimeSlot(TimeSlot slot) {
        schedule.get(slot.getDay()).add(slot);
    }
}

// Enhanced Batch class
class Batch {
    private String nameOfBatch;
    private String department;
    private int semester;
    private int numberOfStudents;
    private List<Subject> subjectList;
    private Map<Subject, Faculty> subjectTeacherMap;
    private Map<DayOfWeek, List<TimeSlot>> schedule;
    private int maxDailyHours;

    public Batch(String nameOfBatch, String department, int semester, 
                int numberOfStudents, int maxDailyHours) {
        this.nameOfBatch = nameOfBatch;
        this.department = department;
        this.semester = semester;
        this.numberOfStudents = numberOfStudents;
        this.maxDailyHours = maxDailyHours;
        this.subjectList = new ArrayList<>();
        this.subjectTeacherMap = new HashMap<>();
        this.schedule = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new ArrayList<>());
        }
    }

    public boolean canAssignTeacher(Subject subject, Faculty faculty) {
        // Check if faculty has enough available hours
        int requiredHours = subject.getLectureHours() + 
                           subject.getTutorialHours() + 
                           subject.getPracticalHours();
                           
        int currentAssignedHours = subjectTeacherMap.entrySet().stream()
            .filter(entry -> entry.getValue().equals(faculty))
            .mapToInt(entry -> {
                Subject s = entry.getKey();
                return s.getLectureHours() + s.getTutorialHours() + s.getPracticalHours();
            })
            .sum();
            
        return currentAssignedHours + requiredHours <= faculty.getMaxWeeklyHours();
    }

    public void assignTeacher(Subject subject, Faculty faculty) {
        if (!canAssignTeacher(subject, faculty)) {
            throw new IllegalStateException(
                "Faculty " + faculty.getName() + 
                " does not have enough available hours for this subject.");
        }
        subjectTeacherMap.put(subject, faculty);
    }

    public String getNameOfBatch() { return nameOfBatch; }
    public String getDepartment() { return department; }
    public int getSemester() { return semester; }
    public int getNumberOfStudents() { return numberOfStudents; }
    public List<Subject> getSubjectList() { return subjectList; }
    public Map<Subject, Faculty> getSubjectTeacherMap() { return subjectTeacherMap; }
    public Map<DayOfWeek, List<TimeSlot>> getSchedule() { return schedule; }
    public int getMaxDailyHours() { return maxDailyHours; }

    public void addTimeSlot(TimeSlot slot) {
        schedule.get(slot.getDay()).add(slot);
    }

    public boolean canAddTimeSlot(TimeSlot newSlot) {
        List<TimeSlot> daySchedule = schedule.get(newSlot.getDay());
        
        // Check for time conflicts
        boolean hasConflict = daySchedule.stream()
            .anyMatch(existingSlot -> 
                newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
                newSlot.getEndTime().isAfter(existingSlot.getStartTime()));
        
        if (hasConflict) return false;

        // Check daily hours limit
        Duration totalDuration = daySchedule.stream()
            .map(slot -> Duration.between(slot.getStartTime(), slot.getEndTime()))
            .reduce(Duration.ZERO, Duration::plus);
        
        Duration newSlotDuration = Duration.between(newSlot.getStartTime(), newSlot.getEndTime());
        
        return totalDuration.plus(newSlotDuration).toHours() <= maxDailyHours;
    }
}

// Enhanced TimeSlot class
class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek day;
    private Subject subject;
    private Faculty faculty;
    private String room;
    private String slotType; // "Lecture", "Tutorial", "Practical"
    private int weekNumber; // For alternate week scheduling
    private String notes;

    public TimeSlot(LocalTime startTime, LocalTime endTime, DayOfWeek day, 
                   Subject subject, Faculty faculty, String room, 
                   String slotType, int weekNumber, String notes) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.subject = subject;
        this.faculty = faculty;
        this.room = room;
        this.slotType = slotType;
        this.weekNumber = weekNumber;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return String.format("%s %s-%s: %s (%s) - %s - Room %s%s%s", 
            day,
            startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            subject.getSubjectName(),
            faculty.getName(),
            slotType,
            room,
            weekNumber > 0 ? " Week " + weekNumber : "",
            notes != null && !notes.isEmpty() ? " (" + notes + ")" : "");
    }

    // Getters
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public DayOfWeek getDay() { return day; }
    public Subject getSubject() { return subject; }
    public Faculty getFaculty() { return faculty; }
    public String getRoom() { return room; }
    public String getSlotType() { return slotType; }
    public int getWeekNumber() { return weekNumber; }
    public String getNotes() { return notes; }
}

// Enhanced Timetable class
class Timetable {
    private Map<String, Room> rooms;
    private Map<String, List<TimeSlot>> schedule;
    private List<TimeSlot> allTimeSlots;
    private LocalTime firstSlot;
    private LocalTime lastSlot;
    private int slotDurationMinutes;
    private Set<DayOfWeek> workingDays;

    public Timetable(LocalTime firstSlot, LocalTime lastSlot, int slotDurationMinutes) {
        this.firstSlot = firstSlot;
        this.lastSlot = lastSlot;
        this.slotDurationMinutes = slotDurationMinutes;
        this.rooms = new HashMap<>();
        this.schedule = new HashMap<>();
        this.allTimeSlots = new ArrayList<>();
        this.workingDays = EnumSet.of(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        );
    }

    public Optional<Room> findAvailableRoom(LocalDateTime startTime, 
                                          LocalDateTime endTime, 
                                          int requiredCapacity, 
                                          Set<String> requiredFacilities) {
        return rooms.stream()
            .filter(room -> room.getCapacity() >= requiredCapacity)
            .filter(room -> room.hasFacilities(requiredFacilities))
            .filter(room -> isRoomAvailable(room, startTime, endTime))
            .max(Comparator.comparingInt(room -> {
                // Prefer rooms that are closer to required capacity to avoid waste
                int capacityDiff = room.getCapacity() - requiredCapacity;
                // Penalize rooms that are much larger than needed
                if (capacityDiff > 50) {
                    return -capacityDiff;
                }
                return -Math.abs(capacityDiff);
            }));
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomId(), room);
        schedule.put(room.getRoomId(), new ArrayList<>());
    }

    public boolean isRoomAvailable(String roomId, TimeSlot newSlot) {
        if (!rooms.containsKey(roomId)) return false;
        
        Room room = rooms.get(roomId);
        if (newSlot.getSubject().requiresLab() && !room.isLab()) return false;
        if (!room.isLab() && room.getCapacity() < getBatchSize(newSlot)) return false;
        
        return schedule.get(roomId).stream()
            .noneMatch(existingSlot -> 
                newSlot.getDay() == existingSlot.getDay() &&
                newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
                newSlot.getEndTime().isAfter(existingSlot.getStartTime()));
    }

    private int getBatchSize(TimeSlot slot) {
        // This would need to be implemented based on your batch tracking
        return 40; // Default value
    }

    public void addTimeSlot(TimeSlot slot) {
        if (!isRoomAvailable(slot.getRoom(), slot)) {
            throw new IllegalStateException("Room " + slot.getRoom() + " is not available for this slot");
        }
        
        allTimeSlots.add(slot);
        schedule.get(slot.getRoom()).add(slot);
    }

    public List<TimeSlot> getAllTimeSlots() {
        return allTimeSlots;
    }

    public void exportToCSV(String filename) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            // Write metadata
            writer.writeComment("Timetable Export - Generated on " + 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            // Write different sections
            exportBasicSchedule(writer);
            writer.writeEmptyLine();
            exportRoomWiseSchedule(writer);
            writer.writeEmptyLine();
            exportFacultySchedule(writer);
            writer.writeEmptyLine();
            exportBatchWiseSchedule(writer);
        }
    }

    private void exportBasicSchedule(CSVWriter writer) {
        writer.writeSection("Basic Schedule");
        writer.writeHeader("Day", "Time", "Subject", "Faculty", "Room", "Type", "Week", "Notes");
        
        allTimeSlots.stream()
            .sorted(Comparator
                .comparing(TimeSlot::getDay)
                .thenComparing(TimeSlot::getStartTime))
            .forEach(slot -> writer.writeRow(
                slot.getDay().toString(),
                formatTimeRange(slot.getStartTime(), slot.getEndTime()),
                slot.getSubject().getSubjectName(),
                slot.getFaculty().getName(),
                slot.getRoom(),
                slot.getSlotType(),
                slot.getWeekNumber() > 0 ? String.valueOf(slot.getWeekNumber()) : "",
                slot.getNotes()
            ));
    }

    private void exportRoomWiseSchedule(CSVWriter writer) {
        writer.writeSection("Room-wise Schedule");
        
        for (String roomId : rooms.keySet()) {
            writer.writeSubHeader("Room: " + roomId);
            writer.writeHeader("Day", "Time", "Subject", "Faculty", "Type");
            
            schedule.get(roomId).stream()
                .sorted(Comparator
                    .comparing(TimeSlot::getDay)
                    .thenComparing(TimeSlot::getStartTime))
                .forEach(slot -> writer.writeRow(
                    slot.getDay().toString(),
                    formatTimeRange(slot.getStartTime(), slot.getEndTime()),
                    slot.getSubject().getSubjectName(),
                    slot.getFaculty().getName(),
                    slot.getSlotType()
                ));
            
            writer.writeEmptyLine();
        }
    }

    private void exportFacultySchedule(CSVWriter writer) {
        writer.writeSection("Faculty-wise Schedule");
        
        Map<Faculty, List<TimeSlot>> facultySchedule = allTimeSlots.stream()
            .collect(Collectors.groupingBy(TimeSlot::getFaculty));
        
        for (Map.Entry<Faculty, List<TimeSlot>> entry : facultySchedule.entrySet()) {
            writer.writeSubHeader("Faculty: " + entry.getKey().getName());
            writer.writeHeader("Day", "Time", "Subject", "Room", "Type");
            
            entry.getValue().stream()
                .sorted(Comparator
                    .comparing(TimeSlot::getDay)
                    .thenComparing(TimeSlot::getStartTime))
                .forEach(slot -> writer.writeRow(
                    slot.getDay().toString(),
                    formatTimeRange(slot.getStartTime(), slot.getEndTime()),
                    slot.getSubject().getSubjectName(),
                    slot.getRoom(),
                    slot.getSlotType()
                ));
            
            writer.writeEmptyLine();
        }
    }

    private void exportBatchWiseSchedule(CSVWriter writer) {
        // Implementation would depend on how batches are tracked
        // Similar structure to faculty schedule
    }

    private String formatTimeRange(LocalTime start, LocalTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + "-" + end.format(formatter);
    }

    public void generateTimeSlots(List<Batch> batches) {
        for (Batch batch : batches) {
            for (Map.Entry<Subject, Faculty> entry : batch.getSubjectTeacherMap().entrySet()) {
                Subject subject = entry.getKey();
                Faculty faculty = entry.getValue();
                
                // Generate slots for lectures
                generateSlotsForType(batch, subject, faculty, "Lecture", 
                                   subject.getLectureHours());
                
                // Generate slots for tutorials
                generateSlotsForType(batch, subject, faculty, "Tutorial", 
                                   subject.getTutorialHours());
                
                // Generate slots for practicals
                if (subject.requiresLab()) {
                    generateSlotsForType(batch, subject, faculty, "Practical", 
                                       subject.getPracticalHours());
                }
            }
        }
    }
    
    private void generateSlotsForType(Batch batch, Subject subject, Faculty faculty, 
                                    String type, int hours) {
        int remainingHours = hours;
        while (remainingHours > 0) {
            TimeSlot slot = findAvailableSlot(batch, faculty, subject, type);
            if (slot == null) {
                throw new IllegalStateException(
                    "Unable to find suitable slot for " + subject.getSubjectName() + 
                    " " + type);
            }
            addTimeSlot(slot);
            remainingHours--;
        }
    }
    
    private TimeSlot findAvailableSlot(Batch batch, Faculty faculty, 
                                     Subject subject, String type) {
        for (DayOfWeek day : workingDays) {
            LocalTime time = firstSlot;
            while (time.plusMinutes(slotDurationMinutes).isBefore(lastSlot)) {
                // Create potential time slot
                TimeSlot slot = new TimeSlot(
                    time, 
                    time.plusMinutes(slotDurationMinutes),
                    day,
                    subject,
                    faculty,
                    findAvailableRoom(subject, time, day),
                    type,
                    0,
                    ""
                );
                
                // Check if slot is valid
                if (isValidTimeSlot(slot, batch, faculty)) {
                    return slot;
                }
                
                time = time.plusMinutes(slotDurationMinutes);
            }
        }
        return null;
    }
    
    private boolean isValidTimeSlot(TimeSlot slot, Batch batch, Faculty faculty) {
        // Check room availability
        if (!isRoomAvailable(slot.getRoom(), slot)) {
            return false;
        }
        
        // Check faculty availability
        if (!faculty.isAvailableAt(slot)) {
            return false;
        }
        
        // Check batch availability
        if (!batch.canAddTimeSlot(slot)) {
            return false;
        }
        
        return true;
    }
    
    private String findAvailableRoom(Subject subject, LocalTime time, DayOfWeek day) {
        return rooms.keySet().stream()
            .filter(roomId -> {
                Room room = rooms.get(roomId);
                return (!subject.requiresLab() || room.isLab()) &&
                       isRoomAvailable(roomId, new TimeSlot(
                           time,
                           time.plusMinutes(slotDurationMinutes),
                           day,
                           subject,
                           null,
                           roomId,
                           "",
                           0,
                           ""
                       ));
            })
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No available room found"));
    }
}

// New Room class
class Room {
    private String roomId;
    private boolean isLab;
    private int capacity;
    private List<String> equipment;
    private boolean hasProjector;
// Room class (continued)
public Room(String roomId, boolean isLab, int capacity, boolean hasProjector) {
    this.roomId = roomId;
    this.isLab = isLab;
    this.capacity = capacity;
    this.hasProjector = hasProjector;
    this.equipment = new ArrayList<>();
}

public String getRoomId() { return roomId; }
public boolean isLab() { return isLab; }
public int getCapacity() { return capacity; }
public boolean hasProjector() { return hasProjector; }
public List<String> getEquipment() { return equipment; }

public void addEquipment(String equipment) {
    this.equipment.add(equipment);
}
}

// CSV Writer utility class
class CSVWriter implements AutoCloseable {
private final PrintWriter writer;
private static final String DELIMITER = ",";

public CSVWriter(Writer writer) {
    this.writer = new PrintWriter(writer);
}

public void writeComment(String comment) {
    writer.println("# " + comment);
}

public void writeSection(String sectionName) {
    writer.println("\n=== " + sectionName + " ===");
}

public void writeSubHeader(String subHeader) {
    writer.println("\n" + subHeader);
}

public void writeHeader(String... headers) {
    writeRow(headers);
}

public void writeRow(String... values) {
    writer.println(formatCSVRow(values));
}

public void writeEmptyLine() {
    writer.println();
}

private String formatCSVRow(String... values) {
    return Arrays.stream(values)
        .map(this::escapeCsvValue)
        .collect(Collectors.joining(DELIMITER));
}

private String escapeCsvValue(String value) {
    if (value == null) return "";
    if (value.contains(DELIMITER) || value.contains("\"") || value.contains("\n")) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
}

@Override
public void close() {
    writer.close();
}
}

// Enhanced Main CLI class
public class TimetableManagementCLI {
private static final Scanner scanner = new Scanner(System.in);
private static List<Batch> batches = new ArrayList<>();
private static List<Faculty> faculty = new ArrayList<>();
private static List<Subject> subjects = new ArrayList<>();
private static Timetable timetable;
private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

public static void main(String[] args) {
    while (true) {
        printMainMenu();
        int choice = getIntInput("Enter your choice: ");
        
        try {
            switch (choice) {
                case 1: inputBasicDetails(); break;
                case 2: manageRooms(); break;
                case 3: manageSubjects(); break;
                case 4: manageFaculty(); break;
                case 5: manageBatches(); break;
                case 6: assignTeachersToBatches(); break;
                case 7: generateTimetable(); break;
                case 8: viewTimetable(); break;
                case 9: exportTimetable(); break;
                case 10: System.out.println("Thank you for using Timetable Management System!"); return;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }
}

private static void printMainMenu() {
    System.out.println("\n=== Timetable Management System ===");
    System.out.println("1. Input Basic Details (Working Hours, Days)");
    System.out.println("2. Manage Rooms");
    System.out.println("3. Manage Subjects");
    System.out.println("4. Manage Faculty");
    System.out.println("5. Manage Batches");
    System.out.println("6. Assign Teachers to Batches");
    System.out.println("7. Generate Timetable");
    System.out.println("8. View Timetable");
    System.out.println("9. Export Timetable");
    System.out.println("10. Exit");
}

private static void inputBasicDetails() {
    System.out.println("\n=== Input Basic Details ===");
    
    System.out.print("Enter first slot time (HH:mm): ");
    LocalTime firstSlot = LocalTime.parse(scanner.nextLine(), timeFormatter);
    
    System.out.print("Enter last slot time (HH:mm): ");
    LocalTime lastSlot = LocalTime.parse(scanner.nextLine(), timeFormatter);
    
    int slotDuration = getIntInput("Enter slot duration (minutes): ");
    
    timetable = new Timetable(firstSlot, lastSlot, slotDuration);
    System.out.println("Basic details set successfully!");
}

private static void manageRooms() {
    while (true) {
        System.out.println("\n=== Manage Rooms ===");
        System.out.println("1. Add Room");
        System.out.println("2. View Rooms");
        System.out.println("3. Add Equipment to Room");
        System.out.println("4. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        if (choice == 4) break;
        
        switch (choice) {
            case 1: addRoom(); break;
            case 2: viewRooms(); break;
            case 3: addEquipmentToRoom(); break;
            default: System.out.println("Invalid choice!");
        }
    }
}

private static void addRoom() {
    System.out.print("Enter room ID: ");
    String roomId = scanner.nextLine();
    
    boolean isLab = getBooleanInput("Is this a lab? (y/n): ");
    int capacity = getIntInput("Enter room capacity: ");
    boolean hasProjector = getBooleanInput("Does it have a projector? (y/n): ");
    
    Room room = new Room(roomId, isLab, capacity, hasProjector);
    timetable.addRoom(room);
    System.out.println("Room added successfully!");
}

private static void viewRooms() {
    // Implementation for viewing rooms
}

private static void addEquipmentToRoom() {
    // Implementation for adding equipment to rooms
}

private static void manageSubjects() {
    while (true) {
        System.out.println("\n=== Manage Subjects ===");
        System.out.println("1. Add Subject");
        System.out.println("2. View Subjects");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        if (choice == 3) break;
        
        switch (choice) {
            case 1: addSubject(); break;
            case 2: viewSubjects(); break;
            default: System.out.println("Invalid choice!");
        }
    }
}

private static void addSubject() {
    System.out.print("Enter subject code: ");
    String code = scanner.nextLine();
    
    System.out.print("Enter subject name: ");
    String name = scanner.nextLine();
    
    int lectureHours = getIntInput("Enter lecture hours per week: ");
    int tutorialHours = getIntInput("Enter tutorial hours per week: ");
    int practicalHours = getIntInput("Enter practical hours per week: ");
    int credits = getIntInput("Enter credits: ");
    boolean requiresLab = getBooleanInput("Does this subject require lab? (y/n): ");
    
    subjects.add(new Subject(code, name, lectureHours, tutorialHours, 
                           practicalHours, credits, requiresLab));
    System.out.println("Subject added successfully!");
}

private static void viewSubjects() {
    // Implementation for viewing subjects
}

private static void manageFaculty() {
    // Similar implementation to manageRooms() and manageSubjects()
}

private static void manageBatches() {
    // Similar implementation to manageRooms() and manageSubjects()
}

private static void assignTeachersToBatches() {
    if (batches.isEmpty() || faculty.isEmpty() || subjects.isEmpty()) {
        System.out.println("Please add batches, faculty, and subjects first!");
        return;
    }

    System.out.println("\n=== Assign Teachers to Batches ===");
    
    // Select batch
    System.out.println("\nAvailable Batches:");
    for (int i = 0; i < batches.size(); i++) {
        System.out.println((i + 1) + ". " + batches.get(i).getNameOfBatch());
    }
    int batchIndex = getIntInput("Select batch (1-" + batches.size() + "): ") - 1;
    Batch selectedBatch = batches.get(batchIndex);

    // For each subject in the batch
    for (Subject subject : selectedBatch.getSubjectList()) {
        System.out.println("\nAssigning teacher for: " + subject.getSubjectName());
        
        // Filter eligible faculty based on specialization and lab requirements
        List<Faculty> eligibleFaculty = faculty.stream()
            .filter(f -> f.getSpecializations().contains(subject.getSubjectCode()) ||
                        f.getSpecializations().contains(subject.getSubjectName()))
            .filter(f -> !subject.requiresLab() || f.isAvailableForLabs())
            .collect(Collectors.toList());

        if (eligibleFaculty.isEmpty()) {
            System.out.println("No eligible faculty found for " + subject.getSubjectName());
            continue;
        }

        System.out.println("Eligible Faculty:");
        for (int i = 0; i < eligibleFaculty.size(); i++) {
            Faculty f = eligibleFaculty.get(i);
            System.out.println((i + 1) + ". " + f.getName() + 
                " (" + f.getSpecializations() + ")");
        }

        int facultyIndex = getIntInput("Select faculty (1-" + eligibleFaculty.size() + "): ") - 1;
        selectedBatch.assignTeacher(subject, eligibleFaculty.get(facultyIndex));
        System.out.println("Teacher assigned successfully!");
    }
}

private static void generateTimetable() {
    if (timetable == null || batches.isEmpty() || faculty.isEmpty()) {
        System.out.println("Please input all required details first!");
        return;
    }
    
    System.out.println("\nGenerating timetable with constraints...");
    try {
        timetable.generateTimeSlots(batches);
        System.out.println("Timetable generated successfully!");
    } catch (IllegalStateException e) {
        System.out.println("Failed to generate timetable: " + e.getMessage());
    }
}

private static void generateTimeSlots() {
    // Implementation for generating time slots with constraints
}

private static void viewTimetable() {
    if (timetable == null) {
        System.out.println("Please generate timetable first!");
        return;
    }
    
    while (true) {
        System.out.println("\n=== View Timetable ===");
        System.out.println("1. View Complete Schedule");
        System.out.println("2. View Room-wise Schedule");
        System.out.println("3. View Faculty-wise Schedule");
        System.out.println("4. View Batch-wise Schedule");
        System.out.println("5. Back to Main Menu");
        
        int choice = getIntInput("Enter choice: ");
        if (choice == 5) break;
        
        switch (choice) {
            case 1: viewCompleteSchedule(); break;
            case 2: viewRoomSchedule(); break;
            case 3: viewFacultySchedule(); break;
            case 4: viewBatchSchedule(); break;
            default: System.out.println("Invalid choice!");
        }
    }
}

private static void viewCompleteSchedule() {
    System.out.println("\n=== Complete Schedule ===");
    timetable.getAllTimeSlots().stream()
        .sorted(Comparator
            .comparing(TimeSlot::getDay)
            .thenComparing(TimeSlot::getStartTime))
        .forEach(System.out::println);
}

private static void viewRoomSchedule() {
    System.out.print("Enter room ID: ");
    String roomId = scanner.nextLine();
    // Implementation for viewing room schedule
}

private static void viewFacultySchedule() {
    // Implementation for viewing faculty schedule
}

private static void viewBatchSchedule() {
    // Implementation for viewing batch schedule
}

private static void exportTimetable() {
    if (timetable == null) {
        System.out.println("Please generate timetable first!");
        return;
    }

    System.out.print("Enter filename to export (e.g., timetable.csv): ");
    String filename = scanner.nextLine();

    try {
        timetable.exportToCSV(filename);
        System.out.println("Timetable exported successfully to " + filename);
    } catch (IOException e) {
        System.out.println("Error exporting to CSV: " + e.getMessage());
    }
}

private static int getIntInput(String prompt) {
    while (true) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
}

private static boolean getBooleanInput(String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("y") || input.equals("yes")) return true;
        if (input.equals("n") || input.equals("no")) return false;
        System.out.println("Please enter y/n!");
    }
}
}