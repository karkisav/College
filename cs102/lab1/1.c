/*Write a C program to illustrate the Stack operations such as
Push, Pop, and Display. The program should be menu driven.*/

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#define max 50

void push(int number);
void pop();
void display();

int top = -1; 
int s[max];

int main(void)
{
    int choice;
    int array[100];
    int num;
    do
    {
        printf("Enter the serial number of to execute the following tasks\n");
        printf("1. Push\n");
        printf("2. Pop\n");
        printf("3. Display\n");
        printf("4. Exit\n");
        printf("\n");

        scanf("%d", &choice);
        switch (choice)
        {
            case 1:
                printf("what number do you want to push: ");
                scanf(" %d", &num);
                push(num);
                break;
            case 2:
                pop();
                break;
            case 3:
                display();
                break;
            
        }
    } while (choice != 4);
    return 0;
    
}

void push(int number)
{
    if(top == max-1)
    {
        printf("The stack is full {stackoverflow...}\n");
        return;
    }
    else
    {
        top = top + 1;
        s[top] = number;
    }
}

void pop()
{
    int temp;
    if (top == -1)
    {
        printf("The stack is empty{StackUnderflow...}\n");
        return;
    }
    else
    {
        temp = s[top];
        top = top - 1;
    }
}

void display()
{
    if (top == -1)
    {
        printf("Stack is empty\n");
    }
    else
    {
        for(int i = top; i >= 0; i--)
        {
            printf("%d\t", s[i]);
        }
        printf("\n");
    }
}
