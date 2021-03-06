import processing.core.PImage;

import java.util.List;

public abstract class Animatable extends Executable{

    private final int animationPeriod;

    public Animatable(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }

}
