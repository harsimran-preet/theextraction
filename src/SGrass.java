import processing.core.PImage;
import java.util.List;
import java.util.Optional;
public class SGrass extends Animates{

    public static final String FISH_KEY = "fish";
    public static final String FISH_ID_PREFIX = "fish -- ";
    public static final int FISH_CORRUPT_MIN = 20000;
    public static final int FISH_CORRUPT_MAX = 30000;

    public SGrass(String id, Point pos, List<PImage> imgs, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }
    public static SGrass createSgrass(String id, Point position, int actionPeriod,
      List<PImage> images)
   {
      return new SGrass(id, position, images, 0, 0,
         actionPeriod, 0);
   }

    public void execute( WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.getPosition());

      if (openPt.isPresent())
      {
         Entity fish = Fish.createFish(FISH_ID_PREFIX + super.getId(),
                 openPt.get(), FISH_CORRUPT_MIN +
                         VirtualWorld.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
          ((Fish) fish).scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
         Activity.createActivityAction(this, world, imageStore),
         this.getActionPeriod());
   }
}
