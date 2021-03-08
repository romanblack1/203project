import processing.core.PImage;

import java.util.List;

public class Create {

    public static Atlantis atlantis(String id, Point position,
                                    List<PImage> images)
    {
        return new Atlantis(id, position, images,
                0, 0, 0, 0);
    }

    public static Skeleton skeleton(String id, Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images){
        return new Skeleton(id, position, actionPeriod, animationPeriod, images);
    }


    public static Octo_Not_Full octoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new Octo_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Obstacle obstacle(String id, Point position,
                                    List<PImage> images)
    {
        return new Obstacle(id, position, images,
                0, 0, 0, 0);
    }

    public static Fish fish(String id, Point position, int actionPeriod,
                           List<PImage> images)
    {
        return new Fish(id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public static Sgrass sgrass(String id, Point position, int actionPeriod,
                                List<PImage> images)
    {
        return new Sgrass(id, position, images, 0, 0,
                actionPeriod, 0);
    }
}
