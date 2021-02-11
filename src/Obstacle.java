import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Obstacle extends Entity{

    public Obstacle(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
    }

    public int getAnimationPeriod()
    {
        throw new UnsupportedOperationException(
                String.format("getAnimationPeriod not supported for Obstacle"));
    }

    public Class getKind(){
        return Obstacle.class;
    }

}
