public class Circle_23bcs118 extends Polygon_23bcs118 {
    private double radius;

    public Circle_23bcs118(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}