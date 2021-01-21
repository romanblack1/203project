import java.util.List;
import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


final class Entity
{
   private final EntityKind kind;
   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;
   private final int resourceLimit;
   private int resourceCount;
   private final int actionPeriod;
   private final int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public EntityKind getKind(){
      return this.kind;
   }
   public Point getPosition(){
      return this.position;
   }
   public int getResourceLimit(){
      return this.resourceLimit;
   }
   public int getResourceCount(){
      return this.resourceCount;
   }
   public int getActionPeriod(){
      return this.actionPeriod;
   }
   public String getId(){
      return this.id;
   }
   public List<PImage> getImages(){
      return this.images;
   }
   public int getImageIndex(){
      return this.imageIndex;
   }

   public void setResourceCount(int resourceCount) {
      this.resourceCount = resourceCount;
   }
   public void setPosition(Point pos){
      this.position = pos;
   }
}
