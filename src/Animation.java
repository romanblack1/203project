public class Animation extends Action{

    private final int repeatCount;

    public Animation(Executable entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        super.getEntity().nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(super.getEntity(),
                    scheduler.createAnimationAction(super.getEntity(),
                            Math.max(this.repeatCount - 1, 0)),
                    super.getEntity().getAnimationPeriod());
        }
    }
}
