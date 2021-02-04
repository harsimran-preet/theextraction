import java.util.*;
import processing.core.PImage;

public class Quake extends Actions{
    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    public static final String FISH_KEY = "fish";
    public Quake(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod){
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point pos, List<PImage> imgs)
    {
        return new Quake(QUAKE_ID, pos, imgs, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animations.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT), this.getAnimationPeriod());
    }

}
