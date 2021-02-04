import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Zombie extends Moved {

    //this will turn into Zombie when in motion
    public static final String NINJA_KEY = "ninja";



    public Zombie(String id, Point pos, List<PImage> imgs, int actionPeriod, int animationPeriod) {
        super(id,pos,imgs, actionPeriod,animationPeriod);
    }
    public static Zombie createZombie(String id, Point pos, int actionPeriod, int animationPeriod, List<PImage> imgs){
        return new Zombie(id, pos, imgs, actionPeriod, animationPeriod);
    }




    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> ZombieT = world.findNearest(this.getPosition(), Crab.class);
        long next = this.getActionPeriod();
        if(ZombieT.isPresent()){
            Point togo = ZombieT.get().getPosition();
            if(moveTo(world, ZombieT.get(), scheduler, imageStore)){
//                Entity q = Quake.createQuake(togo, imageStore.getImageList(Crab.QUAKE_KEY));
//                world.addEntity(q);
//                next += this.getActionPeriod();
//                ((Quake)q).scheduleActions(scheduler, world, imageStore);

            }

        }
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world,imageStore), next);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore){
        if (Point.adjacent(this.getPosition(), target.getPosition()) &&world.getOccupant(target.getPosition()).isPresent()){
            Crab ninja = (Crab)(world.getOccupant(target.getPosition()).get());
            ninja.setImageIndex(3);
            ninja.setImages(imageStore.getImageList(NINJA_KEY));
            ninja.setActionPeriod(5);
            ninja.setActionPeriod(5);
            return true;
        }
        else{
            Point next = this.nextPosition(world, target.getPosition());
            if(!this.getPosition().equals(next))
            {
                Optional<Entity> occ = world.getOccupant(next);
                if(occ.isPresent())
                {
                    scheduler.unscheduleAllEvents(occ.get());
                }
                world.moveEntity(this, next);
            }
            return false;
        }
    }
}
