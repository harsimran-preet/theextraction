import java.util.List;
import processing.core.*;
public class Atlantis extends Entity {

    public Atlantis(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod)
    {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }
    public static Atlantis createAtlantis(String id, Point pos, List<PImage> images){
        return new Atlantis(id, pos, images, 0, 0);
    }
}
