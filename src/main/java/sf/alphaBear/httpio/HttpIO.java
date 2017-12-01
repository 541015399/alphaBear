package sf.alphaBear.httpio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import sf.alphaBear.util.HttpUtil;

public class HttpIO {
	String url = "";
	
	public static String commit() {
		return "";
	}
	public static String step(String url) {
		return HttpUtil.executeGet(url);
	}
	
	public static void main(String[] args) {
		String url = "https://www.baidu.com/home/msg/data/personalcontent?num=8&indextype=manht&_req_seqid=0xa5af389900012e51&asyn=1&t=1512061252382&sid=1452_21104_17001_25177_25146_22158";
		String rlt = step(url);
		
		Object obj = JSON.parse(rlt);
		if (obj instanceof JSONObject) {
			Integer errNo = ((JSONObject) obj).getInteger("errNo");
			System.out.println(errNo);
		}
				
		System.out.println(rlt);
	}
	
}
