#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

// Structure of a node in the binary tree
typedef struct TreeNode {
    int val;
    struct TreeNode* left;
    struct TreeNode* right;
} TreeNode;

// Function to create a new node
TreeNode* createNode(int val) {
    TreeNode* newNode = (TreeNode*)malloc(sizeof(TreeNode));
    newNode->val = val;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

// Function to perform inorder traversal and check if the values are sorted
bool isValidBSTUtil(TreeNode* root, int* prev) {
    if (root == NULL)
        return true;

    // Check left subtree
    if (!isValidBSTUtil(root->left, prev))
        return false;

    // Check current node
    if (root->val <= *prev)
        return false;
    *prev = root->val;

    // Check right subtree
    return isValidBSTUtil(root->right, prev);
}

// Function to check if a binary tree is a valid BST
bool isValidBST(TreeNode* root) {
    int prev = -2147483648; // Assume INT_MIN as the previous value
    return isValidBSTUtil(root, &prev);
}

int main() {
    // Example usage
    TreeNode* root = createNode(2);
    root->left = createNode(1);
    root->right = createNode(3);

    if (isValidBST(root))
        printf("The given binary tree is a valid BST.\n");
    else
        printf("The given binary tree is not a valid BST.\n");

    return 0;
}
