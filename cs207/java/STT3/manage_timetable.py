from timetable_system import *

def main():
    # Initialize system
    timetable = Timetable()
    admin = Admin("A001", "Admin Name", "admin@college.edu")
    coordinator = Coordinator("C001", "Coordinator Name", "coordinator@college.edu")
    
    # Create subjects
    subjects_first_year = [
        Subject("CS101", "Introduction to Programming", "3-1-2-1-4"),
        Subject("MA101", "Engineering Mathematics", "3-1-0-2-4")
    ]
    
    # Create faculty
    faculty = [
        Faculty("F001", "Dr. Smith", "smith@college.edu", ["Programming"]),
        Faculty("F002", "Dr. Johnson", "johnson@college.edu", ["Mathematics"])
    ]
    
    # Create batches
    batches = [
        Batch("CSE-2024-A", 1, "Computer Science", subjects_first_year),
        Batch("CSE-2024-B", 1, "Computer Science", subjects_first_year)
    ]
    
    # Configure rooms
    room_data = {
        "Room101": {"capacity": 60, "has_projector": True},
        "Lab1": {"capacity": 30, "has_computers": True}
    }
    coordinator.input_room_availability(timetable, room_data)
    
    # Assign teachers
    for batch in batches:
        for subject in batch.subject_list:
            suitable_faculty = faculty[0]  # Simplified assignment
            coordinator.assign_teacher(batch, subject.subject_code, suitable_faculty)
    
    # Generate and export
    if timetable.generate_timetable():
        timetable.export_to_csv("timetable.csv")
        
        # Display schedules
        for batch in batches:
            timetable.display_batch_schedule(batch.name_of_batch)

if __name__ == "__main__":
    main()