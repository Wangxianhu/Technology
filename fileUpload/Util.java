import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

/** 
 * @author 王先虎
 * 工具类用于加密解密
 */
public class Util {
	/**
	 * 转化为json字符串
	 * @param object
	 * @return
	 */
	private static String toJSON(Object object) {
		Gson g = new Gson();
		return g.toJson(object);
	}
	/**
	 * 加密为map
	 * @param fromJson
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> encOption(Map<String, Object> fromJson) {
		String json = Util.toJSON(fromJson);
		String encrypt = null;
		try {
			encrypt = AES.encrypt(json);//参见encryption文件夹下的AES
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> m= new HashMap<String, Object>();
		m.put("data", encrypt);
		return m;
	}
	/**
	 * 解密为map
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> desOption(Map<String, String> map) {
		String data = map.get("data");
		String desEncrypt = null;
		try {
			desEncrypt = AES.desEncrypt(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson g=new Gson();
		@SuppressWarnings("unchecked")
		Map<String, String> fromJson = g.fromJson(desEncrypt.trim(), HashMap.class);
		System.out.println(fromJson.toString());
		return fromJson;
	}

}
