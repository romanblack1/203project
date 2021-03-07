public class AStarNode {
    public Point point;
    public int g;
    public int h;
    public int f;
    public AStarNode prior;

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
        result = result * 31 + point.getX();
        result = result * 31 + point.getY();
        return result;
    }

}
