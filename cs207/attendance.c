#include "attendance.h"
#include <stdio.h>
#include <string.h>

AttendanceRecord attendance_records[MAX_ATTENDANCE_RECORDS];
int attendance_count = 0;

void mark_attendance(int employee_id, const char* date, char status) {
    if (attendance_count >= MAX_ATTENDANCE_RECORDS) {
        printf("Maximum number of attendance records reached.\n");
        return;
    }

    AttendanceRecord new_record;
    new_record.employee_id = employee_id;
    strcpy(new_record.date, date);
    new_record.status = status;

    attendance_records[attendance_count++] = new_record;
    printf("Attendance marked successfully.\n");
    save_attendance();
}

void view_attendance(int employee_id) {
    printf("Attendance records for employee ID %d:\n", employee_id);
    for (int i = 0; i < attendance_count; i++) {
        if (attendance_records[i].employee_id == employee_id) {
            printf("Date: %s, Status: %c\n", attendance_records[i].date, attendance_records[i].status);
        }
    }
}

void save_attendance() {
    FILE *file = fopen("attendance.dat", "wb");
    if (file == NULL) {
        printf("Error opening file for writing.\n");
        return;
    }
    fwrite(&attendance_count, sizeof(int), 1, file);
    fwrite(attendance_records, sizeof(AttendanceRecord), attendance_count, file);
    fclose(file);
}

void load_attendance() {
    FILE *file = fopen("attendance.dat", "rb");
    if (file == NULL) {
        printf("No existing attendance data found.\n");
        return;
    }
    fread(&attendance_count, sizeof(int), 1, file);
    fread(attendance_records, sizeof(AttendanceRecord), attendance_count, file);
    fclose(file);
}