/*
Action: ideally what our various entities might do in our virutal world
 */


import java.util.Optional;
import java.util.Random;

abstract class Action
{
   public abstract void executeAction(EventScheduler scheduler);

}
