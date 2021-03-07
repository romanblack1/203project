import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BreadthFirstSearch implements PathingStrategy{

    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        LinkedList<BFSNode> nonSearched = new LinkedList<>();
        HashMap<Integer, BFSNode> searched = new HashMap<>();
        List<Point> path = new LinkedList<>();
        BFSNode next;
        nonSearched.add(new BFSNode(start, null));
        while(!nonSearched.isEmpty()) {
            next = nonSearched.getFirst();
            nonSearched.removeFirst();
            if(withinReach.test(next.getPoint(), end)){
                while(next.getPrior() != null){
                    path.add(next.getPoint());
                    next = next.getPrior();
                }
                Collections.reverse(path);
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(next.getPoint()).collect(Collectors.toList());
            for (Point point: neighbors){
                if(canPassThrough.test(point) &&
                        !(point.equals(end)) &&
                        !searched.containsKey(new BFSNode(point, next).hashCode()) &&
                        !(nonSearched.contains(new BFSNode(point, next)))){
                    nonSearched.add(new BFSNode(point, next));
                }
            }
            searched.put(next.hashCode(), next);
        }
        return path;
    }
}
