import java.util.*;
import processing.core.PImage;

public class OctoNotFull extends Octo {
    public OctoNotFull(String id, Point pos, List<PImage> imgs, int resourceLimit, int resouceCount, int actionPeriod, int animationPeriod) {
        super(id, pos, imgs, resourceLimit, resouceCount, actionPeriod, animationPeriod);
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit, Point pos, int actionPeriod, int animationPeriod, List<PImage> imgs){
        return new OctoNotFull(id, pos, imgs, resourceLimit, 0, actionPeriod, animationPeriod);

    }
       public boolean transformNotFull(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.getResourceCount() >= this.getResourceLimit())
      {
         Entity octo = OctoFull.createOctoFull(this.getId(), this.getResourceLimit(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),this.getImages());

         world.removeEntity((Entity) this);
         scheduler.unscheduleAllEvents((Entity) this);

         world.addEntity(octo);
          ((OctoFull) octo).scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }

       public boolean moveToNotFull(WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.getPosition(), target.getPosition()))
      {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = this.nextPosition(world, target.getPosition());

         if (!this.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity((Entity) this, nextPos);
         }
         return false;
      }
   }
       public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
         Fish.class);

      if (!notFullTarget.isPresent() ||
         !moveToNotFull(world, notFullTarget.get(), scheduler) ||
         !transformNotFull( world, scheduler, imageStore))
      {
         scheduler.scheduleEvent((Entity) this,
            Activity.createActivityAction((Entity) this, world, imageStore),
            this.getActionPeriod());
      }
   }
}
