CC = gcc
CFLAGS = -Wall -Wextra -std=c99
OBJS = main.o employee.o attendance.o leave.o salary.o user.o

payroll: $(OBJS)
	$(CC) $(CFLAGS) -o payroll $(OBJS)

main.o: main.c employee.h attendance.h leave.h salary.h user.h
	$(CC) $(CFLAGS) -c main.c

employee.o: employee.c employee.h
	$(CC) $(CFLAGS) -c employee.c

attendance.o: attendance.c attendance.h
	$(CC) $(CFLAGS) -c attendance.c

leave.o: leave.c leave.h
	$(CC) $(CFLAGS) -c leave.c

salary.o: salary.c salary.h employee.h attendance.h leave.h
	$(CC) $(CFLAGS) -c salary.c

user.o: user.c user.h employee.h attendance.h leave.h salary.h
	$(CC) $(CFLAGS) -c user.c

clean:
	rm -f payroll $(OBJS)

.PHONY: clean