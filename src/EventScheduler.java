import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

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
      List<Event> pending = this.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime(long time)
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().time < time)
      {
         Event next = this.eventQueue.poll();

         this.removePendingEvent(next);

         next.action.executeAction(this);
      }
   }


   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
   public void scheduleActions(Entity entity, WorldModel world, ImageStore imageStore)
   {
      switch (entity.getKind())
      {
         case OCTO_FULL:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity, createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case FISH:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;

         case CRAB:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case QUAKE:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            this.scheduleEvent(entity,
                    createAnimationAction(entity, QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case SGRASS:
            this.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;
         case ATLANTIS:
            this.scheduleEvent(entity,
                    createAnimationAction(entity, ATLANTIS_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         default:
      }
   }

   public Action createAnimationAction(Entity entity, int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
   }

   public Action createActivityAction(Entity entity, WorldModel world,
                                             ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
   }


}
