package sf.alphaBear.astar;

/**
 * Created by 01368138 on 2017/11/30.
 */
public class NodeFactory {

    public AbstractNode createNode(int x, int y) {
        return new Node(x, y);
    }
}
