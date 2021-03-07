import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Octo_Not_Full extends Octo{
    private final String id;

    public Octo_Not_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
        this.id = id;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget;
        super.checkTarget(world);
        if(super.getTarget().isEmpty()){
            notFullTarget = world.findNearest(super.getPosition(),
                    Fish.class);
            super.setTarget(notFullTarget);
        }
        else{
            notFullTarget = super.getTarget();
        }
        if (!notFullTarget.isPresent() ||
                !moveToNotFull(this, world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    scheduler.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (super.getResourceCount() >= super.getResourceLimit())
        {
            Octo_Full octo = createOctoFull(this.id, super.getResourceLimit(),
                    super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                    super.getImages());

            super.transform(octo, world, scheduler, imageStore);

            return true;
        }
        return false;
    }

    private boolean moveToNotFull(Octo octo, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(octo.getPosition(), target.getPosition()))
        {
            (octo).setResourceCount((octo).getResourceCount()+1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            super.setStrategy(new AStarPathingStrategy());
            moveToPartTwo(octo, world, target, scheduler);
            return false;
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, 0), super.getAnimationPeriod());
    }


    private Octo_Full createOctoFull(String id, int resourceLimit,
                                    Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images)
    {
        return new Octo_Full(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }
}
