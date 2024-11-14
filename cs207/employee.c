#include "employee.h"
#include <stdio.h>
#include <string.h>

Employee employees[MAX_EMPLOYEES];
int employee_count = 0;

void add_employee() {
    if (employee_count >= MAX_EMPLOYEES) {
        printf("Maximum number of employees reached.\n");
        return;
    }

    Employee new_employee;
    printf("Enter employee ID: ");
    scanf("%d", &new_employee.id);
    printf("Enter employee name: ");
    scanf("%s", new_employee.name);
    printf("Enter employee position: ");
    scanf("%s", new_employee.position);
    printf("Enter employee base salary: ");
    scanf("%f", &new_employee.base_salary);

    employees[employee_count++] = new_employee;
    printf("Employee added successfully.\n");
    save_employees();
}

void view_employee(int id) {
    int index = find_employee(id);
    if (index != -1) {
        printf("ID: %d\n", employees[index].id);
        printf("Name: %s\n", employees[index].name);
        printf("Position: %s\n", employees[index].position);
        printf("Base Salary: %.2f\n", employees[index].base_salary);
    } else {
        printf("Employee not found.\n");
    }
}

void edit_employee(int id) {
    int index = find_employee(id);
    if (index != -1) {
        printf("Enter new name (or . to keep current): ");
        char new_name[50];
        scanf("%s", new_name);
        if (strcmp(new_name, ".") != 0) {
            strcpy(employees[index].name, new_name);
        }

        printf("Enter new position (or . to keep current): ");
        char new_position[50];
        scanf("%s", new_position);
        if (strcmp(new_position, ".") != 0) {
            strcpy(employees[index].position, new_position);
        }

        printf("Enter new base salary (or -1 to keep current): ");
        float new_salary;
        scanf("%f", &new_salary);
        if (new_salary != -1) {
            employees[index].base_salary = new_salary;
        }

        printf("Employee updated successfully.\n");
        save_employees();
    } else {
        printf("Employee not found.\n");
    }
}

int find_employee(int id) {
    for (int i = 0; i < employee_count; i++) {
        if (employees[i].id == id) {
            return i;
        }
    }
    return -1;
}

void save_employees() {
    FILE *file = fopen("employees.dat", "wb");
    if (file == NULL) {
        printf("Error opening file for writing.\n");
        return;
    }
    fwrite(&employee_count, sizeof(int), 1, file);
    fwrite(employees, sizeof(Employee), employee_count, file);
    fclose(file);
}

void load_employees() {
    FILE *file = fopen("employees.dat", "rb");
    if (file == NULL) {
        printf("No existing employee data found.\n");
        return;
    }
    fread(&employee_count, sizeof(int), 1, file);
    fread(employees, sizeof(Employee), employee_count, file);
    fclose(file);
}