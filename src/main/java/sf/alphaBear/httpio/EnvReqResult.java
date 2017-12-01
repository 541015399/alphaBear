package sf.alphaBear.httpio;

import sf.alphaBear.httpio.pojo.*;

public class EnvReqResult {
	String msg;
	String id;
	
	State state;

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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	
}
