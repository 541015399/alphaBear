package sf.alphaBear;

import java.util.ArrayList;
import java.util.List;


import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;
import sf.alphaBear.httpio.MoveReqResult;

public class AlphaBear {
	EnvReqResult env ;
	int maxStep = 288; 
	List<MoveReqResult> hisStates;
	MoveReqResult lastState = null;
	
	public int gogogo(EnvReqResult env, int maxStep) {
		this.env = env;
		this.hisStates = new ArrayList<>();
		String envId = this.env.getId();
		
		int totalUseTime = 0;
		int totalReward = 0; 
		
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
	
	static class SimpleBear {
		static int maxX = 12;
		static int curX = 0;
		static boolean moveR = true;
		
		static MoveDirection myDecision() {
			MoveDirection dir = null;
			if (moveR) {
				curX ++;
				dir = MoveDirection.R;
			}else {
				curX --;
				dir = MoveDirection.L;
			}
			if (curX>=maxX) {
				moveR = false;
			}
			if (curX<=0) {
				moveR = true;
			}
			return dir;
		}
	}
}
