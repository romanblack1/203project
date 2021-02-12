import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Quake extends Executable{
    private final int animationPeriod;
    private final int actionPeriod;

    public Quake(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod)
    {
        super(position, images);
        this.animationPeriod = animationPeriod;
        this.actionPeriod = actionPeriod;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public Class getKind(){
        return Quake.class;
    }


    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.animationPeriod);
    }


}
