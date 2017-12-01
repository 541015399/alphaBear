package sf.alphaBear;

import sf.alphaBear.httpio.EnvReqResult;
import sf.alphaBear.httpio.HttpIO;

/**
 * Hello world!
 *
 */
public class AlphaBearMain {
    public static void main( String[] args ){
	    EnvReqResult env = HttpIO.createEnv();
	    AlphaBearController smartBear = new AlphaBearController();
	    int maxStep = 288; 
	    
	    int totalReward = smartBear.gogogo(env, maxStep);
	    	
	    	System.out.println( "congratulations, reward = " + totalReward);
    }
}
