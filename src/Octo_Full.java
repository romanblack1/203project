import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Octo_Full extends Octo{
    private final String id;
    private final int resourceLimit;

    public Octo_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, resourceCount, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }


    public Class getKind(){
        return Octo_Full.class;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(),
                "Atlantis");

        if (fullTarget.isPresent() &&
                moveToFull(this, world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            scheduler.scheduleActions(fullTarget.get(), world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, scheduler.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        Entity octo = world.createOctoNotFull(this.id, this.resourceLimit,
                super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                super.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        scheduler.scheduleActions(octo, world, imageStore);
    }


    private boolean moveToFull(Entity octo, WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(octo.getPosition(), target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionOcto(world, target.getPosition());

            if (!octo.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(octo, nextPos);
            }
            return false;
        }
    }

    private Point nextPositionOcto(WorldModel world,
                                   Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz,
                super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(),
                    super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }


}
