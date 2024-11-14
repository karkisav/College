#include <stdio.h>
#include <stdlib.h>

void merge(int* arr, int left, int mid, int right) {
    int i, j, k;
    int n1 = mid - left + 1;
    int n2 = right - mid;

    // Create temp arrays
    int* L = (int*)malloc(n1 * sizeof(int));
    int* R = (int*)malloc(n2 * sizeof(int));

    // Copy data to temp arrays L[] and R[]
    for (i = 0; i < n1; i++) {
        L[i] = arr[left + i];
    }
    for (j = 0; j < n2; j++) {
        R[j] = arr[mid + 1 + j];
    }

    // Merge the temp arrays back into arr[l..r]
    i = 0;
    j = 0;
    k = left;
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arr[k++] = L[i++];
        } else {
            arr[k++] = R[j++];
        }
    }

    // Copy the remaining elements of L[], if any
    while (i < n1) {
        arr[k++] = L[i++];
    }

    // Copy the remaining elements of R[], if any
    while (j < n2) {
        arr[k++] = R[j++];
    }

    free(L);
    free(R);
}

void mergeSort(int* arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;

        // Recursively call mergeSort() for the two halves
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        // Call merge() to combine the two halves back into arr[left..right]
        merge(arr, left, mid, right);
    }
}

int main() {
    int n;
    printf("Enter the number of elements you want in your array: ");
    if (scanf("%d", &n) != 1 || n <= 0) {
        fprintf(stderr, "Invalid input for the number of elements.\n");
        return EXIT_FAILURE;
    }

    int* arr = (int*)malloc(n * sizeof(int));
    if (arr == NULL) {
        perror("Failed to allocate memory.");
        return EXIT_FAILURE;
    }

    for (int i = 0; i < n; i++) {
        printf("Enter element %d: ", i + 1);
        while (scanf("%d", &arr[i]) != 1) {
            // Clear input buffer
            int c;
            while ((c = getchar()) != '\n' && c != EOF);
            printf("Invalid input. Please enter an integer.\n");
            printf("Enter element %d: ", i + 1);
        }
    }

    mergeSort(arr, 0, n - 1);

    printf("Array AFTER sorting:\n");
    for (int i = 0; i < n; i++) {
        printf("%d", arr[i]);
        if (i != n - 1)
            printf(", ");
    }

    free(arr);
    return 0;
}