#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

bool check(int *array[]);

typedef struct node
{
    int value;
    struct node *prev;
    struct node *next;
}node;
node *list = NULL;

bool unload()
{
    node *ptr;
    while(list != NULL)
    {
        ptr = list;
        list = list->next;
        free(list);
    }
    return true;
}
void delete_even()
{
    int i = 1;
    node *ptr = list;
    for(ptr; ptr->next != list; ptr = ptr->next)
    {
        if(i % 2 == 0)
        {
            delete();
        }
        i++;
    }
}
void display()
{
    node *ptr = list;
    for (ptr; ptr->next != list; ptr = ptr->next)
    {
        printf("%d\t", ptr->value);
    }
}
int main(void)
{
    int choice;

    printf("-----------Enter the operation -----------\n");
    printf(" 1.Insertion\n 2.Deletion\n 3.Display\n 4.Delete Odd Values\n 5.Exit{free}\n");
    printf("Enter the operation number: ");
    scanf(" %d", &choice);
    
    do
    {
        switch (choice)
        {
            case 1:
                insertion();
                break;
            case 2:
                deletion();
                break;
            case 3:
                display();
                break;
            case 4:
                if (!unload())
                {
                    printf("Error freeing the list.\n");
                    return 1;
                }
                printf("Freed the list.\n");
                return 0;
            default:
                printf("Enter valid option!");
                break;
        }
    } while (choice != 4);
}