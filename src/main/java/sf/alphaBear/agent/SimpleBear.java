package sf.alphaBear.agent;

import sf.alphaBear.MoveDecision;
import sf.alphaBear.httpio.EnvReqResult;

public class SimpleBear extends BearTemplate {
	public SimpleBear(BearContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	static int maxX = 12;
	static int curX = 0;
	static boolean moveR = true;
	
	public MoveDecision myDecision() {
		MoveDecision dir = null;
		if (moveR) {
			curX ++;
			dir = MoveDecision.R;
		}else {
			curX --;
			dir = MoveDecision.L;
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