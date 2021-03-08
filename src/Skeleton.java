import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Skeleton extends ExtraExecutable{

    public Skeleton(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> skeleTarget;
        super.checkTarget(world);
        if(super.getTarget().isEmpty()){
            skeleTarget = world.findNearest(super.getPosition(),
                    Crab.class);
            super.setTarget(skeleTarget);
        }
        else{
            skeleTarget = super.getTarget();
        }
        if (skeleTarget.isPresent())
        {
            if(moveToSkeleton(this, world, skeleTarget.get(), scheduler)){
                setTarget(world.findNearest(super.getPosition(),
                        Crab.class));
            }
        }
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
//        System.out.println(skeleTarget);
//        if (!skeleTarget.isPresent() || !moveToSkeleton(this, world, skeleTarget.get(), scheduler))
//        {
//            scheduler.scheduleEvent(this,
//                    scheduler.createActivityAction(this, world, imageStore),
//                    super.getActionPeriod());
//        }
    }

    private boolean moveToSkeleton(Skeleton skele, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(skele.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            super.setStrategy(new AStarPathingStrategy());
            moveToPartTwo(skele, world, target, scheduler);
            return false;
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, 0), super.getAnimationPeriod());
    }
}