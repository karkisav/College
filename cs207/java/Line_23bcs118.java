public class Line_23bcs118 {
    private Point_23bcs118 start, end;

    public Line_23bcs118(Point_23bcs118 start, Point_23bcs118 end) {
        this.start = start;
        this.end = end;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
    }

    public Point_23bcs118 getStart() {
        return start;
    }

    public Point_23bcs118 getEnd() {
        return end;
    }
}

