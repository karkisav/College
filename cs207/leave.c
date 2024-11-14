#include "leave.h"
#include <stdio.h>
#include <string.h>

LeaveRecord leave_records[MAX_LEAVE_RECORDS];
int leave_count = 0;

void apply_leave(int employee_id, const char* start_date, const char* end_date, const char* reason) {
    if (leave_count >= MAX_LEAVE_RECORDS) {
        printf("Maximum number of leave records reached.\n");
        return;
    }

    LeaveRecord new_record;
    new_record.employee_id = employee_id;
    strcpy(new_record.start_date, start_date);
    strcpy(new_record.end_date, end_date);
    strcpy(new_record.reason, reason);
    new_record.status = 'P';  // Set initial status as pending

    leave_records[leave_count++] = new_record;
    printf("Leave application submitted successfully.\n");
    save_leave();
}

void view_leave(int employee_id) {
    printf("Leave records for employee ID %d:\n", employee_id);
    for (int i = 0; i < leave_count; i++) {
        if (leave_records[i].employee_id == employee_id) {
            printf("Start Date: %s, End Date: %s, Reason: %s, Status: %c\n",
                   leave_records[i].start_date, leave_records[i].end_date,
                   leave_records[i].reason, leave_records[i].status);
        }
    }
}

void approve_leave(int employee_id, const char* start_date) {
    for (int i = 0; i < leave_count; i++) {
        if (leave_records[i].employee_id == employee_id &&
            strcmp(leave_records[i].start_date, start_date) == 0) {
            leave_records[i].status = 'A';
            printf("Leave approved successfully.\n");
            save_leave();
            return;
        }
    }
    printf("Leave record not found.\n");
}

void save_leave() {
    FILE *file = fopen("leave.dat", "wb");
    if (file == NULL) {
        printf("Error opening file for writing.\n");
        return;
    }
    fwrite(&leave_count, sizeof(int), 1, file);
    fwrite(leave_records, sizeof(LeaveRecord), leave_count, file);
    fclose(file);
}

void load_leave() {
    FILE *file = fopen("leave.dat", "rb");
    if (file == NULL) {
        printf("No existing leave data found.\n");
        return;
    }
    fread(&leave_count, sizeof(int), 1, file);
    fread(leave_records, sizeof(LeaveRecord), leave_count, file);
    fclose(file);
}