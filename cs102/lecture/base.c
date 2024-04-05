#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

typedef struct node
{
    int base;
    char digit;
    struct node *next;
}node;

node *list = NULL;
char key[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

void display();
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
        tmp /= base;
    } while (tmp != 0);

    display();

}

void insert(int r, int base)
{
    node *n = malloc(sizeof(node));
    n->base = base;
    n->digit = key[r];
    n->next = list;
    list = n;
}

void display()
{
    node *ptr;
    for(ptr = list; ptr != NULL; ptr = ptr->next)
    {
        printf("%c\t", ptr->digit);
    }
    printf("\n");
    for(ptr = list; ptr != NULL; ptr = ptr->next)
    {
        printf("%d\t", ptr->base);
    }
    return;
}