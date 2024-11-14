#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>


void print(int *a, int n);
void max_bubble(int *a, int n);
void min_bubble(int *a, int n);
void swap(int *a, int i, int j);


int main(void){
    int n;
    printf("Enter the number of elements you want to sort: ");
    scanf("%d", &n);

    int *a = malloc(n * sizeof(int));
    if (a == NULL) {
        perror("Failed to allocate memory.");
        return EXIT_FAILURE;
    }

    for (int i = 0; i < n; i++) {
        printf("Enter element %d: ", i + 1);
        while (scanf("%d", &a[i]) != 1) {
            // Clear input buffer
            while (getchar() != '\n');
            printf("Invalid input. Please enter an integer.\n");
            printf("Enter element %d: ", i + 1);
        }
    }

    printf("Before: \n");
    print(a, n);

    max_bubble(a, n);
    printf("After: \n");
    print(a, n);

    return 0;

}

void max_bubble(int *a, int n){
    for(int i = 0; i < n - 1; i++)
        for (int j = 0; j < n - i - 1; j++)
            if (a[j] > a[j + 1]) swap(a, j, j + 1);
}
void min_bubble(int *a, int n){
    for(int i = 0; i < n - 1; i++)
        for (int j = 0; j < n - i - 1; j++)
            if (a[j] < a[j + 1]) swap(a, j, j + 1);
}

void swap(int *a, int i, int j) {
    if (i == j) return;  // No need to swap with itself
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

void print(int *a, int n){
    for(int i = 0; i < n; i++){
        printf("%d ", a[i]);
    }
    printf("\n");
}