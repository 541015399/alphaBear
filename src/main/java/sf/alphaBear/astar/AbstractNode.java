package sf.alphaBear.astar;

/**
 * Created by 01368138 on 2017/11/30.
 */
public abstract class AbstractNode {

    protected static final int BASIC_MOVEMENT_COST = 10;

    protected static final int DIAGONAL_MOVEMENT_COST = 14;

    private int xPosition;
    private int yPosition;
    private boolean walkable;

    private AbstractNode previous;

    private boolean diagonally;

    private int movementPenalty;

    private int gCosts;

    private int hCosts;

    public AbstractNode(int xPosition, int yPosition) {
        this.yPosition = yPosition;
        this.xPosition = xPosition;
        this.walkable = true;
        this.movementPenalty = 0;
    }

    public boolean isDiagonally() {
        return diagonally;
    }

    public void setIsDiagonally(boolean isDiagonally) {
        this.diagonally = isDiagonally;
    }

    public void setCoordinates(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public AbstractNode getPrevious() {
        return previous;
    }

    public void setPrevious(AbstractNode previous) {
        this.previous = previous;
    }

    public void setMovementPenalty(int movementPenalty) {
        this.movementPenalty = movementPenalty;
    }

    public int getFCosts() {
        return gCosts + hCosts;
    }

    public int getGCosts() {
        return gCosts;
    }

    private void setGCosts(int gCosts) {
        this.gCosts = gCosts + movementPenalty;
    }

    public void setGCosts(AbstractNode previousAbstractNode, int basicCost) {
        setGCosts(previousAbstractNode.getGCosts() + basicCost);
    }

    public void setGCosts(AbstractNode previousAbstractNode) {
        if(diagonally) {
            setGCosts(previousAbstractNode, DIAGONAL_MOVEMENT_COST);
        } else {
            setGCosts(previousAbstractNode, BASIC_MOVEMENT_COST);
        }
    }

    public int calculateGCosts(AbstractNode previousAbstractNode) {
        if(diagonally) {
            return (previousAbstractNode.getGCosts() + DIAGONAL_MOVEMENT_COST + movementPenalty);
        } else {
            return (previousAbstractNode.getGCosts() + BASIC_MOVEMENT_COST + movementPenalty);
        }
    }

    public int calculateGCosts(AbstractNode previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getGCosts() + movementCost + movementPenalty);
    }

    public int getHCosts() {
        return hCosts;
    }

    protected  void setHCosts(int hCosts) {
        this.hCosts = hCosts;
    }

    public abstract void setHCosts(AbstractNode endAbstractNode);

    private int getMovementPenalty() {
        return movementPenalty;
    }

    public String toString() {
        return "(" + getXPosition() + ", " + getYPosition() + "): h: "
                + getHCosts() + " g: " + getGCosts() + " f: " + getFCosts();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractNode other = (AbstractNode) obj;
        if (this.xPosition != other.xPosition) {
            return false;
        }
        if (this.yPosition != other.yPosition) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.xPosition;
        hash = 17 * hash + this.yPosition;
        return hash;
    }

}
