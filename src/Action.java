/*
Action: ideally what our various entities might do in our virutal world
 */


import java.util.Optional;
import java.util.Random;

final class Action
{
   public ActionKind kind;
   public Entity entity;
   public WorldModel world;
   public ImageStore imageStore;
   public int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }



   public static final Random rand = new Random();

   public static final String CRAB_KEY = "crab";
   public static final String CRAB_ID_SUFFIX = " -- crab";
   public static final int CRAB_PERIOD_SCALE = 4;
   public static final int CRAB_ANIMATION_MIN = 50;
   public static final int CRAB_ANIMATION_MAX = 150;

   public static final String QUAKE_KEY = "quake";

   public static final String FISH_KEY = "fish";
   public static final String FISH_ID_PREFIX = "fish -- ";
   public static final int FISH_CORRUPT_MIN = 20000;
   public static final int FISH_CORRUPT_MAX = 30000;


   public void executeAction(EventScheduler scheduler)
   {
      switch (this.kind)
      {
         case ACTIVITY:
            executeActivityAction(scheduler);
            break;

         case ANIMATION:
            executeAnimationAction(scheduler);
            break;
      }
   }

   public void executeAnimationAction(EventScheduler scheduler)
   {
      this.entity.nextImage();

      if (this.repeatCount != 1)
      {
         scheduler.scheduleEvent(this.entity,
                 scheduler.createAnimationAction(this.entity,
                         Math.max(this.repeatCount - 1, 0)),
                 this.entity.getAnimationPeriod());
      }
   }

   public void executeActivityAction(EventScheduler scheduler)
   {
      switch (this.entity.kind)
      {
         case OCTO_FULL:
            executeOctoFullActivity(this.entity, this.world,
                    this.imageStore, scheduler);
            break;

         case OCTO_NOT_FULL:
            executeOctoNotFullActivity(this.entity, this.world,
                    this.imageStore, scheduler);
            break;

         case FISH:
            executeFishActivity(this.entity, this.world, this.imageStore,
                    scheduler);
            break;

         case CRAB:
            executeCrabActivity(this.entity, this.world,
                    this.imageStore, scheduler);
            break;

         case QUAKE:
            executeQuakeActivity(this.entity, this.world, this.imageStore,
                    scheduler);
            break;

         case SGRASS:
            executeSgrassActivity(this.entity, this.world, this.imageStore,
                    scheduler);
            break;

         case ATLANTIS:
            executeAtlantisActivity(this.entity, this.world, this.imageStore,
                    scheduler);
            break;

         default:
            throw new UnsupportedOperationException(
                    String.format("executeActivityAction not supported for %s",
                            this.entity.kind));
      }
   }

   public void executeOctoFullActivity(Entity entity, WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(entity.position,
              EntityKind.ATLANTIS);

      if (fullTarget.isPresent() &&
              moveToFull(entity, world, fullTarget.get(), scheduler))
      {
         //at atlantis trigger animation
         scheduler.scheduleActions(fullTarget.get(), world, imageStore);

         //transform to unfull
         transformFull(entity, world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(entity,
                 scheduler.createActivityAction(entity, world, imageStore),
                 entity.actionPeriod);
      }
   }

   public void executeOctoNotFullActivity(Entity entity,
                                                 WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(entity.position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(entity, world, notFullTarget.get(), scheduler) ||
              !transformNotFull(entity, world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(entity,
                 scheduler.createActivityAction(entity, world, imageStore),
                 entity.actionPeriod);
      }
   }

   public void executeFishActivity(Entity entity, WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = entity.position;  // store current position before removing

      world.removeEntity(entity);
      scheduler.unscheduleAllEvents(entity);

      Entity crab = world.createCrab(entity.id + CRAB_ID_SUFFIX,
              pos, entity.actionPeriod / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
      scheduler.scheduleActions(crab, world, imageStore);
   }

   public void executeCrabActivity(Entity entity, WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(entity.position, EntityKind.SGRASS);
      long nextPeriod = entity.actionPeriod;

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().position;

         if (moveToCrab(entity, world, crabTarget.get(), scheduler))
         {
            Entity quake = world.createQuake(tgtPos,
                    imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += entity.actionPeriod;
            scheduler.scheduleActions(quake, world, imageStore);
         }
      }

      scheduler.scheduleEvent(entity,
              scheduler.createActivityAction(entity, world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(Entity entity, WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(entity);
      world.removeEntity(entity);
   }

   public void executeAtlantisActivity(Entity entity, WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(entity);
      world.removeEntity(entity);
   }

   public void executeSgrassActivity(Entity entity, WorldModel world,
                                            ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(entity.position);

      if (openPt.isPresent())
      {
         Entity fish = world.createFish(FISH_ID_PREFIX + entity.id,
                 openPt.get(), FISH_CORRUPT_MIN +
                         rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
         scheduler.scheduleActions(fish, world, imageStore);
      }

      scheduler.scheduleEvent(entity,
              scheduler.createActivityAction(entity, world, imageStore),
              entity.actionPeriod);
   }



   public boolean transformNotFull(Entity entity, WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (entity.resourceCount >= entity.resourceLimit)
      {
         Entity octo = world.createOctoFull(entity.id, entity.resourceLimit,
                 entity.position, entity.actionPeriod, entity.animationPeriod,
                 entity.images);

         world.removeEntity(entity);
         scheduler.unscheduleAllEvents(entity);

         world.addEntity(octo);
         scheduler.scheduleActions(octo, world, imageStore);

         return true;
      }

      return false;
   }

   public void transformFull(Entity entity, WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = world.createOctoNotFull(entity.id, entity.resourceLimit,
              entity.position, entity.actionPeriod, entity.animationPeriod,
              entity.images);

      world.removeEntity(entity);
      scheduler.unscheduleAllEvents(entity);

      world.addEntity(octo);
      scheduler.scheduleActions(octo, world, imageStore);
   }

   public boolean moveToNotFull(Entity octo, WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (octo.position.adjacent(target.position))
      {
         octo.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(octo, world, target.position);

         if (!octo.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public boolean moveToFull(Entity octo, WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (octo.position.adjacent(target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(octo, world, target.position);

         if (!octo.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public boolean moveToCrab(Entity crab, WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (crab.position.adjacent(target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionCrab(crab, world, target.position);

         if (!crab.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(crab, nextPos);
         }
         return false;
      }
   }



   public Point nextPositionOcto(Entity entity, WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.position.x);
      Point newPos = new Point(entity.position.x + horiz,
              entity.position.y);

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.y - entity.position.y);
         newPos = new Point(entity.position.x,
                 entity.position.y + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = entity.position;
         }
      }

      return newPos;
   }

   public Point nextPositionCrab(Entity entity, WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.position.x);
      Point newPos = new Point(entity.position.x + horiz,
              entity.position.y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.y - entity.position.y);
         newPos = new Point(entity.position.x, entity.position.y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
         {
            newPos = entity.position;
         }
      }

      return newPos;
   }







}
