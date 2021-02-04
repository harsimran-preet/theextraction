
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {
    class Node{
        private int f;
        private int g;
        private int h;
        private Point position;
        private Node prev_node;

        public Node(int g, int h, int f, Point position, Node prev_node){
            this.f = f;
            this.g = g;
            this.h = h;
            this.prev_node = prev_node;
            this.position = position;
        }
        public int getH(){return h;}
        public int getG(){return g;}
        public int getF(){return f;}
        public void setH(int h) {this.h = h;}
        public void setG(int g) {this.g = g;}
        public Point getPosition(){return position;}
        public String toString(){return "getX(): " + this.position.getX() + "getY(): " + this.position.getY();}
        public void setPosition(Point p){position = p;}
        public Node getPrevNode(){return prev_node;}
        public boolean contains(Point p){
            if(this.position == p){
                return true;
            }
            else{
                return false;
            }
        }
    }


    public int calheuristic(Point curr, Point goal) {return Point.distanceSquared(curr, goal);}


    public void printlopen(Queue<Node> lopen){
        lopen.stream().forEach(a -> System.out.println(a));
    }
    public List<Point> computedPath(List<Point> comp, Node win){
        comp.add(win.getPosition());
        if(win.getPrevNode() == null)
        {
            Collections.reverse(comp);
            return comp;
        }
        return computedPath(comp, win.getPrevNode());
    }






    public List<Point> computePath(Point begin, Point end, Predicate<Point> canPass, BiPredicate<Point, Point> canreach, Function<Point, Stream<Point>> neighbors) {

        List<Point> computedlist = new ArrayList<>();
        Map<Point, Node> openm = new HashMap<Point, Node>();
        Map<Point, Node> closem = new HashMap<Point, Node>();

        Queue<Node> lopen = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF));
        Node nstart = new Node(0, calheuristic(begin, end), 0 + calheuristic(begin, end), begin, null);

        lopen.add(nstart);
        Node curr = null;

        while (!lopen.isEmpty()) {
            curr = lopen.remove();
            if (canreach.test(curr.getPosition(), end)) {
                return computedPath(computedlist, curr);
            }
            List<Point> neighborslist = neighbors.apply(curr.getPosition()).filter(canPass).filter(p -> !p.equals(begin) && !p.equals(end)).collect(Collectors.toList());

            for (Point aneighbor : neighborslist) {
                if (!closem.containsKey(aneighbor)) {
                    int tempg = curr.getG() + 1;

                    if (openm.containsKey(aneighbor)) {
                        if (tempg < openm.get(aneighbor).getG()) {
                            Node goodnode = new Node(tempg, calheuristic(aneighbor, end), calheuristic(aneighbor, end) + tempg, aneighbor, curr);
                            lopen.add(goodnode);
                            lopen.remove(openm.get(aneighbor));
                            openm.replace(aneighbor, goodnode);
                        }
                    } else {
                        Node nodeneighbor = new Node(curr.getG() + 1, calheuristic(aneighbor, end), curr.getG() + 1 + calheuristic(aneighbor, end), aneighbor, curr);

                        lopen.add(nodeneighbor);
                        openm.put(aneighbor, nodeneighbor);
                    }
                }
                closem.put(curr.getPosition(), curr);
            }


        }
        return computedlist;

    }
}

