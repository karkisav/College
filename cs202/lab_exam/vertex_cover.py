def vertex_cover(graph):
    cover = set()
    edges = set(graph)

    while edges:
        u, v = edges.pop()

        cover.add(u)
        cover.add(v)

        edges = {edge for edge in edges if u not in edge and v not in edge}

    return cover

graph = {(1, 2), (1, 3), (2, 4), (3, 4), (4, 5)}

vertex_cover_set = vertex_cover(graph)
print("Approximate Vertex Cover:", vertex_cover_set)