package sf.alphaBear;


import sf.alphaBear.agent.AStarBear;

import sf.alphaBear.agent.BearContext;
import sf.alphaBear.agent.BearTemplate;
import sf.alphaBear.agent.GreedyAstarBear;
import sf.alphaBear.agent.GreedyAstarBearV1936;
import sf.alphaBear.agent.GreedyAstarBearV2032;
import sf.alphaBear.agent.GreedyAstarBearV4000;
import sf.alphaBear.agent.GreedyAstarBearV4300;
import sf.alphaBear.agent.SimpleBear;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;
import sf.alphaBear.httpio.MoveReqResult;

public class AlphaBearController {
	EnvReqResult env ;
	BearContext context ;
	BearTemplate bearAgent ;

	public int gogogo(EnvReqResult env, int maxStep) {
		this.env = env;
		this.context = new BearContext(env, maxStep);
		this.bearAgent = new GreedyAstarBearV4300(context);
		
		HttpIO.resetUseTime();

		long totalUseTime = 0;
		
		long st = System.currentTimeMillis();
		long et = System.currentTimeMillis();
		for(int i=0; i<maxStep; i++) {
			st = System.currentTimeMillis();

			MoveDecision decision = bearAgent.myDecision();
			MoveReqResult moveRlt = context.doStepReq(decision);

			et = System.currentTimeMillis();
			long ut = et - st;

			context.appendState(i, moveRlt, ut, moveRlt.getReward());

			totalUseTime += totalUseTime;
			System.out.println("step " + i + ", totalTime=" + ut + ", reward=" + moveRlt.getReward()  + ",totalReard=" + context.getTotalReward() + ",random walks=" + bearAgent.getRandomWalks());
		}

		long calTime = totalUseTime - HttpIO.useTime;
		System.out.println("total time = " + totalUseTime + ", net work time=" + HttpIO.useTime + ", cal time=" + calTime + ", total reward = " + context.getTotalReward());
		return context.getTotalReward();
	}

}
