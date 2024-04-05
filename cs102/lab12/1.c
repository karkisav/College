#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Structure of a node in the Binary Search Tree
typedef struct PhonebookEntry {
    char name[50];
    char phoneNumber[15];
    struct PhonebookEntry* left;
    struct PhonebookEntry* right;
} PhonebookEntry;

// Function to create a new phonebook entry
PhonebookEntry* createEntry(char name[], char phoneNumber[]) {
    PhonebookEntry* newEntry = (PhonebookEntry*)malloc(sizeof(PhonebookEntry));
    strcpy(newEntry->name, name);
    strcpy(newEntry->phoneNumber, phoneNumber);
    newEntry->left = NULL;
    newEntry->right = NULL;
    return newEntry;
}

// Function to insert a new entry into the Binary Search Tree
PhonebookEntry* insertEntry(PhonebookEntry* root, char name[], char phoneNumber[]) {
    if (root == NULL)
        return createEntry(name, phoneNumber);

    if (strcmp(name, root->name) < 0)
        root->left = insertEntry(root->left, name, phoneNumber);
    else if (strcmp(name, root->name) > 0)
        root->right = insertEntry(root->right, name, phoneNumber);

    return root;
}

// Function to search for a contact by name
PhonebookEntry* searchEntry(PhonebookEntry* root, char name[]) {
    if (root == NULL || strcmp(name, root->name) == 0)
        return root;

    if (strcmp(name, root->name) < 0)
        return searchEntry(root->left, name);
    else
        return searchEntry(root->right, name);
}

// Function to display all contacts in alphabetical order (in-order traversal)
void displayContacts(PhonebookEntry* root) {
    if (root != NULL) {
        displayContacts(root->left);
        printf("Name: %s, Phone Number: %s\n", root->name, root->phoneNumber);
        displayContacts(root->right);
    }
}

int main() {
    PhonebookEntry* phonebook = NULL;

    // Insert some sample contacts
    phonebook = insertEntry(phonebook, "pandey", "1234567890");
    phonebook = insertEntry(phonebook, "sav", "9876543210");
    phonebook = insertEntry(phonebook, "babu prasad", "589354325");

    // Search for a contact
    char searchName[50];
    printf("Enter name to search: ");
    scanf("%s", searchName);
    int n = strlen(searchName);
    for(int i = 0; i < n; i++)
        searchName[i] = tolower(searchName[i]);

    PhonebookEntry* foundContact = searchEntry(phonebook, searchName);
    if (foundContact != NULL)
        printf("Contact found: Name: %s, Phone Number: %s\n", foundContact->name, foundContact->phoneNumber);
    else
        printf("Contact not found.\n");

    // Display all contacts
    printf("\nAll Contacts:\n");
    displayContacts(phonebook);

    return 0;
}