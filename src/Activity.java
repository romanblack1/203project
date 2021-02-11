public class Activity extends Action{

    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Executable entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        super.getEntity().execute(this.world, this.imageStore, scheduler);
    }
}
