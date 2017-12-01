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
	    AlphaBear smartBear = new AlphaBear();
	    
	    int totalReward = smartBear.gogogo(env, 288);
	    	
	    	System.out.println( "congratulations, reward = " + totalReward);
    }
}
