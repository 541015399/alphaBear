package sf.alphaBear;

import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.MoveReqResult;

public class AlphaBear {
	public MoveDirection go(EnvReqResult env, MoveReqResult lastMoveRlt) {
		return MoveDirection.U;
	}
}
