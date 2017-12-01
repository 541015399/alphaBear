package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
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
	List<Point> walkHis ;
	
	public GreedyAstarBear(BearContext context) {
		super(context);
		
		walkHis = new ArrayList<Point>();
		db = new GridStateDb(Config.MAX_X, Config.MAX_Y, context.getWalls());
		astarAlgo = new AstarAlgoByNeo4j(db);
	}
	
	private SchedulePath reSchedule(List<Job> jobs, AI ai) {
		
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
		
		SchedulePath path = jobProfits.get(0).getPath();
		Point target = path.get(path.maxSteps());
		System.out.println("reschedule - x=" + target.getX() + ",y=" + target.getY());
		return jobProfits.get(0).getPath();
	}

	
	public MoveDecision myDecision() {
		AI ai = context.getAI();
		walkHis.add(new Point(ai.getX(), ai.getY()));
		List<Job> jobs = context.getJobs();
		
		if (schedulePath==null || schedulePath.maxSteps()<curStep) {
			if (jobs!=null && jobs.size()>0) {
				schedulePath = reSchedule(jobs, ai);
				curStep = 0;
			}
		}
		if (schedulePath==null || schedulePath.maxSteps()<0) {
			return randomWalk(ai);
		}else {
			Point target = schedulePath.get(curStep ++);
			return calDirection(ai, target);
		}
		
	}

}