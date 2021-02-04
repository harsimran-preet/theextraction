public class Activity extends Action {
    public Activity(Entity entity, WorldModel world, ImageStore imagestore, int repeat){
        super(entity, world, imagestore, repeat);
    }
    public static  Activity createActivityAction(Entity entity, WorldModel world, ImageStore imagestore){
        return new Activity(entity, world, imagestore, 0);
    }
    public void executeAction(EventScheduler scheduler){
        if (this.entity instanceof OctoFull){
            ((OctoFull) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof OctoNotFull){
            ((OctoNotFull) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Fish){
            ((Fish) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Zombie){
            ((Zombie) this.entity).execute(this.world, this.imageStore, scheduler);
        }

        if (this.entity instanceof Crab){
            ((Crab) this.entity).execute(this.world, this.imageStore, scheduler);
        }

        if (this.entity instanceof Quake){
            ((Quake) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof SGrass){
            ((SGrass) this.entity).execute(this.world, this.imageStore, scheduler);
        }

    }
}
