import java.util.Random;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D() {
        this(0.0, 0.0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D ZERO() {
        return new Vector2D(0.0, 0.0);
    }

    public void add(double cx, double cy) {
        this.x += cx;
        this.y += cy;
    }

    public void add(Vector2D vec) {
        this.add(vec.x, vec.y);
    }

    public static double dist(Vector2D a, Vector2D b) {
        return Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }

    public double angle() {
        return x < 0 ? Math.PI + Math.atan(y / x) : Math.atan(y / x);
    }

    public double angle2(double cx, double cy) {
        return Math.atan2(x - cx, y - cy);
    }

    public Vector2D norm() {
        if(this.mag() != 0) {
            return new Vector2D(x / this.mag(), y / this.mag());
        } else {
            return Vector2D.ZERO();
        }
        
    }

    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    public static Vector2D random() {
        Random rand = new Random();
        return new Vector2D(rand.nextDouble() * 2d - 1d, rand.nextDouble() * 2d - 1);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
