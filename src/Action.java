/*
Action: ideally what our various entities might do in our virutal world
 */


import java.util.Optional;
import java.util.Random;

abstract class Action
{
   private final Executable entity;


   public Action(Executable entity)
   {
      this.entity = entity;
   }

   public abstract void executeAction(EventScheduler scheduler);

   public Executable getEntity() {
      return entity;
   }

}
