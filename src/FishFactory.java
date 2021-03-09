import processing.core.PImage;

import java.util.List;

public class FishFactory {

    private final String id;
    private final int actionPeriod;
    private final List<PImage> images;

    public FishFactory(String id, int actionPeriod, List<PImage> images){
        this.id = id;
        this.actionPeriod = actionPeriod;
        this.images = images;
    }

    public Fish create(Point position){
        return new Fish(id, position, images, 0, 0, actionPeriod, 0);
    }

}
