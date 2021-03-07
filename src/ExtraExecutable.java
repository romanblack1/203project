import processing.core.PImage;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class ExtraExecutable extends Animatable{

    private PathingStrategy strategy;

    public ExtraExecutable(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    protected void moveToPartTwo(Entity entity, WorldModel world, Entity target, EventScheduler scheduler){
        Point nextPos;
        List<Point> path = strategy.computePath(entity.getPosition(), target.getPosition(),
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> PathingStrategy.neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if(path.size() != 0){
            nextPos = path.get(0);
        }
        else{
            System.out.println("no path");
            nextPos = entity.getPosition();
        }
        System.out.println(entity.getPosition().getX() + " " + entity.getPosition().getY());
        System.out.println(nextPos.getX() + " " + nextPos.getY());
//        Point nextPos = nextPosition(world, target.getPosition());
        if (!entity.getPosition().equals(nextPos))
        {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(entity, nextPos);
        }
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);

    public void setStrategy(PathingStrategy strategy){
        this.strategy = strategy;
    }
}
