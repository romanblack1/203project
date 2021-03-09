import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Atlantis extends Animatable{

    private SkeletonFactory factory;

    public Atlantis(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());
        this.factory = new SkeletonFactory("skele", 5, 6,
                imageStore.getImageList("skeleton"));

        if (openPt.isPresent())
        {
            Skeleton skeleton = factory.create(openPt.get());
            world.addEntity(skeleton);
            skeleton.scheduleActions(scheduler, world, imageStore);
        }
    }


    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                super.getAnimationPeriod());
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                super.getActionPeriod());

    }

}
