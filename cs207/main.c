#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "employee.h"
#include "attendance.h"
#include "leave.h"
#include "salary.h"
#include "user.h"

int main() {
    int user_type;
    printf("Welcome to Payroll Management System\n");
    printf("Enter user type (1: Employee, 2: Manager, 3: Admin): ");
    scanf("%d", &user_type);

    switch (user_type) {
        case 1:
            employee_menu();
            break;
        case 2:
            manager_menu();
            break;
        case 3:
            admin_menu();
            break;
        default:
            printf("Invalid user type\n");
    }

    return 0;
}