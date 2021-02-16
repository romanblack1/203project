import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Fish extends Executable{
    private final String id;
    private final int actionPeriod;

    public Fish(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod)
    {
        super(position, images);
        this.id = id;
        this.actionPeriod = actionPeriod;
    }

    public Class getKind(){
        return Fish.class;
    }

    private static final Random rand = new Random();

    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = super.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = createCrab(this.id + CRAB_ID_SUFFIX,
                pos, this.actionPeriod / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    private Crab createCrab(String id, Point position,
                           int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images,
                0, 0, actionPeriod, animationPeriod);
    }



}
