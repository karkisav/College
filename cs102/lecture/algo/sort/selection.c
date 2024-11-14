#include <stdio.h>
#include <stdlib.h>

void swap(int *a, int i, int j) {
    if (i == j) return;  // No need to swap with itself
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
}

void sort(int *a, int n) {
    int i, j, min_idx;

    // One by one move boundary of already sorted subarray
    for (i = 0; i < n - 1; i++) {

        // Find the minimum element in unsorted array
        min_idx = i;
        for (j = i + 1; j < n; j++)
            if (a[j] < a[min_idx])
                min_idx = j;

        // Swap the found minimum element with the first element of unsorted array.
        swap(a, min_idx, i);
    }
}

int main(void) {
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
            while (getchar() != '\n');
            printf("Invalid input. Please enter an integer.\n");
            printf("Enter element %d: ", i + 1);
        }
    }

    sort(a, n);

    printf("Array AFTER sorting:\n");
    for (int i = 0; i < n; i++) {
        printf("%d", a[i]);
        if (i != n - 1) printf(", ");
    }

    free(a);
    return 0;
}