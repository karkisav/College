import csv
from datetime import datetime
from typing import List, Dict

class Subject:
    def __init__(self, subject_code: str, subject_name: str, ltpsc: str):
        self.subject_code = subject_code
        self.subject_name = subject_name
        self.ltpsc = ltpsc  # Lecture-Tutorial-Practical-Self Study-Credits
        
    def get_details(self) -> Dict:
        return {
            "subject_code": self.subject_code,
            "subject_name": self.subject_name,
            "ltpsc": self.ltpsc
        }

class Faculty:
    def __init__(self, faculty_id: str, name: str, contact_details: str, specializations: List[str]):
        self.faculty_id = faculty_id
        self.name = name
        self.contact_details = contact_details
        self.specializations = specializations
        self.assigned_subjects = []

    def view_timetable(self) -> None:
        print(f"Timetable for {self.name}:")
        for subject in self.assigned_subjects:
            print(f"Subject: {subject.subject_name}")

    def request_for_change(self, timetable, reason: str) -> bool:
        return timetable.request_change(self.faculty_id, reason)

class Batch:
    def __init__(self, name: str, year: int, branch: str, subject_list: List[Subject]):
        self.name_of_batch = name
        self.year = year
        self.branch = branch
        self.subject_list = subject_list
        self.teacher_assigned = {}  # subject_code: faculty_id mapping

    def assign_teacher(self, subject_code: str, faculty: Faculty) -> bool:
        if subject_code not in [s.subject_code for s in self.subject_list]:
            return False
        self.teacher_assigned[subject_code] = faculty.faculty_id
        faculty.assigned_subjects.append(next(s for s in self.subject_list if s.subject_code == subject_code))
        return True

class Student:
    def __init__(self, student_id: str, name: str, contact_details: str, batch: Batch):
        self.student_id = student_id
        self.name = name
        self.contact_details = contact_details
        self.batch = batch

    def view_timetable(self, timetable) -> None:
        timetable.display_batch_schedule(self.batch.name_of_batch)

class Admin:
    def __init__(self, admin_id: str, name: str, contact_details: str):
        self.admin_id = admin_id
        self.name = name
        self.contact_details = contact_details

    def view_requests(self, timetable) -> List[Dict]:
        return timetable.get_change_requests()

    def approve_timetable(self, timetable) -> bool:
        return timetable.set_approved_status(True)

    def deny_or_approve_request(self, timetable, request_id: int, approved: bool) -> bool:
        return timetable.process_change_request(request_id, approved)

class Coordinator:
    def __init__(self, coordinator_id: str, name: str, contact_details: str):
        self.coordinator_id = coordinator_id
        self.name = name
        self.contact_details = contact_details

    def input_room_availability(self, timetable, room_data: Dict) -> None:
        timetable.update_room_availability(room_data)

    def input_number_of_batches(self, timetable, num_batches: int) -> None:
        timetable.number_of_batches = num_batches

    def input_subjects_per_batch(self, timetable, batch_subjects: Dict) -> None:
        timetable.batch_subjects = batch_subjects

    def assign_teacher(self, batch: Batch, subject_code: str, faculty: Faculty) -> bool:
        return batch.assign_teacher(subject_code, faculty)

    def input_time_for_classes_or_labs(self, timetable, schedule_data: Dict) -> None:
        timetable.update_schedule(schedule_data)

class Timetable:
    def __init__(self):
        self.number_of_batches = 0
        self.number_of_students_per_batch = {}
        self.number_of_classrooms = 0
        self.number_of_labs = 0
        self.room_availability = {}
        self.schedule = {}
        self.change_requests = []
        self.is_approved = False
        self.batch_subjects = {}
        self.room_availability = {}

    def update_room_availability(self, room_data: Dict) -> None:
        for room_id, details in room_data.items():
            self.room_availability[room_id] = details
        print("Room availability updated successfully.")

    def assign_room_to_batch(self, batch_name: str, room_id: str) -> bool:
        if room_id in self.room_availability:
            self.schedule[batch_name]["room"] = room_id
            return True
        return False

    def generate_timetable(self) -> bool:
        # Basic timetable generation logic
        if not self.room_availability or not self.batch_subjects:
            return False
        
        # Generate schedule for each batch
        for batch in self.batch_subjects:
            self.schedule[batch] = {
                "Monday": {},
                "Tuesday": {},
                "Wednesday": {},
                "Thursday": {},
                "Friday": {}
            }
        return True

    def export_to_csv(self, filename: str) -> bool:
        try:
            with open(filename, 'w', newline='') as file:
                writer = csv.writer(file)
                # Write header
                writer.writerow(["Batch", "Year", "Branch", "Day", "Time", "Subject", "Faculty", "Room"])
                
                # Write schedule data
                for batch in self.schedule:
                    for day in self.schedule[batch]:
                        for time, details in self.schedule[batch][day].items():
                            writer.writerow([
                                batch,
                                details.get("year", ""),
                                details.get("branch", ""),
                                day,
                                time,
                                details.get("subject", ""),
                                details.get("faculty", ""),
                                details.get("room", "")
                            ])
                return True
        except Exception as e:
            print(f"Error exporting to CSV: {e}")
            return False

    def import_from_csv(self, filename: str) -> bool:
        try:
            self.schedule = {}
            with open(filename, 'r') as file:
                reader = csv.DictReader(file)
                for row in reader:
                    batch = row["Batch"]
                    day = row["Day"]
                    time = row["Time"]
                    
                    if batch not in self.schedule:
                        self.schedule[batch] = {
                            "Monday": {},
                            "Tuesday": {},
                            "Wednesday": {},
                            "Thursday": {},
                            "Friday": {}
                        }
                    
                    self.schedule[batch][day][time] = {
                        "year": row["Year"],
                        "branch": row["Branch"],
                        "subject": row["Subject"],
                        "faculty": row["Faculty"],
                        "room": row["Room"]
                    }
                return True
        except Exception as e:
            print(f"Error importing from CSV: {e}")
            return False

    def display_batch_schedule(self, batch_name: str) -> None:
        if batch_name in self.schedule:
            print(f"\nSchedule for Batch: {batch_name}")
            for day in self.schedule[batch_name]:
                print(f"\n{day}:")
                for time, details in sorted(self.schedule[batch_name][day].items()):
                    print(f"{time}: {details['subject']} (Room: {details['room']}, Faculty: {details['faculty']})")

    def request_change(self, faculty_id: str, reason: str) -> bool:
        request_id = len(self.change_requests) + 1
        self.change_requests.append({
            "id": request_id,
            "faculty_id": faculty_id,
            "reason": reason,
            "status": "pending",
            "timestamp": datetime.now()
        })
        return True

    def process_change_request(self, request_id: int, approved: bool) -> bool:
        for request in self.change_requests:
            if request["id"] == request_id:
                request["status"] = "approved" if approved else "denied"
                return True
        return False

    def get_change_requests(self) -> List[Dict]:
        return self.change_requests

    def set_approved_status(self, status: bool) -> bool:
        self.is_approved = status
        return True

# Example usage
def main():
    # Create a sample timetable system
    timetable = Timetable()
    
    # Create sample data
    subject1 = Subject("CS101", "Introduction to Programming", "3-1-2-1-4")
    subject2 = Subject("CS102", "Data Structures", "3-0-2-1-4")
    
    # Create a batch with year and branch
    batch1 = Batch("Batch2024A", 1, "Computer Science", [subject1, subject2])
    
    # Create faculty
    faculty1 = Faculty("F101", "Dr. Smith", "smith@example.com", ["Programming", "Algorithms"])
    
    # Create coordinator
    coordinator = Coordinator("C101", "Prof. Johnson", "johnson@example.com")
    
    # Assign teacher to subject
    coordinator.assign_teacher(batch1, "CS101", faculty1)
    
    # Input room availability
    coordinator.input_room_availability(timetable, {
        "Room101": {"capacity": 60, "has_projector": True},
        "Lab1": {"capacity": 30, "has_computers": True}
    })
    
    # Generate and export timetable
    timetable.generate_timetable()
    timetable.export_to_csv("timetable.csv")
    
    # Display schedule
    timetable.display_batch_schedule("Batch2024A")

if __name__ == "__main__":
    main()