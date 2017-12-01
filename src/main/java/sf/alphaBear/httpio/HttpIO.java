package sf.alphaBear.httpio;


import com.google.gson.Gson;

import sf.alphaBear.util.HttpUtil;

public class HttpIO {
	static String host = "http://10.2.5.64/";
	String envId = "";
	String url = "";
	
	public static EnvReqResult createEnv(Env env) {
		String url = host + env.label + "/";
		
		Gson gson = new Gson();
		String rlt = HttpUtil.executePost(url, "{'name': 'alphaBear'}");
		
		EnvReqResult rltObj = gson.fromJson(rlt, EnvReqResult.class);
		return rltObj;
	}
	
	public static String commit() {
		return "";
	}
	public static String step(String url) {
		return HttpUtil.executeGet(url);
	}
	
	static enum Env {
		TEST("test"), REAL("competition");
		String label;
		private Env(String label) {
			this.label = label;
		}
	}
	
	public static void main(String[] args) {
		String url = "http://10.2.5.64/" + Env.TEST.label;
		
		Gson gson = new Gson();
		String rlt = HttpUtil.executePost(url, "{'name': 'alphaBear'}");
		
		EnvReqResult rltObj = gson.fromJson(rlt, EnvReqResult.class);
		System.out.println(rltObj.msg);
	}
	
}
