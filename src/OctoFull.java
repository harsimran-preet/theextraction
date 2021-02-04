import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo {
    public OctoFull(String id, Point pos, List<PImage> images, int resourseLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {
        super(id, pos, images, resourseLimit, resourceCount, actionPeriod, animationPeriod);

    }
    public static OctoFull createOctoFull(String id, int resourceLimit,
      Point position, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new OctoFull(id, position, images,
         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }
    public void execute(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
         Atlantis.class);

      if (fullTarget.isPresent() &&
         moveToFull(world, fullTarget.get(), scheduler))
      {
         //at atlantis trigger animation
         this.scheduleActions( scheduler, world, imageStore);

         //transform to unfull
         this.transformFull( world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
            Activity.createActivityAction(this, world, imageStore),
            this.getActionPeriod());
      }
   }
    public void transformFull( WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      OctoNotFull octo = OctoNotFull.createOctoNotFull(this.getId(), this.getResourceLimit(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getImages());

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity((Entity) octo);
      octo.scheduleActions(scheduler, world, imageStore);
   }

   public boolean moveToFull(WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.getPosition(), target.getPosition()))
      {
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

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

}
