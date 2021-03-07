import processing.core.PImage;

import java.util.List;

public abstract class Octo extends ExtraExecutable{

    private int resourceCount;
    private final int resourceLimit;


    public Octo(Point position, List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod,int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    protected void transform(Octo octo, WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

}
