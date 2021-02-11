import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Sgrass extends Executable{
    private final String id;

    public Sgrass(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.id = id;
    }

    public int getAnimationPeriod()
    {
        throw new UnsupportedOperationException(
                String.format("getAnimationPeriod not supported for SGrass"));
    }

    public Class getKind(){
        return Sgrass.class;
    }

    private static final Random rand = new Random();

    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent())
        {
            Entity fish = world.createFish(FISH_ID_PREFIX + this.id,
                    openPt.get(), FISH_CORRUPT_MIN +
                            rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            scheduler.scheduleActions(fish, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
    }

}
