#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

bool check(int *array[]);

typedef struct node
{
    int value;
    struct node *next;
}node;