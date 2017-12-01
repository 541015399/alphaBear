package sf.alphaBear.agent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.fi.FinnishAnalyzer;
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
	
	
	int curStep = 0;
	SchedulePath schedulePath ;
	
	public GreedyAstarBear(BearContext context) {
		super(context);
		
		db = new GridStateDb(Config.MAX_X, Config.MAX_Y, context.getWalls());
		astarAlgo = new AstarAlgoByNeo4j(db);
	}
	
	private void reTarget(List<Job> jobs, AI ai) {
		curStep = 0;
		schedulePath = null;
		
		List<JobProfit> jobProfits = jobs.stream().map(j->{
			SchedulePath schedulePath = astarAlgo.findPath(ai.getX(), ai.getY(), j.getX(), j.getY());
			int steps = schedulePath.maxSteps() + 1;
			JobDetail detail = context.getDetail(j);
			int cost = detail==null? steps : detail.predictReward(steps);
			int profit = j.getValue() - cost;
			return new JobProfit(j, profit, schedulePath);
		})
			.filter(o->o.getProfit()>0)
			.collect(Collectors.toList());
		
		jobProfits.sort(new Comparator<JobProfit>() {
			@Override
			public int compare(JobProfit o1, JobProfit o2) {
				return o2.getProfit() - o1.getProfit();
			}
		});
		
		schedulePath = jobProfits.get(0).getPath();
	}

	public MoveDecision myDecision() {
		AI ai = context.getAI();
		List<Job> jobs = context.getJobs();
		
		Point target = schedulePath==null? null: schedulePath.get(curStep++);
		
		//没有路线
		if (target==null) {
			if (jobs!=null && jobs.size()>0) {
				reTarget(jobs, ai);
				target = schedulePath==null? null: schedulePath.get(curStep++);
			}
		}
		//这个脏代码， 待debug
		if (target!=null) {
			int retry = 5;
			while((retry--)>0) {
				try {
					return calDirection(ai, target);
				} catch (Exception e) {
					reTarget(jobs, ai);
					e.printStackTrace();
				}
			}
		}
		//如果走到这里， 下一步需要重新选择路径
		schedulePath = null;
		return randomWalk(ai);
	}

}