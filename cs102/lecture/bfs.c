#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h> // For boolean data type

typedef struct Graph {
    int numVertices;
    int** adjMatrix;
    bool* visited;
} Graph;

Graph* createGraph(int vertices) {
    Graph* graph = malloc(sizeof(Graph));
    if (graph == NULL) {
        printf("Memory allocation failed\n");
        return NULL;
    }

    graph->numVertices = vertices;

    // Allocate memory for adjacency matrix
    graph->adjMatrix = malloc(vertices * sizeof(int*));
    if (graph->adjMatrix == NULL) {
        free(graph);
        printf("Memory allocation failed\n");
        return NULL;
    }
    for (int i = 0; i < vertices; i++) {
        graph->adjMatrix[i] = calloc(vertices, sizeof(int));
        if (graph->adjMatrix[i] == NULL) {
            for (int j = 0; j < i; j++)
                free(graph->adjMatrix[j]);
            free(graph->adjMatrix);
            free(graph);
            printf("Memory allocation failed\n");
            return NULL;
        }
    }

    // Allocate memory for visited array
    graph->visited = calloc(vertices, sizeof(bool));
    if (graph->visited == NULL) {
        for (int i = 0; i < vertices; i++)
            free(graph->adjMatrix[i]);
        free(graph->adjMatrix);
        free(graph);
        printf("Memory allocation failed\n");
        return NULL;
    }

    return graph;
}

void addEdge(Graph* graph, int src, int dest) {
    if (src >= 0 && src < graph->numVertices && dest >= 0 && dest < graph->numVertices) {
        graph->adjMatrix[src][dest] = 1;
        graph->adjMatrix[dest][src] = 1; // For undirected graph
    } else {
        printf("Invalid vertex indices\n");
    }
}

void bfs(Graph* graph, int startVertex) {
    if (startVertex < 0 || startVertex >= graph->numVertices) {
        printf("Invalid start vertex\n");
        return;
    }

    int* queue = malloc(graph->numVertices * sizeof(int));
    if (queue == NULL) {
        printf("Memory allocation failed\n");
        return;
    }
    int front = -1, rear = -1;

    graph->visited[startVertex] = true;
    queue[++rear] = startVertex;

    while (front != rear) {
        int currentVertex = queue[++front];
        printf("Visited %d\n", currentVertex);

        for (int i = 0; i < graph->numVertices; i++) {
            if (graph->adjMatrix[currentVertex][i] && !graph->visited[i]) {
                graph->visited[i] = true;
                queue[++rear] = i;
            }
        }
    }

    // Printing the queue
    printf("Queue at the end of BFS: ");
    for (int i = 0; i <= rear; i++) {
        printf("%d ", queue[i]);
    }
    printf("\n");

    free(queue);
}

void destroyGraph(Graph* graph) {
    if (graph != NULL) {
        for (int i = 0; i < graph->numVertices; i++)
            free(graph->adjMatrix[i]);
        free(graph->adjMatrix);
        free(graph->visited);
        free(graph);
    }
}

void print(Graph *graph, int n) {
    printf("The Adjacency Matric is as follows: \n");
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            printf("%d ", graph->adjMatrix[i][j]);
        }
        printf("\n");
    }
}

int main() {
    int numVertices = 6;
    Graph* graph = createGraph(numVertices);

    addEdge(graph, 1, 2);
    addEdge(graph, 1, 4);
    addEdge(graph, 2, 1);
    addEdge(graph, 2, 5);
    addEdge(graph, 2, 3);
    addEdge(graph, 3, 4);
    addEdge(graph, 3, 5);
    addEdge(graph, 5, 3);

    print(graph, numVertices);

    printf("BFS Traversal:\n");
    bfs(graph, 1);

    destroyGraph(graph);
    return 0;
}