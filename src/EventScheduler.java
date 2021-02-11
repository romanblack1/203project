import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   private final PriorityQueue<Event> eventQueue;
   private final Map<Entity, List<Event>> pendingEvents;
   private final double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * this.timeScale);
      Event event = new Event(action, time, entity);

      this.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = this.pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      this.pendingEvents.put(entity, pending);
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime(long time)
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().getTime() < time)
      {
         Event next = this.eventQueue.poll();

         this.removePendingEvent(next);

         next.getAction().executeAction(this);
      }
   }


   private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
   public void scheduleActions(Executable entity, WorldModel world, ImageStore imageStore)
   {
      switch (entity.getKind())
      {
         case Octo_Full.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity, createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
            break;

         case Octo_Not_Full.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case Fish.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;

         case Crab.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case Quake.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case Sgrass.class:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;
         case Atlantis.class:
            this.scheduleEvent(entity,
                    createAnimationAction(entity, ATLANTIS_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         default:
      }
   }

   public Action createAnimationAction(Executable entity, int repeatCount)
   {
      return new Animation(entity, repeatCount);
   }

   public Action createActivityAction(Executable entity, WorldModel world,
                                             ImageStore imageStore)
   {
      return new Activity(entity, world, imageStore);
   }


}
