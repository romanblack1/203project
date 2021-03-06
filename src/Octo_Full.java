import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Octo_Full extends Octo{
    private final String id;

    public Octo_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
        this.id = id;
    }


    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull(this, world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((Executable)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, scheduler.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Octo_Not_Full octo = Create.octoNotFull(id, super.getResourceLimit(),
                super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                super.getImages());
        super.transform(octo, world, scheduler, imageStore);
    }


    private boolean moveToFull(Entity octo, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(octo.getPosition(), target.getPosition()))
        {
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
                super.getActionPeriod());
        scheduler.scheduleEvent(this, scheduler.createAnimationAction(this, 0),
                super.getAnimationPeriod());
    }


}
