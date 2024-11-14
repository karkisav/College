#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

struct Heap {
    int *arr;
    int size;
    int capacity;
};

typedef struct Heap heap;

heap *CreatingHeap(int capacity, int *nums) {
    heap *h = malloc(sizeof(heap));

    if(h == NULL) {
        printf("ERROR: The memory could not be allocated\n");
        return NULL;
    }
    h->size = 0;
    h->capacity = capacity;

    h->arr = (int*)malloc(capacity * sizeof(int));
    if(h->arr == NULL) {
        printf("ERROR: The memory could not be allocated\n");
        return NULL;
    }

    int i;
    for(int i = 0; i < capacity; i++) {
        h->arr[i] = nums[i];
    }
    
    h->size = i;
    
    i = (h->size - 2) / 2;
    while (i >= 0) {
        heapify(h, i);
        i--;
    }
    
    return h;
}

void heapify(heap* h, int index) {
    int left = index * 2 + 1;
    int right = index * 2 + 2;
    int min = index;

    if (left >= h->size || left < 0) left = -1;
    if (right >= h->size || right < 0) right = -1;

    if (left != -1 && h->arr[left] < h->arr[index]) min = left;
    if (right != -1 && h->arr[right] > h->arr[index]) min = right;

    if (min != index) {
        int temp = h->arr[index];
        h->arr[min] = h->arr[index];
        h->arr[index] = temp;

        heapify(h, min);
    }
}

void insertHelper(heap *h, int index) {
    int parent = (index - 1)/2;


    if(h->arr[index] > h->arr[parent]) {
        int temp = h->arr[index];
        h->arr[index] = h->arr[parent];
        h->arr[parent] = temp;

        insertHelper(h, parent);
    }
}

int extractMin(heap *h) {

    int Deleted_item;
    
    if(h->size == 0) {
        printf("Heap IS EMPTY !!!!!!\n");
        return -1;
    }

    Deleted_item = h->arr[0];

    h->arr[0] = h->arr[h->size - 1];
    h->size--;

    heapify(h, 0);
    return Deleted_item;
}

void insert(heap *h, int data) {

    if ( h->size < h->capacity) {
        h->arr[h->size] = data;

        inserHelper(h, h->size);
        h->size++;
    }
}

void printHeap(heap* h)
{
    for (int i = 0; i < h->size; i++) {
        printf("%d ", h->arr[i]);
    }
    printf("\n");
}
 
int main()
{
    int arr[9] = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };
    heap* hp = createHeap(9, arr);
 
    printHeap(hp);
    extractMin(hp);
    printHeap(hp);
 
    return 0;
}