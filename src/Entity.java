import java.util.List;
import java.util.Optional;

import processing.core.PImage;
/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


abstract public class Entity
{



   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   public int actionPeriod;
   private int animationPeriod;

   public Entity(String id, Point position,
      List<PImage> images,
      int actionPeriod, int animationPeriod)
   {

      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }
   public int getActionPeriod(){return actionPeriod;}
   public List<PImage> getImages(){return this.images;}
   public void setPosition(Point p) {this.position = p;}
   public Point getPosition(){return this.position;}
   public int getImageIndex(){return imageIndex;}
   public void nextImage(){this.imageIndex = (this.getImageIndex() + 1) % this.getImages().size();}
   public String getId(){return id;}

   public int getAnimationPeriod() {
      return animationPeriod;
   }
   public PImage getCurrentImage(){return images.get(imageIndex);}
   public void setImageIndex(int a){imageIndex = a;}
   public void setActionPeriod(int x) {actionPeriod = x;
   }
   public void setImages(List<PImage> images){this.images = images;}
   public void setAnimationPeriod(int x) {animationPeriod = x;}
   public String toString(){return id;}

}






