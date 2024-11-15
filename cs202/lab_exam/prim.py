from collections import defaultdict
import heapq

def prims_algo(graph):
    mst_edges = []
    total_cost = 0
    start_vertex = list(graph.keys())[0]

    visited = set()

    pq = []
    visited.add(start_vertex)

    for next_vertex, weight in graph[start_vertex].items():
        heapq.heappush(pq, (weight, start_vertex, next_vertex))

    while pq and len(visited) < len(graph):
        weight, current_vertex, next_vertex = heapq.heappop(pq)
        
        if next_vertex in visited:
            continue

        visited.add(next_vertex)

        mst_edges.append((current_vertex, next_vertex, weight))
        total_cost += weight

        for neighbor, edge_weight in graph[next_vertex].items():
            if neighbor not in visited:
                heapq.heappush(pq, (edge_weight, next_vertex, neighbor))
    
    return total_cost, mst_edges

def example_usage():
    # Create a sample graph
    graph = {
        'A': {'B': 4, 'C': 2},
        'B': {'A': 4, 'C': 1, 'D': 5},
        'C': {'A': 2, 'B': 1, 'D': 8, 'E': 10},
        'D': {'B': 5, 'C': 8, 'E': 2},
        'E': {'C': 10, 'D': 2}
    }
    
    total_cost, mst = prims_algo(graph)
    return total_cost, mst