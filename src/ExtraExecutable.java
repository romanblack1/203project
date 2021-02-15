import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class ExtraExecutable extends Animatable{

    public ExtraExecutable(Point position, List<PImage> images) {
        super(position, images);
    }

    protected void moveToPartTwo(Entity crab, WorldModel world, Entity target, EventScheduler scheduler){

        Point nextPos = nextPosition(world, target.getPosition());
        if (!crab.getPosition().equals(nextPos))
        {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(crab, nextPos);
        }
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);


}
