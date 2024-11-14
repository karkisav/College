#ifndef LEAVE_H
#define LEAVE_H

#define MAX_LEAVE_RECORDS 500

typedef struct {
    int employee_id;
    char start_date[11];
    char end_date[11];
    char reason[100];
    char status;  // 'P' for pending, 'A' for approved, 'R' for rejected
} LeaveRecord;

extern LeaveRecord leave_records[MAX_LEAVE_RECORDS];
extern int leave_count;

void apply_leave(int employee_id, const char* start_date, const char* end_date, const char* reason);
void view_leave(int employee_id);
void approve_leave(int employee_id, const char* start_date);
void save_leave();
void load_leave();

#endif