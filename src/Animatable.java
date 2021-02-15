import processing.core.PImage;

import java.util.List;

public abstract class Animatable extends Executable{

    public Animatable(Point position, List<PImage> images) {
        super(position, images);
    }

    public abstract int getAnimationPeriod();

}
