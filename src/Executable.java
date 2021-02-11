import processing.core.PImage;

import java.util.List;

public abstract class Executable extends Entity{
    public Executable(Point position, List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);
    }

    public abstract void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler);


}
