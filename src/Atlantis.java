import processing.core.PImage;

import java.util.List;

public class Atlantis extends Animatable{

    public Atlantis(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
        scheduler.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
        super.getAnimationPeriod());
    }


}
