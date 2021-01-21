/*
Action: ideally what our various entities might do in our virutal world
 */


import java.util.Optional;
import java.util.Random;

final class Action
{
   private final ActionKind kind;
   private final Entity entity;
   private final WorldModel world;
   private final ImageStore imageStore;
   private final int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }



   private static final Random rand = new Random();

   private static final String CRAB_KEY = "crab";
   private static final String CRAB_ID_SUFFIX = " -- crab";
   private static final int CRAB_PERIOD_SCALE = 4;
   private static final int CRAB_ANIMATION_MIN = 50;
   private static final int CRAB_ANIMATION_MAX = 150;

   private static final String QUAKE_KEY = "quake";

   private static final String FISH_KEY = "fish";
   private static final String FISH_ID_PREFIX = "fish -- ";
   private static final int FISH_CORRUPT_MIN = 20000;
   private static final int FISH_CORRUPT_MAX = 30000;


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

   private void executeAnimationAction(EventScheduler scheduler)
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

   private void executeActivityAction(EventScheduler scheduler)
   {
      switch (this.entity.getKind())
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
                            this.entity.getKind()));
      }
   }

   private void executeOctoFullActivity(Entity entity, WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(entity.getPosition(),
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
                 entity.getActionPeriod());
      }
   }

   private void executeOctoNotFullActivity(Entity entity,
                                                 WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(entity.getPosition(),
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(entity, world, notFullTarget.get(), scheduler) ||
              !transformNotFull(entity, world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(entity,
                 scheduler.createActivityAction(entity, world, imageStore),
                 entity.getActionPeriod());
      }
   }

   private void executeFishActivity(Entity entity, WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = entity.getPosition();  // store current position before removing

      world.removeEntity(entity);
      scheduler.unscheduleAllEvents(entity);

      Entity crab = world.createCrab(entity.getId() + CRAB_ID_SUFFIX,
              pos, entity.getActionPeriod() / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
      scheduler.scheduleActions(crab, world, imageStore);
   }

   private void executeCrabActivity(Entity entity, WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(entity.getPosition(), EntityKind.SGRASS);
      long nextPeriod = entity.getActionPeriod();

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().getPosition();

         if (moveToCrab(entity, world, crabTarget.get(), scheduler))
         {
            Entity quake = world.createQuake(tgtPos,
                    imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += entity.getActionPeriod();
            scheduler.scheduleActions(quake, world, imageStore);
         }
      }

      scheduler.scheduleEvent(entity,
              scheduler.createActivityAction(entity, world, imageStore),
              nextPeriod);
   }

   private void executeQuakeActivity(Entity entity, WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(entity);
      world.removeEntity(entity);
   }

   private void executeAtlantisActivity(Entity entity, WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(entity);
      world.removeEntity(entity);
   }

   private void executeSgrassActivity(Entity entity, WorldModel world,
                                            ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(entity.getPosition());

      if (openPt.isPresent())
      {
         Entity fish = world.createFish(FISH_ID_PREFIX + entity.getId(),
                 openPt.get(), FISH_CORRUPT_MIN +
                         rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
         scheduler.scheduleActions(fish, world, imageStore);
      }

      scheduler.scheduleEvent(entity,
              scheduler.createActivityAction(entity, world, imageStore),
              entity.getActionPeriod());
   }


   private boolean transformNotFull(Entity entity, WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (entity.getResourceCount() >= entity.getResourceLimit())
      {
         Entity octo = world.createOctoFull(entity.getId(), entity.getResourceLimit(),
                 entity.getPosition(), entity.getActionPeriod(), entity.getAnimationPeriod(),
                 entity.getImages());

         world.removeEntity(entity);
         scheduler.unscheduleAllEvents(entity);

         world.addEntity(octo);
         scheduler.scheduleActions(octo, world, imageStore);

         return true;
      }

      return false;
   }

   private void transformFull(Entity entity, WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = world.createOctoNotFull(entity.getId(), entity.getResourceLimit(),
              entity.getPosition(), entity.getActionPeriod(), entity.getAnimationPeriod(),
              entity.getImages());

      world.removeEntity(entity);
      scheduler.unscheduleAllEvents(entity);

      world.addEntity(octo);
      scheduler.scheduleActions(octo, world, imageStore);
   }

   private boolean moveToNotFull(Entity octo, WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (octo.getPosition().adjacent(target.getPosition()))
      {
         octo.setResourceCount(octo.getResourceCount()+1);
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(octo, world, target.getPosition());

         if (!octo.getPosition().equals(nextPos))
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

   private boolean moveToFull(Entity octo, WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (octo.getPosition().adjacent(target.getPosition()))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(octo, world, target.getPosition());

         if (!octo.getPosition().equals(nextPos))
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

   private boolean moveToCrab(Entity crab, WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (crab.getPosition().adjacent(target.getPosition()))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionCrab(crab, world, target.getPosition());

         if (!crab.getPosition().equals(nextPos))
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


   private Point nextPositionOcto(Entity entity, WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.getPosition().x);
      Point newPos = new Point(entity.getPosition().x + horiz,
              entity.getPosition().y);

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.y - entity.getPosition().y);
         newPos = new Point(entity.getPosition().x,
                 entity.getPosition().y + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = entity.getPosition();
         }
      }

      return newPos;
   }

   private Point nextPositionCrab(Entity entity, WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - entity.getPosition().x);
      Point newPos = new Point(entity.getPosition().x + horiz,
              entity.getPosition().y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.y - entity.getPosition().y);
         newPos = new Point(entity.getPosition().x, entity.getPosition().y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.FISH)))
         {
            newPos = entity.getPosition();
         }
      }

      return newPos;
   }



}
