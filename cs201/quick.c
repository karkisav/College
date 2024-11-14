#include <stdio.h>
#include <stdlib.h>

void swap(int* a, int* b) {
    int t = *a;
    *a = *b;
    *b = t;
}

int partition(int arr[], int low, int high, int pivot_choice) {
    int pivot;
    switch(pivot_choice) {
        case 1: // First element as pivot
            pivot = arr[low];
            swap(&arr[low], &arr[high]);
            break;
        case 2: // Last element as pivot
            pivot = arr[high];
            break;
        case 3: // Middle element as pivot
            pivot = arr[(low + high) / 2];
            swap(&arr[(low + high) / 2], &arr[high]);
            break;
        default:
            printf("Invalid pivot choice. Using last element as pivot.\n");
            pivot = arr[high];
    }
    
    int i = (low - 1);

    for (int j = low; j <= high - 1; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(&arr[i], &arr[j]);
        }
    }
    swap(&arr[i + 1], &arr[high]);
    return (i + 1);
}

void quickSort(int arr[], int low, int high, int pivot_choice) {
    if (low < high) {
        int pi = partition(arr, low, high, pivot_choice);

        quickSort(arr, low, pi - 1, pivot_choice);
        quickSort(arr, pi + 1, high, pivot_choice);
    }
}

void printArray(int arr[], int size) {
    for (int i = 0; i < size; i++)
        printf("%d ", arr[i]);
    printf("\n");
}

int main() {
    int n, pivot_choice;
    printf("Enter the number of elements: ");
    scanf("%d", &n);

    int *arr = (int*)malloc(n * sizeof(int));
    if (arr == NULL) {
        printf("Memory allocation failed\n");
        return 1;
    }

    printf("Enter %d integers:\n", n);
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    printf("Choose pivot selection method:\n");
    printf("1. First element\n");
    printf("2. Last element\n");
    printf("3. Middle element\n");
    printf("Enter your choice (1-3): ");
    scanf("%d", &pivot_choice);

    printf("Unsorted array: \n");
    printArray(arr, n);
    
    quickSort(arr, 0, n - 1, pivot_choice);
    
    printf("Sorted array: \n");
    printArray(arr, n);

    free(arr);
    return 0;
}