public class Rectangle_23bcs118 extends Polygon_23bcs118 {
    private double length, width;

    public Rectangle_23bcs118(double length, double width) {
        this.length = length;
        this.width = width;
        // Create 4 edges of the rectangle and add them to edges list
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }
}
