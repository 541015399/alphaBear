package sf.alphaBear;


import sf.alphaBear.agent.AStarBear;

import sf.alphaBear.agent.BearContext;
import sf.alphaBear.agent.BearTemplate;
import sf.alphaBear.agent.GreedyAstarBear;
import sf.alphaBear.agent.SimpleBear;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.MoveReqResult;

public class AlphaBearController {
	EnvReqResult env ;
	BearContext context ;
	BearTemplate bearAgent ;

	public int gogogo(EnvReqResult env, int maxStep) {
		this.env = env;
		this.context = new BearContext(env, maxStep);
		this.bearAgent = new GreedyAstarBear(context);

		long st = System.currentTimeMillis();
		long et = System.currentTimeMillis();
		for(int i=0; i<maxStep; i++) {
			st = System.currentTimeMillis();

			MoveDecision decision = bearAgent.myDecision();
			MoveReqResult moveRlt = context.doStepReq(decision);

			et = System.currentTimeMillis();
			long ut = et - st;

			context.appendState(i, moveRlt, ut, moveRlt.getReward());

			System.out.println("step " + i + ", time=" + ut + ", reward=" + moveRlt.getReward()  + ",totalReard=" + context.getTotalReward() + ",random walks=" + bearAgent.getRandomWalks());
		}

		System.out.println("total time = " + context.getTotalUseTime() + ", total reward = " + context.getTotalReward());
		return context.getTotalReward();
	}

}
