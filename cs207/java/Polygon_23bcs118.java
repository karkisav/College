import java.util.ArrayList;
import java.util.List;

public abstract class Polygon_23bcs118 {
    protected List<Line_23bcs118> edges = new ArrayList<>();

    public void addEdge(Line_23bcs118 edge) {
        edges.add(edge);
    }

    public double getPerimeter() {
        double perimeter = 0;
        for (Line_23bcs118 edge : edges) {
            perimeter += edge.getLength();
        }
        return perimeter;
    }

    public abstract double getArea();

    public boolean isConvex() {
        int n = edges.size();
        boolean isConvex = true;
        for (int i = 0; i < n; i++) {
            double crossProduct = crossProduct(edges.get(i), edges.get((i + 1) % n));
            if (crossProduct < 0) {
                isConvex = false;
                break;
            }
        }
        return isConvex;
    }

    private double crossProduct(Line_23bcs118 edge1, Line_23bcs118 edge2) {
        return (edge1.getEnd().getX() - edge1.getStart().getX()) * (edge2.getEnd().getY() - edge2.getStart().getY()) -
               (edge1.getEnd().getY() - edge1.getStart().getY()) * (edge2.getEnd().getX() - edge2.getStart().getX());
    }
}
