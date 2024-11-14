#include <stdio.h>
#include <stdlib.h>

void QuickSort(int *a, int start, int end){
    if(start >= end) return;

    int pivot = a[(start + end) / 2];

    int i = start;
    int j = end;

    while(i <= j){
        while((a[j] > pivot)) j--;
        while (a[i] < pivot) i++;
        
        if(i <= j){
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            i++, j--;
        }
    }

    QuickSort(a, start, j);
    QuickSort(a, i, end);
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

    QuickSort(arr, 0, n - 1);

    printf("Array AFTER sorting:\n");
    for (int i = 0; i < n; i++) {
        printf("%d", arr[i]);
        if (i != n - 1)
            printf(", ");
    }

    free(arr);
    return 0;
}