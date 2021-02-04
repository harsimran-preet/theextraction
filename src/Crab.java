import java.util.*;
import processing.core.PImage;

public class Crab extends  Moved{
    public static final String QUAKE_KEY = "quake";

    public Crab(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod) {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }


    public static Crab createCrab(String id,Point pos,int actionPeriod, int animationPeriod,  List<PImage> imgs){
        return new Crab(id, pos, imgs, actionPeriod, animationPeriod);
    }
    public boolean moveToCrab(WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.getPosition(), target.getPosition()))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = this.nextPosition(world, target.getPosition());

         if (!this.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant( nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }
    public void execute(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(this.getPosition(), SGrass.class);
      long nextPeriod = this.getActionPeriod();

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().getPosition();

         if (moveToCrab( world, crabTarget.get(), scheduler))
         {
            Entity quake = Quake.createQuake(tgtPos,
               imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.getActionPeriod();
             ((Quake)quake).scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
         Activity.createActivityAction(this, world, imageStore),
         nextPeriod);
   }


}
