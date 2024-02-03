/*Write a C program to illustrate the Stack operations such as
Push, Pop, and Display. The program should be menu driven.*/

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

void push(int number);
void pop(int number);
void display();

int main(void)
{
    int choice;
    int array[100];
    do
    {
        printf("Enter the serial number of to execute the following tasks\n");
        printf("1. Push\n");
        printf("2. Pop\n");
        printf("3. Display\n");
        printf("4. Exit\n")

        scanf("%d", &choice);
        switch (choice)
        {
            case '1':
                int number;
                printf("what number do you want to push: ")
                scanf("%d", &number);
                push(number);
        }
    } while (choice != 4);
    return 0;
    
}