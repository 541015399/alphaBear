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
    
    	
    	System.out.println( "Hello World!" );
    }
}
