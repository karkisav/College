#include <stdio.h>

void merge_sort(int a[], int start, int end);
void merge(int a[], int start, int middle, int end);

int main (void)
{
    int n;
    printf("Enter No. of elemnts: ");
    scanf("%d", &n);
    int a[n];
    for (int i = 0; i < n; i++)
    {
        printf("Element %d: ", i);
        scanf("%d", &a[i]);
    }
    int start = 0;
    int end = n - 1;
    merge_sort(a, start, end);
    for (int i = 0; i < n; i++)
    {
        printf("%d ", a[i]);
    }
    printf("\n");
}
void merge_sort(int a[], int start, int end)
{
    if (start < end)
    {
        int middle = (start + end) / 2;
        merge_sort(a, start, middle);
        merge_sort(a, middle + 1, end);
        merge(a, start, middle, end);
    }
}
void merge(int a[], int start, int middle, int end)
{
    int result[end - start + 1];
    int i = start, j = middle + 1, k = 0;
    while (i <= middle && j <= end)
    {
        if (a[i] <= a[j])
        {
            result[k] = a[i++];
        }
        else
        {
            result[k] = a[j++];
        }
        k++;
    }
    while (i <= middle)
    {
        result[k++] = a[i++];
    }
    while (j <= end)
    {
        result[k++] = a[j++];
    }
    for (int c = start; c <= end; c++)
        a[c] = result[c - start];
}