import java.util.List;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.*;

public class Moved extends Actions{
    public Moved(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod)
    {
        super(id, pos, imgs, actionPeriod, animationPeriod);
    }
    public Point nextPosition(WorldModel world,
      Point destPos)
   {
       PathingStrategy aStar = new AStarPathingStrategy();
       List<Point> nextP = aStar.computePath(getPosition(), destPos, canPass(world), canreach(), PathingStrategy.CARDINAL_NEIGHBORS);
       if (nextP.size() == 0){
           return getPosition();

       }
       return nextP.get(1);
//      int horiz = Integer.signum(destPos.x - this.getPosition().x);
//      Point newPos = new Point(this.getPosition().x + horiz,
//         this.getPosition().y);
//
//      Optional<Entity> occupant = world.getOccupant(newPos);
//
//      if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
//      {
//          int vert = Integer.signum(destPos.y - this.getPosition().y);
//          newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//          occupant = world.getOccupant(newPos);
//          if ((vert == 0) || (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
//          {
//              newPos = this.getPosition();
//          }
//      }
//
//      return newPos;
   }
   private static BiPredicate<Point, Point> canreach(){return Point::adjacent;}
   private static Predicate<Point> canPass(WorldModel world){
        return a->(world.withinBounds(a) && !world.isOccupied(a));
   }
}
