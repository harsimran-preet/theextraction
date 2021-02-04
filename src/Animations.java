import java.util.List;

public class Animations extends Action {
    public Animations(Entity entity, WorldModel world, ImageStore imagestore, int repeatCount)
    {
        super(entity, world, imagestore, repeatCount);
    }
    public static Animations createAnimationAction(Entity entity, int repeatCount){
        return new Animations(entity, null, null, repeatCount);
    }
    public void executeAction(EventScheduler scheduler){
        this.entity.nextImage();
        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity, createAnimationAction(this.entity, Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
        }
    }
}
