import processing.core.PImage;

import java.util.List;

public abstract class Executable extends Entity{
    public Executable(Point position, List<PImage> images) {
        super(position, images);
    }

    public abstract void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler);

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

}
