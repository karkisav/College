#include "user.h"
#include <stdio.h>
#include "employee.h"
#include "attendance.h"
#include "leave.h"
#include "salary.h"

void employee_menu() {
    int choice, id;
    char date[11], status;
    char start_date[11], end_date[11], reason[100];

    printf("Enter your employee ID: ");
    scanf("%d", &id);

    while (1) {
        printf("\nEmployee Menu:\n");
        printf("1. View My Information\n");
        printf("2. Mark Attendance\n");
        printf("3. Apply for Leave\n");
        printf("4. View My Attendance\n");
        printf("5. View My Leave\n");
        printf("6. View Salary Slip\n");
        printf("7. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                view_employee(id);
                break;
            case 2:
                printf("Enter date (YYYY-MM-DD): ");
                scanf("%s", date);
                printf("Enter status (P/A): ");
                scanf(" %c", &status);
                mark_attendance(id, date, status);
                break;
            case 3:
                printf("Enter start date (YYYY-MM-DD): ");
                scanf("%s", start_date);
                printf("Enter end date (YYYY-MM-DD): ");
                scanf("%s", end_date);
                printf("Enter reason: ");
                scanf("%s", reason);
                apply_leave(id, start_date, end_date, reason);
                break;
            case 4:
                view_attendance(id);
                break;
            case 5:
                view_leave(id);
                break;
            case 6:
                generate_salary_slip(id);
                break;
            case 7:
                return;
            default:
                printf("Invalid choice. Please try again.\n");
        }
    }
}

void manager_menu() {
    int choice, id;
    char start_date[11];

    while (1) {
        printf("\nManager Menu:\n");
        printf("1. View Employee Information\n");
        printf("2. View Employee Attendance\n");
        printf("3. View Employee Leave\n");
        printf("4. Approve Leave\n");
        printf("5. Generate Salary Slip\n");
        printf("6. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_employee(id);
                break;
            case 2:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_attendance(id);
                break;
            case 3:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_leave(id);
                break;
            case 4:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                printf("Enter leave start date (YYYY-MM-DD): ");
                scanf("%s", start_date);
                approve_leave(id, start_date);
                break;
            case 5:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                generate_salary_slip(id);
                break;
            case 6:
                return;
            default:
                printf("Invalid choice. Please try again.\n");
        }
    }
}

void admin_menu() {
    int choice, id;

    while (1) {
        printf("\nAdmin Menu:\n");
        printf("1. Add Employee\n");
        printf("2. View Employee Information\n");
        printf("3. Edit Employee Information\n");
        printf("4. View Employee Attendance\n");
        printf("5. View Employee Leave\n");
        printf("6. Approve Leave\n");
        printf("7. Generate Salary Slip\n");
        printf("8. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                add_employee();
                break;
            case 2:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_employee(id);
                break;
            case 3:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                edit_employee(id);
                break;
            case 4:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_attendance(id);
                break;
            case 5:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                view_leave(id);
                break;
            case 6:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                char start_date[11];
                printf("Enter leave start date (YYYY-MM-DD): ");
                scanf("%s", start_date);
                approve_leave(id, start_date);
                break;
            case 7:
                printf("Enter employee ID: ");
                scanf("%d", &id);
                generate_salary_slip(id);
                break;
            case 8:
                return;
            default:
                printf("Invalid choice. Please try again.\n");
        }
    }
}

