#include <stdio.h>
#include <stdlib.h>

void swap(int *a, int i, int j) {
    if (i == j)
        return; // No need to swap with itself

    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

void insertionSort(int *a, int n) {
    for (int i = 1; i < n; i++){
        int key = a[i];
        int j = i - 1;

        while (j >= 0 && a[j] > key){
            a[j + 1] = a[j];
            j--;
        }
        a[j + 1] = key;
    }
}

int main() {
    int n;
    printf("Enter the number of elements you want in your array: ");
    if (scanf("%d", &n) != 1 || n <= 0) {
        fprintf(stderr, "Invalid input for the number of elements.\n");
        return EXIT_FAILURE;
    }

    int *a = malloc(n * sizeof(int));
    if (a == NULL) {
        perror("Failed to allocate memory.");
        return EXIT_FAILURE;
    }

    for (int i = 0; i < n; i++) {
        printf("Enter element %d: ", i + 1);
        while (scanf("%d", &a[i]) != 1) {
            // Clear input buffer
            int c;
            while ((c = getchar()) != '\n' && c != EOF);
            printf("Invalid input. Please enter an integer.\n");
            printf("Enter element %d: ", i + 1);
        }
    }

    insertionSort(a, n);

    printf("Array AFTER sorting:\n");
    for (int i = 0; i < n; i++) {
        printf("%d", a[i]);
        if (i != n - 1)
            printf(", ");
    }

    free(a);
    return 0;
}