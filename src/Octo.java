import processing.core.PImage;

import java.util.List;

public abstract class Octo extends ExtraExecutable{

    private final int animationPeriod;
    private int resourceCount;
    private final int resourceLimit;


    public Octo(Point position,
                List<PImage> images, int resourceLimit, int resourceCount, int animationPeriod)
    {
        super(position, images);
        this.resourceCount = resourceCount;
        this.animationPeriod = animationPeriod;
        this.resourceLimit = resourceLimit;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
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

    protected Point nextPosition(WorldModel world, Point destPos){
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
