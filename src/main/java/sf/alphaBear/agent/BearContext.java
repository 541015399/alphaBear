package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.List;

import sf.alphaBear.MoveDecision;
import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;
import sf.alphaBear.httpio.MoveReqResult;

public class BearContext {
	EnvReqResult env ;
	
	int maxStep = 288;
	int curStep = 0;
	
	List<MoveReqResult> hisStates;
	MoveReqResult lastState = null;
	
	long totalUseTime = 0;
	int totalReward = 0; 
	
	public BearContext(EnvReqResult env, int maxStep) {
		this.env = env;
		this.maxStep = maxStep;
		this.hisStates = new ArrayList<MoveReqResult>();
	}
	public MoveReqResult doStepReq(MoveDecision decision) {
		return HttpIO.step(env.getId(), decision);
	}
	public void appendState(Integer step, MoveReqResult state, long useTime, int reward) {
		this.curStep = step;
		this.hisStates.add(state);
		this.lastState = state;
		this.totalUseTime += useTime;
		this.totalReward += reward;
		
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
		return hisStates;
	}

	public void setHisStates(List<MoveReqResult> hisStates) {
		this.hisStates = hisStates;
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