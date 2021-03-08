import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends ExtraExecutable{


    public Crab(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }
    private static final String QUAKE_KEY = "quake";

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget;
        super.checkTarget(world);
        if(super.getTarget().isEmpty()){
            crabTarget = world.findNearest(super.getPosition(), Sgrass.class);
            super.setTarget(crabTarget);
        }
        else{
            crabTarget = super.getTarget();
        }
        long nextPeriod = super.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(this, world, crabTarget.get(), scheduler))
            {
                Quake quake = createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    private boolean moveToCrab(Entity crab, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(crab.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            super.setStrategy(new SingleStepPathingStrategy());
            moveToPartTwo(crab, world, target, scheduler);
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

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images,
                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
}
