package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lupinyin
 * TODO : 按照历史预计n步后的值
 */
public class JobDetail {
	final int x;
	final int y;
	final int initReward;
	final int lifeStartStep;
	final List<Integer> chgHis;
	
	int lifeEndStep;
	int curReward;
	
	/*
	 * 当前尚未开发完成， 所以不推荐使用
	 */
	@Deprecated
	public int predictReward(int nSteps) {
		return curReward;
	}
	
	public JobDetail(int x, int y, int initReward, int step) {
		this.x = x;
		this.y = y;
		this.initReward = initReward;
		this.lifeStartStep = step;
		this.chgHis = new ArrayList<Integer>();
	}
	public void update(int reward) {
		this.curReward = reward;
		this.chgHis.add(reward);
	}
	public void endLife(int step) {
		this.lifeEndStep = step;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getInitReward() {
		return initReward;
	}
	public int getLifeStartStep() {
		return lifeStartStep;
	}
	public int getLifeEndStep() {
		return lifeEndStep;
	}
	public void setLifeEndStep(int lifeEndStep) {
		this.lifeEndStep = lifeEndStep;
	}
	public int getCurReward() {
		return curReward;
	}
	public void setCurReward(int curReward) {
		this.curReward = curReward;
	}
	public List<Integer> getChgHis() {
		return chgHis;
	}
}
