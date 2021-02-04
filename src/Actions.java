import processing.core.PImage;
import javax.swing.plaf.basic.*;
import java.lang.reflect.Executable;
import java.util.List;

abstract public class Actions extends Entity{
    public Actions(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod) {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animations.createAnimationAction(this, 0), this.getAnimationPeriod());

    }
}
