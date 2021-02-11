import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Quake extends Executable{
    private final int animationPeriod;

    public Quake(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
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


}
