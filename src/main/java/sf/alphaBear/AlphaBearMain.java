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
	    int maxStep = 400;

		System.out.println(env.getId());
	    int totalReward = smartBear.gogogo(env, maxStep);
	    	
	    	System.out.println( "congratulations, reward = " + totalReward);
	    	System.out.println( "id = " + env.getId());
    }
}
