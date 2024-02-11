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
                //deletion(list);
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

    if(n->priority < n->next->priority)
    {
         n->value = value;
        n->priority = priority;
        n->next = list;
        n = list;
    }
}

void display(node *list)
{
    printf("\n+-- List Visualizer --+\n\n");
    while (list != NULL)
    {
        printf("Location %p\nPhrase: \"%d\"\nNext: %p\n\n", list, list->value, list->next);
        list = list->next;
    }
    printf("+---------------------+\n\n");
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

void deleteion(node *list)
{
    node *ptr = list->next;
    if(ptr == NULL)
    {
        return 1;
    }
    node *tptr = ptr;
    node *xptr = NULL;
    while(ptr != NULL)
    {   
        xptr = tptr;
        tptr = ptr;
        ptr = ptr->next;
    }
    printf("The number to be deleted from the list is: %d", tptr->value);
    if(xptr == NULL)
    {
        free(tptr);
        return 1;
    }
    xptr->next = NULL;
    free(tptr);
}