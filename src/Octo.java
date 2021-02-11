import processing.core.PImage;

import java.util.List;

public abstract class Octo extends Executable{

    private final int animationPeriod;
    private int resourceCount;

    public Octo(Point position, List<PImage> images, int actionPeriod, int resourceCount, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.resourceCount = resourceCount;
        this.animationPeriod = animationPeriod;
    }

    int getAnimationPeriod() {
        return animationPeriod;
    }

    abstract Class getKind();

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }



}
