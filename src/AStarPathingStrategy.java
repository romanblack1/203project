import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Comparator<Point> pointComparator = (p1,p2) -> p1.getFvalue() - p2.getFvalue();
        PriorityQueue<Point> openList = new PriorityQueue<Point>(pointComparator);
        HashMap<Integer, Point> closedList = new HashMap<Integer, Point>();

        List<Point> path = new LinkedList<>();
        Point currentPoint = start;
        openList.add(currentPoint);

        while(!openList.isEmpty()) { //try all accessible points before giving up
            currentPoint = openList.peek();
            List<Point> neighbors = potentialNeighbors.apply(currentPoint).collect(Collectors.toList());
            for (Point p : neighbors) { //for all neighbors
                if (canPassThrough.test(p) && !closedList.containsKey(currentPoint.hashCode())) { //test for validity
//                    if(withinReach.test(p,end)){
//                        path.add(p);
//                        while(currentPoint.prior != null){
//                            path.add(currentPoint);
//                            currentPoint = currentPoint.prior;
//                        }
//                        return path.stream().sorted(pointComparator).collect(Collectors.toList());
//                    }
                    if (p.equals(end)){
                        while (currentPoint.getPrior() != null){
                            path.add(currentPoint);
                            currentPoint = currentPoint.getPrior();
                        }
                        return path.stream().sorted(pointComparator).collect(Collectors.toList());
                    }
//                    if (currentPoint.equals(end)){
//                        currentPoint = currentPoint.prior;
//                        while (currentPoint.prior != null){
//                            path.add(currentPoint);
//                            currentPoint = currentPoint.prior;
//                        }
//                        return path.stream().sorted(pointComparator).collect(Collectors.toList());
//                    }
                    int gvalue = (int) (10 * Math.sqrt(Math.pow(p.getX() - currentPoint.getX(),2)
                            + (Math.pow(p.getY() - currentPoint.getY(),2))) + currentPoint.getGvalue()); //calculate dist to start
                    if (!openList.contains(p)) { //if openlist does not contain it
                        p.setGvalue(gvalue);
                        p.setHvalue(Math.abs(p.getX()- end.getX()) + Math.abs(p.getY() - end.getY()));
                        p.setFvalue(p.getGvalue() + p.getHvalue());
                        p.setPrior(currentPoint);
                        openList.add(p);
                    }
                    else {
                        if (p.getGvalue() > gvalue) { //if openlist contains it, and the new gvalue is better
                            p.setGvalue(gvalue);
                            if(p.getHvalue() == 0){
                                p.setHvalue(Math.abs(p.getX()- end.getX()) + Math.abs(p.getY() - end.getY()));
                            }
                            p.setFvalue(p.getGvalue() + p.getHvalue());
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
