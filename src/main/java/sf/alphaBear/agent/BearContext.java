package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.neo4j.cypher.internal.compiler.v2_3.ast.rewriters.projectFreshSortExpressions;

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
	
	ConcurrentHashMap<String, JobDetail> allJobDetails;
	ConcurrentHashMap<String, JobDetail> avaiableJobDetails;
	
	public BearContext(EnvReqResult env, int maxStep) {
		this.env = env;
		this.maxStep = maxStep;
		this.hisMoveRlt = new ArrayList<>();
		this.hisState = new ArrayList<>();
		this.allJobDetails = new ConcurrentHashMap<>();
		this.avaiableJobDetails = new ConcurrentHashMap<String, JobDetail>();
		// 最初状态
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
		
		List<Job> jobUpdates = moveReqResult.getState().getJobs();
		if (jobUpdates!=null && jobUpdates.size()>0) {
			// 更新状态， 或者需要生成新的job detail
			Map<String, JobDetail> newMap = jobUpdates.stream().map(up->{
				String key = JobDetail.avaibleKey(up.getX(), up.getY());
				JobDetail detail = avaiableJobDetails.get(key);
				if (detail==null) {
					detail = new JobDetail(up.getX(), up.getY(), up.getValue(), step);
				}
				detail.update(up.getValue());
				
				return detail;
			}).collect(Collectors.toConcurrentMap(JobDetail::getAvaibleKey, p->p));
			//job 生命终结
			List<JobDetail> endJobs = avaiableJobDetails.entrySet().stream().map(e->{
				if(!newMap.containsKey(e.getValue().getAvaibleKey())) {
					e.getValue().endLife(step);
				}
				return e.getValue();
			}).filter(p->p.getLifeEndStep()>0).collect(Collectors.toList());
			endJobs.stream().map(p->{
				allJobDetails.put(p.getId(), p);
				return 1;
			}).count();
			//更换
			avaiableJobDetails.clear();
			avaiableJobDetails.putAll(newMap);
		}
	}
	
	public AI getAI() {
		return lastState.getState().getAi();
	}
	public List<Wall> getWalls(){
		return env.getState().getWalls();
	}
	public List<Job> getJobs(){
		return lastState.getState().getJobs();
	}
	public Map<String, JobDetail> getAvaibleJobDetails(){
		return avaiableJobDetails;
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