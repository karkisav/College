import pandas as pd
import random

# Constants
DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
START_TIME = 9
END_TIME = 17.5
BREAK_TIME = 13
SLOT_DURATION = 1

# Input CSV data
data = {
    "Semester": ["Sem 1", "Sem 1", "Sem 1", "Sem 1", "Sem 1", "Sem 1", "Sem 1", "Sem 3", "Sem 3", "Sem 3", "Sem 3", "Sem 3", "Sem 3", "Sem 5", "Sem 5", "Sem 5", "Sem 5", "Sem 5", "Sem 5", "Sem 7", "Sem 7", "Sem 7", "Sem 7", "Sem 7"],
    "Course name": [
        "Statistics", "Introduction to DS and AI", "Open elective I", "Probability",
        "Digital Design", "Problem Solving through Programming", "English Language and Communication",
        "Discrete Mathematics", "OOP", "Computer Architecture", "DAA", "Probability",
        "Industrial Social Psychology", "Statistics for Computer Science", "Computer Networks",
        "Artificial Intelligence", "Graph Theory/Advanced algorithm design", "Open electives",
        "Environmental studies", "Elective 1", "Elective 2", "Elective 3", "Elective 4", "Mini project II"
    ],
    "Sections": [
        "Combined", "Combined", "", "Combined", "Combined", "2 sections", "2 sections",
        "2 sections", "2 sections", "2 sections", "2 sections", "2 sections",
        "2 sections", "2 sections", "2 sections", "2 sections", "2 sections", "",
        "", "", "", "", "", ""
    ]
}

df = pd.DataFrame(data)

# Generate a timetable
def generate_timetable(section):
    timetable = {day: [] for day in DAYS}
    for day in DAYS:
        time = START_TIME
        available_courses = df[df["Sections"].str.contains(section, na=False)]["Course name"].tolist()
        random.shuffle(available_courses)

        for course in available_courses:
            if time == BREAK_TIME:
                time += 1
            if time + SLOT_DURATION > END_TIME:
                break
            start_time = f"{int(time)}:00"
            end_time = f"{int(time + SLOT_DURATION)}:00"
            timetable[day].append((start_time, end_time, course))
            time += SLOT_DURATION
    return timetable

# Generate timetables for Section A and B
section_a_timetable = generate_timetable("Section A")
section_b_timetable = generate_timetable("Section B")

# Display timetables
def display_timetable(timetable, section_name):
    print(f"\nTimetable for {section_name}")
    for day, slots in timetable.items():
        print(f"{day}:")
        for slot in slots:
            print(f"  {slot[0]} - {slot[1]}: {slot[2]}")
    print()

display_timetable(section_a_timetable, "Section A")
display_timetable(section_b_timetable, "Section B")
