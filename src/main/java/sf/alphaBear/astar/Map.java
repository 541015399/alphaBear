package sf.alphaBear.astar;

import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;
import sf.alphaBear.httpio.pojo.Wall;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by 01368138 on 2017/11/30.
 */
public class Map<T extends AbstractNode> {

    protected static boolean CAN_MOVE_DIAGONALLY = false;

    private T[][] nodes;

    protected int width;

    protected int height;

    private NodeFactory nodeFactory;

    public Map(int width, int height, NodeFactory nodeFactory) {

        this.nodeFactory = nodeFactory;

        nodes = (T[][]) new AbstractNode[width][height];
        this.width = width - 1;
        this.height = height - 1;
        initEmptyNodes();
    }

    private void initEmptyNodes() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                nodes[i][j] = (T) nodeFactory.createNode(i, j);
            }
        }
    }

    public void setWalkable(int x, int y, boolean walkable) {
        nodes[x][y].setWalkable(walkable);
    }

    public final T getNode(int x, int y) {
        return nodes[x][y];
    }


    public void setWalls(List<Wall> walls) {
        for (int i = 0; i < walls.size(); i++) {
            int x = walls.get(i).getX();
            int y = walls.get(i).getY();

            nodes[x][y].setWalkable(false);
        }
    }

    public void drawMap() {

        for (int i = 0; i <= width; i++) {
            print(" _");
        }
        print("\n");

        for (int j = 0; j <= height; j++) {
            print("|");
            for (int i = 0; i <= width; i++) {
                if (nodes[i][j].isWalkable()) {
                    print(" *");
                } else {
                    print(" #");
                }
            }
            print("|\n");
        }

        for (int i = 0; i <= width; i++) {
            print(" -");
        }
    }

    private void print(String s) {
        System.out.print(s);
    }

    private List<T> openList;
    private List<T> closedList;
    private boolean done = false;

    public final List<T> findPath(int oldX, int oldY, int newX, int newY) {


        openList = new LinkedList<T>();
        closedList = new LinkedList<T>();

        openList.add(nodes[oldX][oldY]);

        done = false;

        T current;

        while (!done) {
            current = lowestFInOpen();
            closedList.add(current);
            openList.remove(current);

            if ((current.getXPosition() == newX) && (current.getYPosition() == newY)) {
                return calcPath(nodes[oldX][oldY], current);
            }

            List<T> adjacentNodes = getAdjacent(current);

            for (int i = 0; i < adjacentNodes.size(); i++) {

                T currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) {
                    currentAdj.setPrevious(current);
                    currentAdj.setHCosts(nodes[newX][newY]);
                    currentAdj.setGCosts(current);
                    openList.add(currentAdj);
                } else {
                    if (currentAdj.getGCosts() > currentAdj.calculateGCosts(current)) {
                        currentAdj.setPrevious(current);
                        currentAdj.setGCosts(current);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new LinkedList<T>();
            }
        }

        return null;
    }

    private List<T> calcPath(T start, T goal) {
        LinkedList<T> path = new LinkedList<T>();

        T curr = goal;
        boolean done = false;

        while (!done) {
            path.addFirst(curr);
            curr = (T) curr.getPrevious();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }

    private T lowestFInOpen() {
        T cheapest = openList.get(0);

        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getFCosts() < cheapest.getFCosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private void addAdjacentNode(T temp, List<T> tempAdjList, boolean diagonally) {
        if(temp.isWalkable() && !closedList.contains(temp)) {
            temp.setIsDiagonally(diagonally);
            tempAdjList.add(temp);
        }
    }

    private List<T> getAdjacent(T node) {
        int x = node.getXPosition();
        int y = node.getYPosition();

        List<T> adj = new LinkedList<T>();

        T temp;

        if (x > 0) {
            temp = this.getNode((x - 1), y);
            addAdjacentNode(temp, adj, false);
        }

        if (x < width) {
            temp = this.getNode((x + 1), y);
            addAdjacentNode(temp, adj,false);
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            addAdjacentNode(temp, adj, false);
        }

        if (y < height) {
            temp = this.getNode(x, (y + 1));
            addAdjacentNode(temp, adj, false);
        }

        if (CAN_MOVE_DIAGONALLY) {
            if (x < width && y < height) {
                temp = this.getNode((x + 1), (y + 1));
                addAdjacentNode(temp, adj, true);
            }

            if (x > 0 && y > 0) {
                temp = this.getNode((x - 1), (y - 1));
                addAdjacentNode(temp, adj, true);
            }

            if (x > 0 && y < height) {
                temp = this.getNode((x - 1), (y + 1));
                addAdjacentNode(temp, adj, true);
            }

            if (x < width && y > 0) {
                temp = this.getNode((x + 1), (y - 1));
                addAdjacentNode(temp, adj, true);
            }
        }
        return adj;
    }
}
