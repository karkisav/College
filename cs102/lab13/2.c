#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct {
    int numVertices;
    int** adjMatrix;
    bool* visited;
} Graph;

Graph* createGraph(int vertices);
void bfs(Graph* graph, int startVertex);
void destroyGraph(Graph* graph);

int main() {
    int numVertices;
    printf("Enter the number of vertices in the graph: ");
    scanf("%d", &numVertices);

    Graph* graph = createGraph(numVertices);

    // Input adjacency matrix
    printf("Enter the adjacency matrix for the graph:\n");
    for (int i = 0; i < numVertices; i++) {
        for (int j = 0; j < numVertices; j++) {
            printf("a[%d][%d]: ", i, j);
            scanf("%d", &graph->adjMatrix[i][j]);
        }
    }

    printf("BFS Traversal:\n");
    bfs(graph, 0);

    // Free allocated memory
    destroyGraph(graph);

    return 0;
}

Graph* createGraph(int vertices) {
    Graph* graph = malloc(sizeof(Graph));
    if (graph == NULL) {
        printf("Memory allocation failed\n");
        exit(EXIT_FAILURE);
    }

    graph->numVertices = vertices;

    graph->adjMatrix = malloc(vertices * sizeof(int*));
    if (graph->adjMatrix == NULL) {
        printf("Memory allocation failed\n");
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < vertices; i++) {
        graph->adjMatrix[i] = malloc(vertices * sizeof(int));
        if (graph->adjMatrix[i] == NULL) {
            printf("Memory allocation failed\n");
            exit(EXIT_FAILURE);
        }
    }

    graph->visited = calloc(vertices, sizeof(bool));
    if (graph->visited == NULL) {
        printf("Memory allocation failed\n");
        exit(EXIT_FAILURE);
    }

    return graph;
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

    for (int i = 0; i < graph->numVertices; i++) {
        printf("|%d| ", queue[i]);
    }

    free(queue);
}

// Function to free allocated memory for the graph
void destroyGraph(Graph* graph) {
    if (graph != NULL) {
        for (int i = 0; i < graph->numVertices; i++)
            free(graph->adjMatrix[i]);
        free(graph->adjMatrix);
        free(graph->visited);
        free(graph);
    }
}