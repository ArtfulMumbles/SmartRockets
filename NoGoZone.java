public class NoGoZone {
    public int x;
    public int y;
    public int width;
    public int height;

    public NoGoZone(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean testIntersect(Rocket r) {
        if(x < r.x && r.x < x + width && y < r.y && r.y < y + height) {
            return false;
        }
        return true;
    }
    
}
