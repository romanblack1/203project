import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends Executable{
    private final int animationPeriod;
    private final int actionPeriod;

    public Crab(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod)
    {
        super(position, images);
        this.animationPeriod = animationPeriod;
        this.actionPeriod = actionPeriod;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public Class getKind(){
        return Crab.class;
    }

    private static final String QUAKE_KEY = "quake";

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(super.getPosition(), Sgrass.class);
        long nextPeriod = this.actionPeriod;

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(this, world, crabTarget.get(), scheduler))
            {
                Quake quake = world.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    private boolean moveToCrab(Entity crab, WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(crab.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionCrab(world, target.getPosition());

            if (!crab.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(crab, nextPos);
            }
            return false;
        }
    }


    private Point nextPositionCrab(WorldModel world,
                                   Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz,
                super.getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getKind().isInstance(Fish.class))))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getKind().isInstance(Fish.class))))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, 0), this.animationPeriod);
    }


}
