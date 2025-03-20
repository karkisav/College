# Timetable Generator

This Java program generates timetables based on course data provided in CSV files for different departments: Computer Science (CSE), Data Science & Artificial Intelligence 
(DSAI), and Electrical Engineering (ECE). The program reads the input CSV files, processes the course information, and outputs a folder containing the generated timetables as CSV 
files.

## Prerequisites

- Java Development Kit (JDK) 1.8 or later installed.
- A text editor or an Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or NetBeans to view the code.

## Setup Instructions

1. **Clone the Repository:**
   If you have cloned this repository, navigate to the directory where the program is located.

   ```bash
   cd path/to/timetable-generator
   ```

2. **Compile the Java Program:**
   Ensure that all your Java files are in the same directory or update your build paths accordingly. Compile the Java source files using `javac`.

   ```bash
   javac TimetableGenerator.java
   ```

## Running the Timetable Generator

The program requires three command-line arguments corresponding to the CSV files for CSE, DSAI, and ECE courses.

```bash
java TimetableGenerator cse_courses.csv dsai_courses.csv ece_courses.csv
```

### Command-Line Arguments

1. **cse_courses.csv** - The path to the CSV file containing Computer Science courses.
2. **dsai_courses.csv** - The path to the CSV file containing Data Science & Artificial Intelligence courses.
3.i **ece_courses.csv** - The path to the CSV file containing Electrical Engineering courses.

### Output

After running the program, it will generate a folder named `timetables` in the current working directory. Inside this folder, you will find individual timetable files for each 
department saved as CSV files.