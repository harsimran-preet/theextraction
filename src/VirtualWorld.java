import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 1280;
   private static final int VIEW_HEIGHT = 720;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;
   public static final Random rand = new Random();
   public static final int COLOR_MASK = 0xffffff;
   public static final int PROPERTY_KEY = 0;

   private static double timeScale = 1.0;

   private static final String PERFORMANCE_SERUM_KEY = "performanceserum";
   private static final String KNIGHT_KEY = "knight";
   private static final String ZOMBIE_KEY = "zombie";
   private static final String WOLF_KEY = "wolf";


   public static final String OCTO_KEY = "octo";
   public static final int OCTO_NUM_PROPERTIES = 7;
   public static final int OCTO_ID = 1;
   public static final int OCTO_COL = 2;
   public static final int OCTO_ROW = 3;
   public static final int OCTO_LIMIT = 4;
   public static final int OCTO_ACTION_PERIOD = 5;
   public static final int OCTO_ANIMATION_PERIOD = 6;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String FISH_KEY = "fish";
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;

   public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;
//   public static final int ATLANTIS_ANIMATION_PERIOD = 70;
//   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   public static final String SGRASS_KEY = "seaGrass";
   public static final int SGRASS_NUM_PROPERTIES = 5;
   public static final int SGRASS_ID = 1;
   public static final int SGRASS_COL = 2;
   public static final int SGRASS_ROW = 3;
   public static final int SGRASS_ACTION_PERIOD = 4;

   private boolean click;
   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;


   public long next_time;


   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
      click = false;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      this.view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         this.view.shiftView(dx, dy);
      }
   }

   private void aclick(int wolfAnimationPeriod, int wolfActionPeriod) {
      Zombie creating = Zombie.createZombie(ZOMBIE_KEY, getPressed(), 0, 0, imageStore.getImageList(ZOMBIE_KEY));
      world.addEntity(creating);
      (creating).scheduleActions(scheduler, world, imageStore);
      Background performanceserum = new Background(PERFORMANCE_SERUM_KEY, imageStore.getImageList(PERFORMANCE_SERUM_KEY));
      aroundPressed(1).forEach(a-> {
         world.setBackground(a, performanceserum);
         if(isOctoAtPS(a) && world.getOccupant(a).isPresent()) {
            Octo octoatPS = (Octo) (world.getOccupant(a).get());
            octoatPS.setImageIndex(3);
            octoatPS.setImages(imageStore.getImageList(WOLF_KEY));
            octoatPS.setAnimationPeriod(wolfAnimationPeriod);
            octoatPS.setActionPeriod(wolfActionPeriod);
         }
      }
      );
   }

   private List<Point> aroundPressed(int awayfrompressed){
      return Arrays.asList(PressedOffSet(0, awayfrompressed), PressedOffSet(awayfrompressed, 0), PressedOffSet(awayfrompressed, awayfrompressed),
              PressedOffSet(0, -awayfrompressed), PressedOffSet(-awayfrompressed, 0), PressedOffSet(-awayfrompressed, -awayfrompressed), PressedOffSet(awayfrompressed, -awayfrompressed),
              PressedOffSet(-awayfrompressed, awayfrompressed));
   }
   private Point getPressed(){return new Point(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);}
   private Point PressedOffSet (int x, int y){
      return new Point(getPressed().getX()+x, getPressed().getY()+y);
   }
   private boolean isOctoAtPS(Point pos) {
      return world.withinBounds(pos) && (world.getOccupancyCell(pos) instanceof OctoNotFull || world.getOccupancyCell(pos) instanceof OctoFull);
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         ImageStore.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity instanceof Actions)
            ((Actions) entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }
   public void mousePressed(){
      if(!click) {
         aclick(1, 1);

         click = true;
      }
      else{
         spawnknight();
         aclick(1,1);

      }
      redraw();
   }
   private void spawnknight(){
      List<Entity> atlantis = world.getEntities().stream().collect(Collectors.toList());
      List<Atlantis> mainatlantis = new ArrayList<>();
      atlantis.forEach(A -> {
         if(A instanceof Atlantis){
            mainatlantis.add((Atlantis)A);
         }
      });
      List<Atlantis> aknight = new ArrayList<>();
      mainatlantis.forEach(Z -> {aknight.add(Z);});
      aknight.forEach(Z -> Z.setImages(imageStore.getImageList(KNIGHT_KEY)));
   }
   public static void setAlpha(PImage img, int maskColor, int alpha) {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++) {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore) {
      String[] properties = line.split("\\s");
      if (properties.length > 0) {
         switch (properties[PROPERTY_KEY]) {
            case BGND_KEY:
               return parseBackground(properties, world, imageStore);
            case OCTO_KEY:
               return parseOcto(properties, world, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, world, imageStore);
            case FISH_KEY:
               return parseFish(properties, world, imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties, world, imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties, world, imageStore);
         }
      }

      return false;
   }


   public static void load(Scanner in, WorldModel world, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!processLine(in.nextLine(), world, imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         } catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         } catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }


   public static boolean parseBackground(String[] properties,
                                         WorldModel world, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseOcto(String[] properties, WorldModel world,
                                   ImageStore imageStore) {
      if (properties.length == OCTO_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = OctoNotFull.createOctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
      if (properties.length == OBSTACLE_NUM_PROPERTIES) {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseFish(String[] properties, WorldModel world,
                                   ImageStore imageStore) {
      if (properties.length == FISH_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = Fish.createFish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   public static boolean parseAtlantis(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
      if (properties.length == ATLANTIS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = Atlantis.createAtlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   public static boolean parseSgrass(String[] properties, WorldModel world,
                                     ImageStore imageStore) {
      if (properties.length == SGRASS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = SGrass.createSgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }
   

   public static void main(String [] args)
   {

      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
