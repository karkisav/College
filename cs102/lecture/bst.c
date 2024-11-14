#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>

struct tree {
    int data;
    struct tree *right;
    struct tree *left;
};
struct tree *newnode(int data) {
    struct tree *newnode = malloc(sizeof(struct tree));
    newnode->data = data;
    newnode->right = NULL;
    newnode->left = NULL;
    return newnode;
}

struct tree *maketree(struct tree *head, int height) {
    if(height == 0) {
        return head;
    }
    else {
        char c;
        printf("Do you want to insert left child ? (y/n): ");
        scanf(" %c", &c); // Add space before %c to consume newline
        if(c == 'y' || c == 'Y') {
            int data;
            printf("Enter the value of the left child: ");
            scanf("%d", &data);
            head->left = newnode(data);
        }
        printf("Do you want to insert right child ? (y/n): ");
        scanf(" %c", &c); // Add space before %c to consume newline
        if(c == 'y' || c == 'Y') {
            int data;
            printf("Enter the value of the right child: ");
            scanf("%d", &data);
            head->right = newnode(data);
        }
        if(head->left != NULL) {
            printf("youre going to the left subtree\n");
            maketree(head->left, height - 1);
        }
        if(head->right != NULL) {
            printf("youre going to the right subtree\n");
            maketree(head->right, height - 1);
        }
    }
    return head;
}

bool validbst(struct tree *head, int min, int max) {
    if(head == NULL) {
        return true;
    }
    if(head->data >= max || head->data <= min) {
        printf("INVALID BST {WRONG VALUE INSERTION}\n");
        return false;
    }
    return validbst(head->left, min, head->data) && validbst(head->right, head->data, max);
}

int main(){
    struct tree *root = malloc(sizeof(struct tree));
    root->left = NULL;
    root->right = NULL;
    int height;
    do {
        printf("Enter height of the tree {if only one element ENTER 0}: ");
        scanf("%d", &height);
    }while(height < -1);
    int data;
    printf("enter the root node data: ");
    scanf("%d", &data);
    root->data = data;
    maketree(root, height);
    if(validbst(root, INT_MIN, INT_MAX)) {
        printf("valid BST tree\n");
    }
    else
        printf("INVALID BST\n");
}