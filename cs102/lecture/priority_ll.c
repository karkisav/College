#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

bool unload(node *list);

typedef struct node
{
    int value;
    int priority;
    struct node* next;
}node;

bool insertion(node *list);
bool deletion(node *list);
void display(node *list);

int main(void)
{
    node *list = NULL;
    int choice;
    printf("-----------Enter the operation -----------\n");
    printf(" 1.Insertion\n 2.Deletion\n 3. Display\n 4. Exit{free}");
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

bool inserrtion(node *list)
{
    int value, priority;
    printf("Enter your value: ");
    scanf("%d", &value);

    printf("Enter the priorty: ");
    scanf("%d", &priority);

    node *n = malloc(sizeof(node));

    n->value = value;
    n->priority = priority;
    n->next = list;
    n = list;


}

void display(node *list)
{
        printf("\n+-- List Visualizer --+\n\n");
    while (list != NULL)
    {
        printf("Location %p\nPhrase: \"%s\"\nNext: %p\n\n", list, list->value, list->next);
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