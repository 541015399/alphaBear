package sf.alphaBear.agent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lupinyin
 * TODO : 按照历史预计n步后的值
 */
public class JobDetail implements Comparable<JobDetail> {
	final int x;
	final int y;
	final int initReward;
	final int lifeStartStep;
	final List<Integer> chgHis;
	
	final String avaibleKey;
	final String id ;
	
	int lifeEndStep;
	int curReward;
	
	/*
	 * 当前尚未开发完成， 所以不推荐使用
	 */
	public int predictReward(int nSteps) {
		int rate = 1;//默认为1
		if (chgHis!=null) {//有历史则估算一下
			for(int i: chgHis) rate += i;
			rate = rate / chgHis.size();
		}
		int cost = nSteps * rate;
		return curReward - cost;
	}
	@Override
	public String toString() {
		return id;
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	@Override
	public int compareTo(JobDetail o) {
		return id.compareTo(o.id);
	}
	public final static String avaibleKey(int x, int y) {
		return "x=" + x + ",y=" + y;
	}
	public JobDetail(int x, int y, int initReward, int step) {
		this.x = x;
		this.y = y;
		this.initReward = initReward;
		this.lifeStartStep = step;
		this.chgHis = new ArrayList<Integer>();
		
		this.avaibleKey = avaibleKey(x, y);
		this.id = "x=" + x + ",y=" + y + ",step=" + step + "val=" + initReward;
	}
	public void update(int reward) {
		this.curReward = reward;
		this.chgHis.add(reward);
	}
	public void endLife(int step) {
		this.lifeEndStep = step;
	}
	
	public String getAvaibleKey() {
		return avaibleKey;
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

	public String getId() {
		return id;
	}

	
}
