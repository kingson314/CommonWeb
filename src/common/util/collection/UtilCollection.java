package common.util.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description:
 * @date May 9, 2014
 * @author:fgq
 */
public class UtilCollection {

	// 判断null对象返回空值
	public static String isNilMap(Map<?, ?> map, String key) {
		if (map == null)
			return "";
		else if (map.get(key) == null)
			return "";
		return map.get(key).toString();
	}

	// 判断null对象返回空值
	public static Object isNilMapIgnoreCase(Map<?, ?> map, String key) {
		if (map == null)
			return "";
		else if (map.get(key) == null) {
			if (map.get(key.toUpperCase()) == null) {
				return "";
			} else {
				return map.get(key.toUpperCase());
			}
		}
		return map.get(key);
	}

	// 判断null对象返回空值
	public static String isNilMap(Map<?, ?> map, String key, String defaultValue) {
		if (map == null)
			return defaultValue;
		else if (map.get(key) == null)
			return defaultValue;
		return map.get(key).toString();
	}

	/**
	 * @param map
	 *            <String,String
	 * @return 对象元素map
	 * @date 2013-01-22
	 */
	public static Map<String, Object> getObjectMap(Map<String, String> map) {
		Map<String, Object> mapRs = new HashMap<String, Object>();
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			mapRs.put(entry.getKey().toString(), (Object) entry.getValue());
		}
		return mapRs;
	}

	/**
	 * @param map
	 *            <String,String>
	 * @return 对象元素map
	 * @date 2013-01-22
	 */
	public static Map<String, String> getStringMap(Map<String, Object> map) {
		Map<String, String> mapRs = new HashMap<String, String>();
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			mapRs.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return mapRs;
	}

}
