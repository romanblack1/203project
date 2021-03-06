import processing.core.PImage;

import java.util.List;

public abstract class Executable extends Entity{
    private final int actionPeriod;

    public Executable(Point position, List<PImage> images, int actionPeriod) {
        super(position, images);
        this.actionPeriod = actionPeriod;
    }

    public abstract void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler);

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public int getActionPeriod(){
        return actionPeriod;
    }
}
