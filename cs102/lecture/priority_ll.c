#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>


typedef struct node
{
    int value;
    int priority;
    struct node* next;
}node;

void insertion(node *list);
//void deletion(node *list);
bool unload(node *list);
void display(node *list);

int main(void)
{
    node *list = NULL;
    int choice;
    printf("-----------Enter the operation -----------\n");
    printf(" 1.Insertion\n 2.Deletion\n 3. Display\n 4. Exit{free\n");
    printf("Enter the operation number: ");
    scanf("%d", &choice);
    
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
        }
    } while (choice != 4);
    
    if (!unload(list))
    {
        printf("Error freeing the list.\n");
        return 1;
    }

    printf("Freed the list.\n");
    return 0;
}

void insertion(node *list)
{
    int value, priority;
    printf("Enter your value to be stored: ");
    scanf("%d", &value);

    printf("Enter the priorty: ");
    scanf("%d", &priority);

    node *n = malloc(sizeof(node));
    n->value = value;
    n->priority = priority;

    if(n->priority > list->next->priority)
    {
        n->next = list;
        n = list;
    }
    else
    {
        for(list; list->next->next != NULL; list = list->next)
        {
            if(n->priority > list->next->priority)
            {
                n->next = list->next;
                n = list;
            }
        }
    }
}

void display(node *list)
{
    printf("\n+-- List Visualizer --+\n\n");
    while (list != NULL)
    {
        printf("Location %p\nValue: \"%d\"\nNext: %p\n\n", list, list->value, list->next);
        list = list->next;
    }
    printf("+---------------------+\n\n");
}

void deleteion(node *list)
{
    node *ptr = list->next;
    if(ptr == NULL)
        return;

    if(ptr->next == NULL)
    {
        free(ptr);
        return;
    }
    while(ptr->next->next != NULL)
    {
        ptr = ptr->next;
    }
    printf("The number to be deleted from the list is: %d", ptr->next->value);
    free(ptr->next);
    ptr->next = NULL;
}

bool unload(node *list)
{
    node *ptr = list->next;
    while(ptr != NULL)
    {
        ptr = list->next;
        free(list);
        list = ptr;
    }
    return true;
}