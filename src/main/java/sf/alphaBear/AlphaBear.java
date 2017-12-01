package sf.alphaBear;

import java.util.ArrayList;
import java.util.List;

import sf.alphaBear.agent.SimpleBear;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;
import sf.alphaBear.httpio.MoveReqResult;

public class AlphaBear {
	EnvReqResult env ;
	int maxStep = 288; 
	List<MoveReqResult> hisStates;
	MoveReqResult lastState = null;
	
	int totalUseTime = 0;
	int totalReward = 0; 
	
	public int gogogo(EnvReqResult env, int maxStep) {
		this.env = env;
		this.hisStates = new ArrayList<>();
		String envId = this.env.getId();
		
		
		
		long st = System.currentTimeMillis();
		long et = System.currentTimeMillis();
		for(int i=0; i<maxStep; i++) {
			st = System.currentTimeMillis();
			
			MoveDirection decision = makeDecision();
			MoveReqResult moveRlt = HttpIO.step(envId, decision);
			
			hisStates.add(moveRlt);
			lastState = moveRlt;
			
			totalReward += moveRlt.getReward();
			
			et = System.currentTimeMillis();
			long diff = et - st;
			totalUseTime += diff;
			
			System.out.println("step " + i + ", time=" + diff + ", reward=" + moveRlt.getReward());
		}
		
		System.out.println("total time = " + totalUseTime + ", total reward = " + totalReward);
		return totalReward;
	}
	
	
	MoveDirection makeDecision() {
		return SimpleBear.myDecision();
	}
}
