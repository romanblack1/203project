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
        Comparator<AStarNode> nodeComparator = (p1, p2) -> p1.f - p2.f;
        PriorityQueue<AStarNode> openList = new PriorityQueue<AStarNode>(nodeComparator);
        HashMap<Integer, AStarNode> closedList = new HashMap<Integer, AStarNode>();

        List<Point> path = new LinkedList<>();
        AStarNode currentPoint = new AStarNode(start);
        openList.add(currentPoint);

        while(!openList.isEmpty()) {//try all accessible points before giving up
            currentPoint = openList.peek();
            if(withinReach.test(currentPoint.point, end)){
                while (currentPoint.prior != null){
                    path.add(currentPoint.point);
                    currentPoint = currentPoint.prior;
                }
                Collections.reverse(path);
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(currentPoint.point).collect(Collectors.toList());
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
                    int gvalue = (int) (10 * Math.sqrt(Math.pow(p.point.getX() - currentPoint.point.getX(),2)
                            + (Math.pow(p.point.getY() - currentPoint.point.getY(),2))) + currentPoint.g); //calculate dist to start
                    if (!openList.contains(p)) { //if openlist does not contain it
                        p.g = gvalue;
                        p.h = Math.abs(p.point.getX()- end.getX()) + Math.abs(p.point.getY() - end.getY());
                        p.f = p.g + p.h;
                        p.prior = currentPoint;
                        openList.add(p);
                    }
                    else {
                        if (p.g > gvalue) { //if openlist contains it, and the new gvalue is better
                            p.g = gvalue;
                            if(p.h == 0){
                                p.h = Math.abs(p.point.getX() - end.getX()) + Math.abs(p.point.getY() - end.getY());
                            }
                            p.f = p.g + p.h;
                            p.prior = currentPoint;
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
