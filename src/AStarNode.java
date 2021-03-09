public class AStarNode {
    private final Point point;
    private int g;
    private int h;
    private int f;
    private AStarNode prior;

    public AStarNode(Point point){
        this.point = point;
    }

    public boolean equals(Object other)
    {
        return other instanceof AStarNode &&
                ((AStarNode)other).point.getX() == this.point.getX() &&
                ((AStarNode)other).point.getY() == this.point.getY();
    }

    public int hashCode()
    {
        int result = 17;
        result = result * 33 + point.getX()*2;
        result = result * 29 + point.getY()*5;
        result = result * 21 + this.h;
        return result;
    }

    public Point getPoint() {
        return point;
    }
    public AStarNode getPrior() {
        return prior;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public int getF() {
        return f;
    }
    public void setF(int f) {
        this.f = f;
    }
    public void setG(int g) {
        this.g = g;
    }
    public void setH(int h) {
        this.h = h;
    }
    public void setPrior(AStarNode prior) {
        this.prior = prior;
    }
}
