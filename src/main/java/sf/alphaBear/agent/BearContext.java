package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.List;

import sf.alphaBear.MoveDecision;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;
import sf.alphaBear.httpio.MoveReqResult;
import sf.alphaBear.httpio.pojo.AI;
import sf.alphaBear.httpio.pojo.Job;
import sf.alphaBear.httpio.pojo.Wall;
import sf.alphaBear.httpio.pojo.MapState;

public class BearContext {
	// context
	EnvReqResult env ;
	
	int maxStep = 288;
	int curStep = 0;
	
	List<MoveReqResult> hisMoveRlt;
	List<MapState> hisState;
	MoveReqResult lastState = null;
	
	long totalUseTime = 0;
	int totalReward = 0; 
	
	public BearContext(EnvReqResult env, int maxStep) {
		this.env = env;
		this.maxStep = maxStep;
		this.hisMoveRlt = new ArrayList<MoveReqResult>();
		
		this.hisState.add(env.getState());
	}
	public MoveReqResult doStepReq(MoveDecision decision) {
		return HttpIO.step(env.getId(), decision);
	}
	public void appendState(Integer step, MoveReqResult moveReqResult, long useTime, int reward) {
		this.curStep = step;
		this.hisMoveRlt.add(moveReqResult);
		this.hisState.add(moveReqResult.getState());
		this.lastState = moveReqResult;
		this.totalUseTime += useTime;
		this.totalReward += reward;
		
	}
	
	public AI getAI() {
		return lastState==null? null: lastState.getState().getAi();
	}
	public List<Wall> getWalls(){
		return env.getState().getWalls();
	}
	public List<Job> getJobs(){
		return lastState.getState().getJobs();
	}

	public EnvReqResult getEnv() {
		return env;
	}

	public void setEnv(EnvReqResult env) {
		this.env = env;
	}

	public int getMaxStep() {
		return maxStep;
	}

	public void setMaxStep(int maxStep) {
		this.maxStep = maxStep;
	}

	public int getCurStep() {
		return curStep;
	}

	public void setCurStep(int curStep) {
		this.curStep = curStep;
	}

	public List<MoveReqResult> getHisStates() {
		return hisMoveRlt;
	}

	public void setHisStates(List<MoveReqResult> hisStates) {
		this.hisMoveRlt = hisStates;
	}

	public MoveReqResult getLastState() {
		return lastState;
	}

	public void setLastState(MoveReqResult lastState) {
		this.lastState = lastState;
	}

	public long getTotalUseTime() {
		return totalUseTime;
	}

	public void setTotalUseTime(int totalUseTime) {
		this.totalUseTime = totalUseTime;
	}

	public int getTotalReward() {
		return totalReward;
	}

	public void setTotalReward(int totalReward) {
		this.totalReward = totalReward;
	}
	
	
}