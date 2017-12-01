package sf.alphaBear.agent;

import java.util.Random;

import sf.alphaBear.Config;
import sf.alphaBear.MoveDecision;
import sf.alphaBear.Point;
import sf.alphaBear.agent.algo.SchedulePath;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;

public abstract class BearTemplate {
	EnvReqResult env;
	BearContext context ; 
	int randomWalks = 0;
	Random rs = new Random();
	
	public abstract MoveDecision myDecision() ;
	
	public BearTemplate(BearContext context) {
		this.context = context;
		this.env = context.getEnv();
	}

	public void incRandomWalks() {
		randomWalks ++;
	}
	public EnvReqResult getEnv() {
		return env;
	}

	public void setEnv(EnvReqResult env) {
		this.env = env;
	}

	public int getRandomWalks() {
		return randomWalks;
	}

	public BearContext getContext() {
		return context;
	}

	public void setContext(BearContext context) {
		this.context = context;
	}
	
	protected MoveDecision calDirection(AI ai, Point target) {
		if (ai.getX()==target.getX()) {
			if (target.getY()>ai.getX()) {
				return MoveDecision.R;
			}else {
				return MoveDecision.L;
			}
		}
		
		if (ai.getY()==target.getY()) {
			if (target.getX()>ai.getX()) {
				return MoveDecision.D;
			}else {
				return MoveDecision.U;
			}
		}
		
		System.out.println("should not come here");
		throw new RuntimeException("should not come here");
	}
	protected MoveDecision randomWalk(AI ai) {
		incRandomWalks();
		
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
	public static class JobProfit {
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
}