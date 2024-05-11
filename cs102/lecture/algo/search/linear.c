#include <stdio.h>
#include <stdlib.h>

int find(int a[], int x, int n) {
    for (int i = 0; i < n; i++) {
        if (x == a[i]) {
            return i; // Return the index if found
        }
    }
    return -1; // Return -1 if not found
}

int main() {
    int n;
    printf("Enter the number of elements: ");
    scanf("%d", &n);

    int *a = malloc(n * sizeof(int));
    if (a == NULL) {
        fprintf(stderr, "Error: Memory allocation failed.\n");
        return 1; // Indicate error
    }

    for (int i = 0; i < n; i++) {
        printf("Enter element %d: ", i+1);
        scanf("%d", &a[i]);
    }

    int x;
    printf("Enter the element you want to find: ");
    scanf("%d", &x);

    int index = find(a, x, n);
    if (index != -1) {
        printf("Element %d found at index %d\n", x, index);
    } else {
        printf("Element %d not found\n", x);
    }

    free(a);
    return 0;
}