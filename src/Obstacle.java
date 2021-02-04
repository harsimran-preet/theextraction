import processing.core.PImage;
import java.util.List;

public class Obstacle extends Entity{
    public Obstacle(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod) {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(String id, Point pos, List<PImage> imgs){
        return new Obstacle(id, pos, imgs, 0, 0);
    }
}
