import processing.core.*;
import java.util.*;
public class Fish extends Actions {

    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);

    }

    public static Fish createFish(String id, Point pos, int acitonPeriod, List<PImage> images){
        return new Fish(id, pos, images, acitonPeriod, 0);
    }

    public void execute(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.getPosition();  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = Crab.createCrab(this.getId() + CRAB_ID_SUFFIX,
              pos, this.getActionPeriod() / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      VirtualWorld.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
       ((Crab) crab).scheduleActions(scheduler, world, imageStore);
   }


}
