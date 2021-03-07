//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Stream;
//
//public class DepthFirstSearch implements PathingStrategy{
//    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
//        List<Point> path = new LinkedList<>();
//        Point next;
//        for(int i=0;i<4;i++) {
//            //make point
//            switch (i){
//                case(0):
//                    next = new Point(start.getX() +1, start.getY() );
//                    break;
//                case(1):
//                    next = new Point(start.getX() -1, start.getY());
//                    break;
//                case(2):
//                    next = new Point(start.getX(), start.getY()+1);
//                    break;
//                default:
//                    next = new Point(start.getX(), start.getY()-1);
//            }
//            //test if this is a valid grid cell
//            if (canPassThrough.test(next) && !next.searched) {
//                //check if my next neighbor is the goal
//                if (next.equals(end)) {
//                    next.searched = true;
//                    path.add(0, next);
//                    return path;
//                }
//                else {
//                    path.add(next);
//                    next.searched = true;
//                    if (computePath(next, end, canPassThrough, withinReach, potentialNeighbors).size() > 0){
//                        return path;
//                    }
//                }
//            }
//        }
//        start.searched = true;
//        path.remove(start);
//        return new LinkedList<>();
//    }
////
////    private boolean moveOnce(Point pos, GridValues[][] grid, List<Point> path)
////    {
////        Point next;
////        for(int i=0;i<4;i++) {
////            //make point
////            switch (i){
////                case(0):
////                    next = new Point(pos.x +1, pos.y );
////                    break;
////                case(1):
////                    next = new Point(pos.x-1, pos.y);
////                    break;
////                case(2):
////                    next = new Point(pos.x, pos.y+1);
////                    break;
////                default:
////                    next = new Point(pos.x, pos.y-1);
////            }
////            //test if this is a valid grid cell
////            if (withinBounds(next, grid) &&
////                    grid[next.y][next.x] != GridValues.OBSTACLE &&
////                    grid[next.y][next.x] != GridValues.SEARCHED) {
////                //check if my next neighbor is the goal
////                if (grid[next.y][next.x] == GridValues.GOAL) {
////                    grid[pos.y][pos.x] = GridValues.SEARCHED;
////                    path.add(0, next);
////                    return true;
////                }
////                else {
////                    path.add(next);
////                    grid[pos.y][pos.x] = GridValues.SEARCHED;
////                    if (moveOnce(next, grid, path) == true){
////                        return true;
////                    }
////                }
////            }
////        }
////        grid[pos.y][pos.x] = GridValues.SEARCHED;
////        path.remove(pos);
////        return false;
////    }
//}
