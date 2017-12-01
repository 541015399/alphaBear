package sf.alphaBear.agent;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.neo4j.graphdb.Path;

import scala.reflect.internal.Symbols.ClassSymbol;
import scala.reflect.internal.Trees.New;
import sf.alphaBear.Config;
import sf.alphaBear.MoveDecision;
import sf.alphaBear.Point;
import sf.alphaBear.agent.algo.AstarAlgoByNeo4j;
import sf.alphaBear.agent.algo.SchedulePath;
import sf.alphaBear.agent.db.GridStateDb;
import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;

public class GreedyAstarBearV1704 extends BearTemplate {
	GridStateDb db;
	AstarAlgoByNeo4j astarAlgo;
	
	int curStep = 0;
	SchedulePath schedulePath ;
	
	public GreedyAstarBearV1704(BearContext context) {
		super(context);
		
		db = new GridStateDb(Config.MAX_X, Config.MAX_Y, context.getWalls());
		astarAlgo = new AstarAlgoByNeo4j(db);
	}

	public MoveDecision myDecision() {
		AI ai = context.getAI();
		List<Job> jobs = context.getJobs();
		
		Point target = schedulePath==null? null: schedulePath.get(curStep++);
		
		//没有路线
		if (target==null) {
			if (jobs!=null && jobs.size()>0) {
				List<JobProfit> jobProfits = jobs.stream().map(j->{
					SchedulePath schedulePath = astarAlgo.findPath(ai.getX(), ai.getY(), j.getX(), j.getY());
					int steps = schedulePath.maxSteps() + 1;
					int profit = j.getValue() - steps;
					return new JobProfit(j, profit, 0f, schedulePath);
				}).collect(Collectors.toList());
				
				jobProfits.sort(new Comparator<JobProfit>() {
					@Override
					public int compare(JobProfit o1, JobProfit o2) {
						return o2.getProfit() - o1.getProfit();
					}
				});
				
				schedulePath = jobProfits.get(0).getPath();
				
				target = schedulePath==null? null: schedulePath.get(curStep++);
			}
		}
		if (target==null) return randomWalk(ai);
		else return calDirection(ai, target);
	}
	
}