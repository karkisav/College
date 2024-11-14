#include <stdio.h>
#include <stdlib.h>

// Define the structure for a node in the graph
typedef struct Node {
    int data;
    struct Node* next;
} Node;

// Define the structure for an adjacency list representation of a graph
typedef struct AdjList {
    Node* head; // Pointer to the first element in the list
} AdjList;

// Function to create a new node with given data
Node* createNode(int data) {
    Node* newNode = (Node*) malloc(sizeof(Node));
    if (!newNode) {
        printf("Memory error\n");
        return NULL;
    }
    newNode->data = data;
    newNode->next = NULL;
    return newNode;
}

// Function to perform BFS traversal
void BFS(AdjList* graph, int startNode) {
    // Create a queue for BFS traversal
    NodeQueue* nodeQueue = NULL;

    // Mark all nodes as not visited initially
    AdjList* adjList = graph;
    while (adjList) {
        Node* currNode = adjList->head;
        while (currNode) {
            currNode->visited = 0;  // Initialize node as not visited
            currNode = currNode->next;
        }
        adjList = adjList->next;
    }

    // Enqueue the start node and mark it as visited initially
    Node* newNode = createNode(startNode);
    newNode->visited = 1;  // Mark start node as visited

    // Add start node to the queue
    if (!nodeQueue) {
        nodeQueue = newNode;
    } else {
        Node* tempNode = nodeQueue;
        while (tempNode->next) {
            tempNode = tempNode->next;
        }
        tempNode->next = newNode;
    }

    // Perform BFS traversal
    printf("BFS Traversal: ");
    while (nodeQueue) {
        Node* visitedNode = nodeQueue;
        nodeQueue = nodeQueue->next;

        // Print the visited node
        printf("%d ", visitedNode->data);

        // Dequeue a visited node and add all its adjacent nodes to the queue
        Node* tempNode = graph->head;
        while (tempNode) {
            if (!visitedNode->visited && tempNode->data == visitedNode->next->data) {
                Node* newNode = createNode(tempNode->data);
                newNode->visited = 1;  // Mark new node as visited

                // Add the new node to the queue
                Node* tempNode2 = nodeQueue;
                while (tempNode2->next) {
                    tempNode2 = tempNode2->next;
                }
                tempNode2->next = newNode;

                tempNode = tempNode->next;
            } else {
                tempNode = tempNode->next;
            }
        }
    }

    printf("\n");
}
int main() {
    // Create a graph with three nodes and two edges
    AdjList* graph = (AdjList*) malloc(sizeof(AdjList));
    Node* node1 = createNode(0);
    Node* node2 = createNode(1);
    Node* node3 = createNode(2);

    graph->head = node1;
    node1->next = node2;
    node2->next = node3;

    // Perform BFS traversal starting from node 0
    BFS(graph, 0);

    return 0;
}
