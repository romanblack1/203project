import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class ExtraExecutable extends Animatable{

    public ExtraExecutable(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    protected void moveToPartTwo(Entity entity, WorldModel world, Entity target, EventScheduler scheduler){

        Point nextPos = nextPosition(world, target.getPosition());
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

}
