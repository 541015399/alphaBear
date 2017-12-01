package sf.alphaBear.agent;

import sf.alphaBear.MoveDirection;

public class SimpleBear {
	static int maxX = 12;
	static int curX = 0;
	static boolean moveR = true;
	
	public static MoveDirection myDecision() {
		MoveDirection dir = null;
		if (moveR) {
			curX ++;
			dir = MoveDirection.R;
		}else {
			curX --;
			dir = MoveDirection.L;
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