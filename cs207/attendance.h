#ifndef ATTENDANCE_H
#define ATTENDANCE_H

#define MAX_ATTENDANCE_RECORDS 1000

typedef struct {
    int employee_id;
    char date[11];
    char status;  // 'P' for present, 'A' for absent
} AttendanceRecord;

extern AttendanceRecord attendance_records[MAX_ATTENDANCE_RECORDS];
extern int attendance_count;

void mark_attendance(int employee_id, const char* date, char status);
void view_attendance(int employee_id);
void save_attendance();
void load_attendance();

#endif