package sf.alphaBear.astar;

/**
 * Created by 01368138 on 2017/11/30.
 */
public class Node extends AbstractNode {

    public Node(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    public void setHCosts(AbstractNode endNode) {
        this.setHCosts(absolute(this.getXPosition() - endNode.getXPosition())
                + absolute(this.getYPosition() - endNode.getYPosition())
                + BASIC_MOVEMENT_COST);
    }

    private int absolute(int a) {
        return a > 0 ? a : -a;
    }
}
