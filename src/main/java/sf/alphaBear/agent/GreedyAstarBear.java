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

public class GreedyAstarBear extends BearTemplate {
	GridStateDb db;
	AstarAlgoByNeo4j astarAlgo;
	Random rs = new Random();
	
	int curStep = 0;
	SchedulePath schedulePath ;
	
	public GreedyAstarBear(BearContext context) {
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
					return new JobProfit(j, profit, schedulePath);
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
	private static class JobProfit {
		SchedulePath path;
		Job job;
		int profit;
		public JobProfit(Job job, int profit, SchedulePath path) {
			this.job = job;
			this.profit = profit;
			this.path = path;
		}
		public Job getJob() {
			return job;
		}
		public int getProfit() {
			return profit;
		}
		public SchedulePath getPath() {
			return path;
		}
		
	}
	private MoveDecision calDirection(AI ai, Point target) {
		if(ai.getX()==target.getX()) {
			if (ai.getY()<target.getY()) {
				return MoveDecision.U;
			}else {
				return MoveDecision.D;
			}
		}
		if (ai.getY()==target.getY()) {
			if (ai.getX()<target.getX()) {
				return MoveDecision.R;
			}else {
				return MoveDecision.L;
			}
		}else {
			
		}
		
		if (ai.getX()<target.getX()) {
			return MoveDecision.R;
		}else {
			return MoveDecision.L;
		}
	}
	private MoveDecision randomWalk(AI ai) {
		if (ai.getX()==Config.MAX_X) return MoveDecision.L;
		if (ai.getX()==0) return MoveDecision.R;
		
		if (ai.getY()==0) return MoveDecision.U;
		if (ai.getY()==Config.MAX_Y) return MoveDecision.D;
		
		int r = Math.abs(rs.nextInt()) % 4;
		switch (r) {
		case 0: return MoveDecision.R;
		case 1: return MoveDecision.L;
		case 2: return MoveDecision.U;
		default: return MoveDecision.D;
		}
	}
}