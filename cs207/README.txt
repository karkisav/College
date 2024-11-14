**Roll Number:** 23bcs118
**Name:** Saurav Karki

===============================================================================
Description
===============================================================================
This Payroll Management System is a C-based application designed to handle 
various aspects of employee management, including attendance tracking, leave 
management, and salary calculation. The system supports three types of users: 
employees, managers, and administrators, each with different levels of access 
and functionality.

===============================================================================
Features
===============================================================================
- Employee information management
- Attendance tracking
- Leave application and approval
- Salary slip generation with deduction calculation
- Different user roles (Employee, Manager, Admin) with appropriate access levels

===============================================================================
Files in the Project
===============================================================================
- main.c:           Entry point of the program
- employee.c/h:     Employee data management
- attendance.c/h:   Attendance record handling
- leave.c/h:        Leave application and management
- salary.c/h:       Salary calculation and slip generation
- user.c/h:         User interface and menu systems
- Makefile:         For easy compilation and management of the project

===============================================================================
How to Compile and Run
===============================================================================
1. Ensure you have GCC installed on your system.
2. Open a terminal and navigate to the project directory.
3. Run the following command to compile the project:
   ---------------------------------------------------------
   |   make                                                 |
   ---------------------------------------------------------
4. Once compilation is successful, run the program using:
   ---------------------------------------------------------
   |   ./payroll                                            |
   ---------------------------------------------------------
5. Follow the on-screen prompts to use the system.

===============================================================================
Cleaning Up
===============================================================================
To remove all compiled files and start fresh, run:
---------------------------------------------------------
|   make clean                                          |
---------------------------------------------------------

===============================================================================
Usage
===============================================================================
1. When you start the program, you'll be prompted to select a user type 
   (Employee, Manager, or Admin).

2. Based on your selection, you'll see different menu options:
   - Employees can view their information, mark attendance, apply for leave, 
     and view their salary slip.
   - Managers can view employee information and attendance, and approve leave 
     applications.
   - Admins have full access to add and edit employee information, as well as 
     all manager functionalities.

3. Follow the on-screen prompts to navigate through the system and perform 
   desired actions.

===============================================================================
Data Persistence
===============================================================================
The system uses file I/O to store data persistently. Employee information, 
attendance records, and leave applications are saved to and loaded from files, 
ensuring data is retained between program executions.

===============================================================================
Note
===============================================================================
This is a basic implementation and may require further enhancements for real-
world use, such as improved security measures, more robust error handling, and 
additional features like reporting and data analytics.
_______________________________________________________________________________
