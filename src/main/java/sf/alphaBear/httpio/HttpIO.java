package sf.alphaBear.httpio;


import com.google.gson.Gson;

import sf.alphaBear.Env;
import sf.alphaBear.MoveDirection;
import sf.alphaBear.util.HttpUtil;

public class HttpIO {
	static String HOST = "http://10.2.5.64/";
	static Env ENV = Env.TEST;
	static String envId = null;
	
	public static EnvReqResult createEnv() {
		String url =  HOST + ENV.getLabel();
		
		Gson gson = new Gson();
		String rlt = HttpUtil.executePost(url, "{'name': 'alphaBear'}");
		
		EnvReqResult rltObj = gson.fromJson(rlt, EnvReqResult.class);
		return rltObj;
	}
	public static MoveReqResult step(MoveDirection direction) {
		String url = HOST + ENV.getLabel() + "/" + envId + "/move";
		
		Gson gson = new Gson();
		String rlt = HttpUtil.executePost(url, "{'direction': '" + direction + "'}");
		
		MoveReqResult rltObj = gson.fromJson(rlt, MoveReqResult.class);
		return rltObj;
	}
	
	public static void test() {
		EnvReqResult rltObj = createEnv();
		System.out.println(rltObj.msg);
		
		envId = rltObj.getId();
		
		MoveReqResult moveReqResult = step(MoveDirection.U);
		
		System.out.println(moveReqResult.getReward());
	}
	
	public static void test1() {
		String url =  HOST + ENV.getLabel();
		
		Gson gson = new Gson();
		String rlt = HttpUtil.executePost(url, "{'name': 'alphaBear'}");
		
		EnvReqResult rltObj = gson.fromJson(rlt, EnvReqResult.class);
		
		System.out.println(rltObj.msg);
	}
	
	public static void main(String[] args) {
		test();
	}
	
}
