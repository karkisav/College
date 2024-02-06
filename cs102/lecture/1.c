/*Write a C program that dynamically allocates memory for a structure representing a book. The structure should have members for the title, author, and publication year. Prompt the user to enter the number of books.

The program should be able to read the information about the specified number of books and print the information for a book and display it before freeing the allocated memory.*/


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

typedef struct book
{
    char title[10];
    int pages;
} book;

int main(void)
{
    int n;
    char string[10];
    printf("Enter the number of books: ");
    scanf("%d", &n);
    struct book *ptr = malloc(n * sizeof(book));
    if(ptr == NULL)
    {
        return 1;
    }
    for (int i = 0; i < n; i++)
    {
        printf("Enter the title of the book: ");
        scanf(" %[^\n]", string);
        strcpy(ptr[i].title, string);
    }

    free(ptr);
    return 0;
}