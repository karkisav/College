#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#define MAX_EMPLOYEES 100

typedef struct {
    int id;
    char name[50];
    char position[50];
    float base_salary;
} Employee;

extern Employee employees[MAX_EMPLOYEES];
extern int employee_count;

void add_employee();
void view_employee(int id);
void edit_employee(int id);
int find_employee(int id);
void save_employees();
void load_employees();

#endif