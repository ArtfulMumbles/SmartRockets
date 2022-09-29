import java.awt.Polygon;

public class Rocket extends Sprite{
    public Vector2D[] velArr;

    public Vector2D vel;
    private int WIDTH = 10;
    private int LENGTH = 15;
    private double SPEED = 0.5;
    public int id;
    public boolean alive;
    
    public Rocket(double x, double y, int steps) {
        super(x, y);
        this.alive = true;
        this.vel = Vector2D.ZERO();
        this.velArr = new Vector2D[steps];
        for(int i = 0; i < steps; i++) {
            this.velArr[i] = Vector2D.random();
        }
    }

    public Rocket(double x, double y, Vector2D[] velArr) {
        super(x, y);
        this.vel = Vector2D.ZERO();
        this.velArr = velArr;
        this.alive = true;
    }

    public double calculateFitness(Vector2D target) {
        double dist = Vector2D.dist(new Vector2D(x, y), target);
        return alive ? 20 / (dist * dist) : 1 / (dist * dist);
    }
 
    public void add(int x, int y) {
        this.vel.add(x, y);
    }

    public void move() {
        x += vel.x * SPEED;
        y += vel.y * SPEED;
    }
 
    public Polygon getShape() {
        double a = vel.angle();
        return new Polygon(
            new int[]{(int) (WIDTH / 2 * Math.cos(a + Math.PI/2) + x), (int) (WIDTH/2 * Math.cos(a - Math.PI/2) + x), (int) (LENGTH * Math.cos(a) + x)}, 
            new int[]{(int) (WIDTH/2 * Math.sin(a + Math.PI/2) + y), (int) (WIDTH/2 * Math.sin(a - Math.PI/2) + y), (int) (LENGTH * Math.sin(a) + y)}, 3);
    }

    public Vector2D[] getVelArr() {
        return this.velArr;
    }

    public Vector2D getVel(int i) {
        return this.velArr[i];
    }

    public void setVelArr(Vector2D[] velArr) {
        this.velArr = velArr;
    }

    public void setVel(Vector2D vec, int index) {
        this.velArr[index] = vec;
    }

    public void kill() {
        this.alive = false;
    }

    public boolean outOfBounds() {
        if(x < 0 || y < 0 || x > 1000 || y > 800) {
            return true;
        }
        return false;
    }

    public boolean dead() {
        return !alive;
    }

}
