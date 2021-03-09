import processing.core.PImage;
import java.util.List;
import java.util.stream.Collectors;

public class Hero extends ExtraExecutable{

    QuakeFactory factory;
    private boolean difficulty = false;

    public Hero(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        factory = new QuakeFactory(imageStore.getImageList("quake"));
        List<Point> neighbors;
        Point pos = super.getPosition();
        if (difficulty){
            neighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(pos).collect(Collectors.toList());
        }
        else{
            neighbors = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS.apply(pos).collect(Collectors.toList());
        }
        for (Point p : neighbors) {
            if (world.getOccupant(p).isPresent()) {
                Entity entity = world.getOccupant(p).get();
                if (entity.getClass().equals(Skeleton.class)) {
                    world.removeEntity(entity);
                    scheduler.unscheduleAllEvents(entity);
                    Quake quake = factory.create(p);
                    world.tryAddEntity(quake);
                    quake.scheduleActions(scheduler, world, imageStore);
                }
            }
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    public void setDifficulty(boolean strategy) {
        this.difficulty = strategy;
    }
}
