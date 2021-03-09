import processing.core.PImage;

import java.util.List;

public class SkeletonFactory{

    private final String id;
    private final int actionPeriod;
    private final int animationPeriod;
    private final List<PImage> images;

    public SkeletonFactory(String id, int actionPeriod, int animationPeriod,
                           List<PImage> images){
        this.id = id;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.images = images;
    }

    public Skeleton create(Point position){
        return new Skeleton(id, position, actionPeriod, animationPeriod, images);
    }

}
