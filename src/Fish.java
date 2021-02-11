import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Fish extends Executable{
    private final String id;
    private int imageIndex;

    public Fish(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.id = id;
    }

    public int getAnimationPeriod()
    {
        throw new UnsupportedOperationException(
                String.format("getAnimationPeriod not supported for Fish"));
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

        Entity crab = world.createCrab(this.id + CRAB_ID_SUFFIX,
                pos, super.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        scheduler.scheduleActions(crab, world, imageStore);
    }



}
