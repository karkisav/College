public class Square_23bcs118 extends Polygon_23bcs118 {
    private double sideLength;

    public Square_23bcs118(double sideLength) {
        this.sideLength = sideLength;
        // Create 4 edges of the square and add them to edges list
    }

    @Override
    public double getArea() {
        return sideLength * sideLength;
    }

    @Override
    public double getPerimeter() {
        return 4 * sideLength;
    }
}
