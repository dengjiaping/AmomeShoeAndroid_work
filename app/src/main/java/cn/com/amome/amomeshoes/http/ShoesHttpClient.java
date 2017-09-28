package cn.com.amome.amomeshoes.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.loopj.android.http.AsyncHttpClient;

public class ShoesHttpClient {
	public static String TAG = "ShoesHttpClient";

	public static AsyncHttpClient client = new AsyncHttpClient();

	static {
		client.setTimeout(ClientConstant.TIMEOUT);
	}

	/**
	 * 将map中的参数整理成完整get方法url
	 * @param type
	 * @param paramsMap
	 * @return 完整的url
	 */
	public static String getUrl(String url, HashMap<String, String> paramsMap) {
//		String paramStr = getParamString(ClientUtil.getParamMap(paramsMap));
		String paramStr = getParamString(paramsMap);
		return generateUrl(url, paramStr);
	}

	/**
	 * 将map中的参数整理成完整post方法url
	 * @param type
	 * @param paramsMap
	 * @return 完整的url
	 */
	public static String postUrl(String url, HashMap<String, String> paramsMap) {
//		String paramStr = getParamString(ClientUtil.getSysParamMap(paramsMap));
		String paramStr = getParamString(paramsMap);
		return generateUrl(url, paramStr);
	}

	/**
	 * 组合生成完整url
	 * @param type
	 * @param paramStr
	 * @return
	 */
	private static String generateUrl(String url, String paramStr) {
		return url+ paramStr;
	}

	/**
	 * 组合url中带的参数
	 * @param map
	 * @return ?xxx=xx&xxx=xx
	 */
	private static String getParamString(HashMap<String, String> map) {
		String params = "?";

		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			params += entry.getKey();
			params += "=";
			params += entry.getValue();
			if (iter.hasNext())
				params += "&";
		}

		return params;
	}
}
