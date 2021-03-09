import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Comparator<AStarNode> nodeComparator = (p1, p2) -> p1.getF() - p2.getF();
        PriorityQueue<AStarNode> openList = new PriorityQueue<AStarNode>(nodeComparator);
        HashMap<Integer, AStarNode> closedList = new HashMap<Integer, AStarNode>();

        List<Point> path = new LinkedList<>();
        AStarNode currentPoint = new AStarNode(start);
        openList.add(currentPoint);

        while(!openList.isEmpty()) {//try all accessible points before giving up
            currentPoint = openList.peek();
            if(withinReach.test(currentPoint.getPoint(), end)){
                while (currentPoint.getPrior() != null){
                    path.add(currentPoint.getPoint());
                    currentPoint = currentPoint.getPrior();
                }
                Collections.reverse(path);
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(currentPoint.getPoint()).collect(Collectors.toList());
            for (Point po : neighbors) { //for all neighbors
                AStarNode p = new AStarNode(po);
                if (canPassThrough.test(po) &&
                        !(po.equals(end)) &&
                        !closedList.containsKey(currentPoint.hashCode())) { //test for validity
//                    if (p.point.equals(end)){
//                        while (currentPoint.prior != null){
//                            path.add(currentPoint.point);
//                            currentPoint = currentPoint.prior;
//                        }
//                        return path;
//                    }
                    int gvalue = (int) (10 * Math.sqrt(Math.pow(p.getPoint().getX() - currentPoint.getPoint().getX(),2)
                            + (Math.pow(p.getPoint().getY() - currentPoint.getPoint().getY(),2))) +
                            currentPoint.getG()); //calculate dist to start
                    if (!openList.contains(p)) { //if openlist does not contain it
                        p.setG(gvalue);
                        p.setH(Math.abs(p.getPoint().getX()- end.getX()) + Math.abs(p.getPoint().getY() - end.getY()));
                        p.setF(p.getG() + p.getH());
                        p.setPrior(currentPoint);
                        openList.add(p);
                    }
                    else {
                        if (p.getG() > gvalue) { //if openlist contains it, and the new gvalue is better
                            p.setG(gvalue);
                            if(p.getH() == 0){
                                p.setH(Math.abs(p.getPoint().getX() - end.getX()) + Math.abs(p.getPoint().getY()
                                        - end.getY()));
                            }
                            p.setF(p.getG() + p.getH());
                            p.setPrior(currentPoint);
                        }
                    }
                }
            }
            closedList.put(currentPoint.hashCode(), currentPoint);
            openList.remove(currentPoint);
        }
        return path;
    }

}
