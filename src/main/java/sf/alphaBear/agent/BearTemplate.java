package sf.alphaBear.agent;

import sf.alphaBear.MoveDecision;
import sf.alphaBear.httpio.EnvReqResult;

public abstract class BearTemplate {
	EnvReqResult env;
	BearContext context ; 
	
	public abstract MoveDecision myDecision() ;
	
	public BearTemplate(BearContext context) {
		this.context = context;
		this.env = context.getEnv();
	}

	public EnvReqResult getEnv() {
		return env;
	}

	public void setEnv(EnvReqResult env) {
		this.env = env;
	}

	public BearContext getContext() {
		return context;
	}

	public void setContext(BearContext context) {
		this.context = context;
	}
	
	
}