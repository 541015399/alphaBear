package sf.alphaBear.httpio;

import sf.alphaBear.httpio.pojo.*;

public class MoveReqResult {
	String msg;
	String id;
	
	MapState state;
	int reward;
	boolean done;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MapState getState() {
		return state;
	}
	public void setState(MapState state) {
		this.state = state;
	}
	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
	
}
