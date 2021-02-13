import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Octo_Not_Full extends Octo{
    private final String id;
    private final int actionPeriod;

    public Octo_Not_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, images, resourceLimit, resourceCount, animationPeriod);
        this.actionPeriod = actionPeriod;
        this.id = id;
    }

    public Class getKind(){
        return Octo_Not_Full.class;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(super.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(this, world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    scheduler.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (super.getResourceCount() >= super.getResourceLimit())
        {
            Octo_Full octo = createOctoFull(this.id, super.getResourceLimit(),
                    super.getPosition(), this.actionPeriod, super.getAnimationPeriod(),
                    super.getImages());

            super.transform(octo, world, scheduler, imageStore);

            return true;
        }
        return false;
    }

    private boolean moveToNotFull(Entity octo, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(octo.getPosition(), target.getPosition()))
        {
            ((Octo)octo).setResourceCount(((Octo)octo).getResourceCount()+1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            moveToPartTwo(octo, world, target, scheduler);
            return false;
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, 0), super.getAnimationPeriod());
    }


    public Octo_Full createOctoFull(String id, int resourceLimit,
                                    Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images)
    {
        return new Octo_Full(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }
}
