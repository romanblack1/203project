import processing.core.PImage;

import java.util.List;

public class QuakeFactory {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    List<PImage> images;


    public QuakeFactory(List<PImage> images){
        this.images = images;
    }

    public Quake create(Point position)
    {
        return new Quake(QUAKE_ID, position, images,
                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }


}
