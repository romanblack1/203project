public class Animation extends Action{

    private final int repeatCount;
    private final Animatable entity;

    public Animation(Animatable entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    scheduler.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    (this.entity).getAnimationPeriod());
        }
    }
}
