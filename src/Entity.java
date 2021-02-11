import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */

public abstract class Entity
{

   private Point position;
   private final List<PImage> images;
   private int imageIndex;
   private final int actionPeriod;

   public Entity(Point position, List<PImage> images, int actionPeriod)
   {
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.actionPeriod = actionPeriod;
   }

   abstract int getAnimationPeriod();

   abstract Class getKind();

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public Point getPosition(){
      return this.position;
   }

   public void setPosition(Point pos){
      this.position = pos;
   }

   public List<PImage> getImages(){
      return this.images;
   }

   public int getImageIndex(){
      return this.imageIndex;
   }

   public int getActionPeriod(){
      return this.actionPeriod;
   }

}
