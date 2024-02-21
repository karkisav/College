#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>


typedef struct node
{
    int value;
    int priority;
    struct node* next;
}node;

node *list = NULL;

void insertion(node *list);
void deletion(node *list);
bool unload(node *list);
void display(node *list);

int main(void)
{
    int choice;

    printf("-----------Enter the operation -----------\n");
    printf(" 1.Insertion\n 2.Deletion\n 3.Display\n 4.Exit{free\n");
    printf("Enter the operation number: ");
    scanf(" %d", &choice);
    
    do
    {
        switch (choice)
        {
            case 1:
                insertion(list);
                break;
            case 2:
                deletion(list);
                break;
            case 3:
                display(list);
                break;
            case 4:
                if (!unload(list))
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

void insertion(node *list)
{
    int value, priority;
    node *ptr = list;
    printf("Enter your value to be stored: ");
    scanf("%d", &value);

    printf("Enter the priority: ");
    scanf("%d", &priority);

    node *n = malloc(sizeof(node));
    if (n == NULL)
    {
        printf("Error no more space left!\n");
        return;
    }

    n->value = value;
    n->priority = priority;
    n->next = NULL;

    if (list == NULL || priority > list->priority)
    {
        n->next = list;
        list = n; // Change made to the list pointer
        return;
    }

    while (ptr->next != NULL && priority <= ptr->next->priority)
    {
        ptr = ptr->next;
    }

    n->next = ptr->next;
    ptr->next = n;
    return;
}

void display(node *list)
{
    printf("\n+-- List Visualizer --+\n\n");
    while (list != NULL)
    {
        printf("Location %p\nValue: \"%d\"\nPriority: \"%d\"\nNext: %p\n\n", list, list->value, list->priority, list->next);
        list = list->next;
    }
    printf("+---------------------+\n\n");
}

void deletion(node *list)
{
    if (list == NULL)
    {
        printf("The list is empty\n");
        return;
    }
    node *ptr = list;

    if (list->next == NULL)
    {
        free(list);
        list = NULL;
        return;
    }
    while(ptr->next != NULL && ptr->next->next != NULL)
    {
        ptr = ptr->next;
    }
    printf("The number to be deleted from the list is: %d", ptr->next->value);
    free(ptr->next);
    ptr->next = NULL;
}

bool unload(node *list)
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