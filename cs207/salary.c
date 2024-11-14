#include "salary.h"
#include "employee.h"
#include "attendance.h"
#include "leave.h"
#include <stdio.h>
#include <string.h>

#define WORKDAYS_PER_MONTH 22

float calculate_deduction(int employee_id) {
    int absent_days = 0;
    for (int i = 0; i < attendance_count; i++) {
        if (attendance_records[i].employee_id == employee_id && attendance_records[i].status == 'A') {
            absent_days++;
        }
    }

    int index = find_employee(employee_id);
    if (index != -1) {
        float daily_rate = employees[index].base_salary / WORKDAYS_PER_MONTH;
        return daily_rate * absent_days;
    }
    return 0;
}

void generate_salary_slip(int employee_id) {
    int index = find_employee(employee_id);
    if (index != -1) {
        float deduction = calculate_deduction(employee_id);
        float net_salary = employees[index].base_salary - deduction;

        printf("\n--- Salary Slip ---\n");
        printf("Employee ID: %d\n", employees[index].id);
        printf("Name: %s\n", employees[index].name);
        printf("Position: %s\n", employees[index].position);
        printf("Base Salary: %.2f\n", employees[index].base_salary);
        printf("Deductions: %.2f\n", deduction);
        printf("Net Salary: %.2f\n", net_salary);
        printf("-------------------\n");
    } else {
        printf("Employee not found.\n");
    }
}