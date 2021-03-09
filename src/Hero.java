import processing.core.PImage;
import java.util.List;

public class Hero extends ExtraExecutable{
    public Hero(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler) {

    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }
}
