package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.neo4j.kernel.configuration.ssl.SslSystemSettings;

import sf.alphaBear.Config;
import sf.alphaBear.MoveDecision;
import sf.alphaBear.Point;
import sf.alphaBear.agent.algo.AstarAlgoByNeo4j;
import sf.alphaBear.agent.algo.SchedulePath;
import sf.alphaBear.agent.db.GridStateDb;
import sf.alphaBear.agent.db.LookUpTable;
import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;

public class GreedyAstarBear extends BearTemplate {
	GridStateDb db;
	AstarAlgoByNeo4j astarAlgo;

	JobProfit curJobProfit ;
	int curStep = 0;
	SchedulePath schedulePath ;
	List<Point> walkHis ;
	
	LookUpTable pathLookUpTbl ;
	
	public GreedyAstarBear(BearContext context) {
		super(context);
		
		walkHis = new ArrayList<Point>();
		db = new GridStateDb(Config.MAX_X, Config.MAX_Y, context.getWalls());
		astarAlgo = new AstarAlgoByNeo4j(db);
		long st = System.currentTimeMillis();
		pathLookUpTbl = new LookUpTable(db);
		long ut = System.currentTimeMillis() - st;
		System.out.println("look up table use time - " + ut);
	}
	
	private JobProfit reSchedule(List<Job> jobs, AI ai) {
		
		List<JobProfit> jobProfits = jobs.stream().map(j->{
			// SchedulePath schedulePath = astarAlgo.findPath(ai.getX(), ai.getY(), j.getX(), j.getY());
			
			SchedulePath schedulePath = pathLookUpTbl.findPath(ai.getX(), ai.getY(), j.getX(), j.getY());
			
			int steps = schedulePath.maxSteps() + 1;
			JobDetail detail = context.getDetail(j);
			int cost = detail==null? steps : detail.predictReward(steps);
			int profit = j.getValue() - cost;
			float avgProfit = profit/(float)steps;
			return new JobProfit(j, profit, avgProfit, schedulePath);
		})
			.filter(o->o.getProfit()>0)
			.collect(Collectors.toList());
		
		List<JobProfit> newList = new ArrayList<>();
		int range = 1;
		while(newList.size()==0) {
			final int fRange = range;
			newList = jobProfits.stream().filter(o->o.getPath().maxSteps()<=fRange).collect(Collectors.toList());
			range ++;
		}
		jobProfits = newList;
		
		jobProfits.sort(new Comparator<JobProfit>() {
			@Override
			public int compare(JobProfit o1, JobProfit o2) {
				// return o2.getProfit() - o1.getProfit();
				Float f2 = o2.getAvgProfit();
				Float f1 = o1.getAvgProfit();
				return f2.compareTo(f1);
			}
		});
		
		SchedulePath path = jobProfits.get(0).getPath();
		Point target = path.get(path.maxSteps());
		System.out.println("reschedule - x=" + target.getX() + ",y=" + target.getY());
		return jobProfits.get(0);
	}

	public MoveDecision myDecision() {
		AI ai = context.getAI();
		walkHis.add(new Point(ai.getX(), ai.getY()));
		List<Job> jobs = context.getJobs();
		//初始化， 或者走到头了
		if (curJobProfit==null || schedulePath==null || schedulePath.maxSteps()<curStep) {
			curJobProfit = null;
			schedulePath = null;
			curStep = 0;
			if (jobs!=null && jobs.size()>0) {
				curJobProfit = reSchedule(jobs, ai);
				schedulePath = curJobProfit.getPath();
				
				Point target = schedulePath.get(curStep ++);
				return calDirection(ai, target);
			}else {
				return randomWalk(ai);
			}
		}else {
			// 正常走，不管如何，对比一下
			JobProfit tryJob = reSchedule(jobs, ai);
			//if (tryJob.getProfit() > curJobProfit.getProfit()) {
			if (tryJob.getAvgProfit() > curJobProfit.getAvgProfit()) {
				System.out.println("reschedule gain -" + (tryJob.getProfit() - curJobProfit.getProfit()));
				curJobProfit = tryJob;
				schedulePath = curJobProfit.getPath();
				curStep = 0;
				
				Point target = schedulePath.get(curStep ++);
				return calDirection(ai, target);
			}else {
				Point target = schedulePath.get(curStep ++);
				return calDirection(ai, target);
			}
		}
		
		
	}

}