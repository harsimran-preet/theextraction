import processing.core.PImage;
import javax.swing.plaf.basic.*;
import java.lang.*;
import java.util.*;
abstract public class Octo extends Moved{
    protected int resourceLimit;
    protected int resourceCount;

    public int getResourceLimit(){return resourceLimit;}
    public int getResourceCount(){return resourceCount;}

    public Octo(String id, Point pos, List<PImage> imgs, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, pos, imgs, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    abstract  public void execute(WorldModel world, ImageStore imagestor, EventScheduler scheduler);

}
