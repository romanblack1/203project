import processing.core.PImage;

import java.util.List;

public class Atlantis extends Executable{
    private int imageIndex;
    private final int animationPeriod;


    public Atlantis(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public Class getKind(){
        return Atlantis.class;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }


    public void execute(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
