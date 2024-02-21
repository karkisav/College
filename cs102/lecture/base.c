#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

typedef struct node
{
    int base;
    char digit;
    node *next;
}node;

node *list = NULL;
char key[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C'};

void insert(int r, int base);
int main(void)
{
    int base, tmp, num , r;
    printf("Enter the Base: ");
    scanf("%d", &base);

    printf("Enter the number for base conversion: ");
    scanf("%d", &num);

    tmp = num;
    do
    {
        r = tmp % base;
        insert(r, base);
    } while (tmp != 0);
    

}

void insert(int r, int base)
{
    node *n = malloc(sizeof(node));
    n->base = base;
    n->digit = key[r];
    n->next = list;
    list = n;
}