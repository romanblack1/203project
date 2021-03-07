public class BFSNode {
    private Point point;
    private BFSNode prior;

    public BFSNode(Point point, BFSNode prior){
        this.point = point;
        this.prior = prior;
    }

    public boolean equals(Object other)
    {
        return other instanceof BFSNode &&
                ((BFSNode)other).point.getX() == this.point.getX() &&
                ((BFSNode)other).point.getY() == this.point.getY();
    }

    public int hashCode()
    {
        int result = 17;
        result = result * 31 + point.getX();
        result = result * 31 + point.getY();
        return result;
    }

    public Point getPoint() {
        return point;
    }
    public BFSNode getPrior() {
        return prior;
    }
}
