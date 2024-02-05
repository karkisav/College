/*Write a C program to read 5 integer numbers into a Stack of
size 10. The program should be able to compute the sum,
average, maximum and minimum of the numbers in the stack and
store them in the stack at subsequent positions (LIFO order)
respectively.*/

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#define max 10
int top = -1, array[max];

void push(int number);
void pop();
void display();
void execute();

int main(void)
{
    int choice, number;
    printf("------This program will take in 5 integers and compute the following------\n");
    printf("1.Sum\t 2.Average\t 3.Maximum\t 4. Minimum\n");
    do
    {
        printf("----------------------------Enter the operation----------------------------\n");
        printf(" 1. Push\n 2. Pop\n 3. Display\n 4. Execute\n 5.Exit\n");
        printf("Operation: ");
        scanf("%d", &choice);
        printf("\n");

        switch (choice)
        {
            case 1:
                printf("Enter the element you want to push: ");
                scanf("%d", &number);
                push(number);
                break;
            case 2:
                pop();
                break;
            case 3:
                display();
                break;
            case 4:
                execute();
                break;
        }
    }
    while(choice != 5);
    return 0;
}

void push(int number)
{
    if(top == 4)
    {
        printf("StackOverflow...\n...Only 5 numbers can be pushed...\n");
        return;
    }
    else
    {
        top++;
        array[top] = number;
    }
}
void pop()
{
    int temp;
    if(top == -1)
    {
        printf("StackUnderflow...\n...No elements left to pop...\n");
        return;
    }
    else
    {
        char ch;
        temp = array[top];

        printf("Element to be popped is %d\n", temp);
        printf("Enter y/n: ");
        scanf(" %c", &ch);

        if(tolower(ch) == 'y')
        {
            temp = array[top];
            top--;
        }
    }
}
void display()
{
    if (top == -1)
    {
        printf("StackUnderflow...\n...No elements to display...\n");
    }
    else
    {
        printf("--------------------------------Your stack--------------------------------\n");
        for(int i = 0; i <= top; i++)
        {
            printf("%d\t", array[i]);
        }
        printf("\n");
    }
}
void execute()
{
    if(top != 4)
    {
        printf("StackUnderflow...\n...Stack is still not complete...\n");
        return;
    }
    else
    {
        int sum = 0, minimum, maximum;
        float avg;
        maximum = array[0];
        minimum = array[0];
        for(int i = 0; i <= top; i ++)
        {
            sum += array[i];
            if(maximum < array[i])
                maximum = array[i];
            if(minimum > array[i])
                minimum = array[i];
        }
        avg = (float)sum/top;
        printf("1.Sum = %d\n", sum);
        printf("2.Average = %f\n", avg);
        printf("3.Maximum = %d\n", maximum);
        printf("4.Minimum = %d\n", minimum);
    }
}