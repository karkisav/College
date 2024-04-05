#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Grades {
    int grade;
    struct Grades* next;
} Grades;

typedef struct Student {
    char name[50];
    Grades* gradesList;
    struct Student* left;
    struct Student* right;
} Student;

Student* createStudent(char name[], int grade) {
    Student* newStudent = (Student*)malloc(sizeof(Student));
    strcpy(newStudent->name, name);
    newStudent->gradesList = (Grades*)malloc(sizeof(Grades));
    newStudent->gradesList->grade = grade;
    newStudent->gradesList->next = NULL;
    newStudent->left = NULL;
    newStudent->right = NULL;
    return newStudent;
}

Student* insertStudent(Student* root, char name[], int grade) {
    if (root == NULL)
        return createStudent(name, grade);

    if (strcmp(name, root->name) < 0)
        root->left = insertStudent(root->left, name, grade);
    else if (strcmp(name, root->name) > 0)
        root->right = insertStudent(root->right, name, grade);
    else {
        // If student already exists, add the grade to their grades list
        Grades* newGrade = (Grades*)malloc(sizeof(Grades));
        newGrade->grade = grade;
        newGrade->next = root->gradesList;
        root->gradesList = newGrade;
    }

    return root;
}

Student* searchStudent(Student* root, char name[]) {
    if (root == NULL || strcmp(name, root->name) == 0)
        return root;

    if (strcmp(name, root->name) < 0)
        return searchStudent(root->left, name);
    else
        return searchStudent(root->right, name);
}

void displayStudents(Student* root) {
    if (root == NULL)
        return;

    // Create an empty stack for iterative inorder traversal
    Student* stack[100];
    int top = -1;

    while (1) {
        // Reach the leftmost student of the current student
        while (root != NULL) {
            stack[++top] = root;
            root = root->left;
        }

        // If stack is empty, we're done
        if (top == -1)
            break;

        // Pop the top student from stack and print it
        root = stack[top--];
        printf("Name: %s, Grades: ", root->name);
        Grades* currentGrade = root->gradesList;
        while (currentGrade != NULL) {
            printf("%d ", currentGrade->grade);
            currentGrade = currentGrade->next;
        }
        printf("\n");

        // Move to the right and continue traversal
        root = root->right;
    }
}

int main() {
    Student* gradeTracker = NULL;

    gradeTracker = insertStudent(gradeTracker, "kaka", 85);
    insertStudent(gradeTracker, "Alakh", 92);
    insertStudent(gradeTracker, "kaka", 78);
    insertStudent(gradeTracker, "nigbba", 95);
    insertStudent(gradeTracker, "pandey", 70);

    char searchName[50];
    printf("Enter name to search: ");
    scanf("%s", searchName);
    Student* foundStudent = searchStudent(gradeTracker, searchName);
    if (foundStudent != NULL) {
        printf("Grades for %s: ", foundStudent->name);
        Grades* currentGrade = foundStudent->gradesList;
        while (currentGrade != NULL) {
            printf("%d ", currentGrade->grade);
            currentGrade = currentGrade->next;
        }
        printf("\n");
    } else {
        printf("Student not found.\n");
    }

    printf("\nAll Students and Grades:\n");
    displayStudents(gradeTracker);

    return 0;
}