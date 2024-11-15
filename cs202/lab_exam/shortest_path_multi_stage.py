# Multistage Graph declaration using a dictionary
graph = {
    0: [(1, 1), (2, 2), (3, 5)],
    1: [(4, 4), (5, 11)],
    2: [(4, 9), (5, 5), (6, 16)],
    3: [(6, 2)],
    4: [(7, 18)],
    5: [(7, 13)],
    6: [(7, 2)],
    7: []
}

stages = 4
n = len(graph)

def shortest_path_with_trace(graph, stages, n):
    dist = [float('inf')] * n
    dist[n - 1] = 0
    predecessors = [-1] * n  # Store the predecessor for each node

    for i in range(n - 2, -1, -1):
        for (j, weight) in graph[i]:
            if dist[i] > dist[j] + weight:
                dist[i] = dist[j] + weight
                predecessors[i] = j

    # Reconstruct the path from source to sink using predecessors
    path = []
    current_node = 0
    while current_node != -1:
        path.append(current_node)
        current_node = predecessors[current_node]

    return dist[0], path

# Get the minimum distance and path
min_cost, path = shortest_path_with_trace(graph, stages, n)
print(f"Minimum distance to travel from source to sink is: {min_cost}")
print(f"Path from source to sink is: {path}")
