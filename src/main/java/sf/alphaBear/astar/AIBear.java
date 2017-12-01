package sf.alphaBear.astar;

import java.util.List;

/**
 * Created by 01368138 on 2017/11/30.
 */
public class AIBear {

    public static void main(String[] args) {
        Map<Node> myMap  =  new Map<Node>(10, 10, new NodeFactory());

        myMap.drawMap();

        List<Node> path = myMap.findPath(0 ,0, 4,7);

        System.out.println("\nSolution: ");
        for (int i = 0; i < path.size(); i++) {
            if(i < path.size() - 1) {
                System.out.print("(" + path.get(i).getXPosition() + ", " + path.get(i).getYPosition() + ") -> ");
            } else {
                System.out.print("(" + path.get(i).getXPosition() + ", " + path.get(i).getYPosition() + ").");
            }
        }
    }
}
