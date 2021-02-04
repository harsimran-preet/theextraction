import java.util.*;
import processing.core.PImage;

abstract public class Animates extends Actions {
    public Animates(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod)
    {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imagestore){
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imagestore), this.getActionPeriod());
    }
}
