package sf.alphaBear.httpio;


import com.google.gson.Gson;

import sf.alphaBear.util.HttpUtil;

public class HttpIO {
	String envId = "";
	String url = "";
	
	public static String createEnv(Env env) {
		String url = "/" + env.label + "/";
		
		Gson gson = new Gson();
		/*String json = gson.toJson(new Param() {
			String name = "alphaBear";
		});*/
		
		HttpUtil.executePost(url, "");
		return "";
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
		String url = "/" + Env.TEST.label + "/";
		
		/*String json = JSON.toJSONString(new Param() {
			String name = "alphaBear";
		});*/
		
		System.out.println("");
		
		HttpUtil.executePost(url, "{'name': 'alphaBear'}");
	}
	
}
