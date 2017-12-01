package sf.alphaBear.agent;

import sf.alphaBear.MoveDecision;
import sf.alphaBear.astar.Map;
import sf.alphaBear.astar.Node;
import sf.alphaBear.astar.NodeFactory;
import sf.alphaBear.httpio.MoveReqResult;
import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;
import sf.alphaBear.httpio.pojo.MapState;
import sf.alphaBear.httpio.pojo.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 01368138 on 2017/12/1.
 */
public class AStarBear extends BearTemplate {

    private static final int MAP_SIZE = 12;

    private boolean anotherRound = true;

    public AStarBear(BearContext context) {
        super(context);
        decisions = getDecisions();
    }

    public List<MoveDecision> getDecisions() {

        MapState map = getContext().getLastMapState();

        AI bear = map.getAi();
        List<Job> jobs = map.getJobs();
        List<Wall> walls = map.getWalls();

        Map<Node> aMap = new Map<Node>(MAP_SIZE, MAP_SIZE, new NodeFactory());

        aMap.setWalls(walls);

        int startX = bear.getX();
        int startY = bear.getY();

        int maxReward = -1000;

        int targetX = -1;
        int targetY = -1;

        for (int i = 0; i < jobs.size(); i++) {

            Job job = jobs.get(i);

            int tempX = job.getX();
            int tempY = job.getY();

            int shortestPath = aMap.findPath(startX, startY, tempX, tempY).size();

            int reward = job.getValue() - shortestPath;

            if (reward > maxReward) {
                maxReward = reward;
                targetX = tempX;
                targetY = tempY;
            }
        }

        System.out.println("maxReward: " + maxReward);

        List<Node> path = aMap.findPath(startX, startY, targetX, targetY);

        for (int i = 0; i < path.size(); i++) {
            if(i < path.size() - 1) {
                System.out.print("(" + path.get(i).getXPosition() + ", " + path.get(i).getYPosition() + ") -> ");
            } else {
                System.out.print("(" + path.get(i).getXPosition() + ", " + path.get(i).getYPosition() + ").");
            }
        }


        System.out.println("StartX: " + startX);
        System.out.println("StartY: " + startY);
        System.out.println("targetX: " + targetX);
        System.out.println("targetY: " + targetY);

        List<MoveDecision> moves = new ArrayList<>();


        //当前节点和下一个节点
        int curX = startX;
        int curY = startY;
        for (int i = 0; i < path.size(); i++) {

            Node node = path.get(i);

            int nextX = node.getXPosition();
            int nextY = node.getYPosition();

            MoveDecision decision = MoveDecision.R;

            if (nextX - curX == 1) {
                decision = MoveDecision.D;
            }

            if (nextX - curX == -1) {
                decision = MoveDecision.U;
            }

            if (nextY - curY == 1) {
                decision = MoveDecision.R;
            }

            if (nextY - curY == -1) {
                decision = MoveDecision.L;
            }
            moves.add(decision);
            curX = nextX;
            curY = nextY;
        }
        return moves;
    }

    private int curDecision = 0;

    private List<MoveDecision> decisions;

    @Override
    public MoveDecision myDecision() {

        //MoveReqResult moveRlt = context.doStepReq(decision)

        if (curDecision == decisions.size()) {
            curDecision = 0;
            decisions = getDecisions();

            return decisions.get(curDecision++);
        } else {
            return decisions.get(curDecision++);
        }

//        if (anotherRound) {
//            anotherRound = false;
//            decisions = getDecisions();
//            return decisions.get(curDecision++);
//        } else {
//            if (curDecision == decisions.size()) {
//                curDecision = 0;
//                return getDecisions().get(curDecision++);
//            }
//            return decisions.get(curDecision++);
//        }
    }
}
