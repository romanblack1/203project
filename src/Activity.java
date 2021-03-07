public class Activity extends Action{

    private final WorldModel world;
    private final ImageStore imageStore;
    private final Executable entity;

    public Activity(Executable entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.execute(this.world, this.imageStore, scheduler);
    }
}
