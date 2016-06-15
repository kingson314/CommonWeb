package common.util.conver;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import common.util.string.UtilString;

/**
 * 格式转换
 * 
 * @author fgq 20120815
 * 
 */
public class UtilConver {
	// ArrayToString
	public static String ArrayToString(String[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(UtilString.isNil(arr[i]));
		}
		return sb.toString();
	}

	// 数组转Vector
	public static Vector<String> arrayToVector(String[] arr) {
		Vector<String> vector = new Vector<String>();
		for (String s : arr) {
			vector.add(s);
		}
		return vector;
	}

	// 格式化长度
	public static String formatInt(int value, String format) {
		String rs = String.valueOf(value);
		while (rs.length() < format.length()) {
			rs = "0" + rs;
		}
		return rs;
	}

	// 日期转换为字符串
	public synchronized static String dateToStr(java.util.Date date, String format) {
		if (date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	// 当期日期转换为字符串
	public synchronized static String dateToStr(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar cl = Calendar.getInstance();
		return df.format(cl.getTimeInMillis());
	}

	// 字符串转换为日期
	public synchronized static Date strToDate(String sDate, String format) throws ParseException {
		if (sDate == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(sDate);
	}

	/** **********************************beanMap******************************************* */
	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return returnMap 转化出来的 Map 对象
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Class<? extends Object> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				// System.out.println(propertyName);
				if (!"class".equals(propertyName) && !"date".equals(propertyName)) {
					Method readMethod = descriptor.getReadMethod();
					if (readMethod == null)
						continue;
					Object result = readMethod.invoke(bean, new Object[0]);
					returnMap.put(propertyName, result != null ? result : "");
				}
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException("分析类属性失败", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("分析类属性失败", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("分析类属性失败", e);
		}
		return returnMap;
	}

	/**
	 * 将一个Map对象转化为一个JavaBean
	 * 
	 * @param map
	 *            包含属性值的map
	 * @param bean
	 *            要转化的类型
	 * @return beanObj 转化出来的JavaBean对象
	 */
	public static <T> T mapToBean(Map<String, Object> paramMap, Class<T> clazz) {
		T beanObj = null;
		try {
			beanObj = clazz.newInstance();
			String propertyName = null;
			Object propertyValue = null;
			for (Map.Entry<String, Object> entity : paramMap.entrySet()) {
				propertyName = entity.getKey();
				propertyValue = entity.getValue();
				if (propertyValue == null)
					continue;
				Object value = null;
				if (propertyValue instanceof Object[]) {
					Object[] arrPropertyValue = (Object[]) propertyValue;
					for (int i = 0; i < arrPropertyValue.length; i++) {
						if (i == arrPropertyValue.length - 1) {
							value = UtilString.isNil(value) + UtilString.isNil((Object) arrPropertyValue[i]);
						} else
							value = UtilString.isNil(value) + UtilString.isNil((Object) arrPropertyValue[i]) + ";";
					}
				} else if (propertyValue instanceof String) {
					value = UtilString.isNil(propertyValue);
				} else if (propertyValue instanceof Integer) {
					value = propertyValue == null ? 0 : propertyValue;
				}
				//System.out.println(propertyName + ":" + value);
				setProperties(beanObj, propertyName, value);
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("不合法或不正确的参数", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("实例化JavaBean失败", e);
		} catch (Exception e) {
			throw new RuntimeException("Map转换为Java Bean对象失败", e);
		}
		return beanObj;
	}

	public static <T> void setProperties(T entity, String propertyName, Object propertyValue) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, entity.getClass());
			Method methodSet = pd.getWriteMethod();
			// System.out.println(pd.getPropertyType());
			Class<?> clazz = pd.getPropertyType();
			// System.out.println(propertyName + " " + propertyValue);
			//System.out.println(clazz);
			if (clazz.equals(String.class)) {
				methodSet.invoke(entity, propertyValue);
			} else if (clazz.equals(Date.class)) {
				// 日期字段暂时忽略
			} else if (clazz.equals(Integer.class)|| clazz.equals(int.class) ) {
				methodSet.invoke(entity, Integer.valueOf(propertyValue==null?"0":propertyValue.toString()));
			} else {
				Method m = clazz.getMethod("valueOf", String.class);
				methodSet.invoke(entity, m.invoke(clazz, propertyValue));
			}
		} catch (Exception e) {
			// Log.logInfo(propertyName + " 字段没转换");
			e.printStackTrace();
			System.out.println(propertyName + " 字段没转换");
		}
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("unchecked")
	public static Object convertMap(Map map, Class type) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象
		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName().toUpperCase();
			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;
				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("unchecked")
	public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/** **********************************beanMap******************************************* */


	/** *****************************(非固定格式)xmlToList************************************** */

	public static Map<String, Object> getLowercaseKeyMap(Map<String, Object> map) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			rsMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return rsMap;
	}
	public static Map<String, Object> getTreeMap(Map<String, Object> map) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(entry.getKey().equalsIgnoreCase("iconcls")){
				rsMap.put("iconCls", entry.getValue());
			}else{
				rsMap.put(entry.getKey().toLowerCase(), entry.getValue());
			}
		}
		return rsMap;
	}
	/** **********************************Test******************************************* */
	public static void testMapBean() {
		// TaskSpace ts = new TaskSpace();
		// ts.setTaskId(1L);
		//
		// Map<String, Object> um = beanToMap(ts);
		// for (Map.Entry<String, Object> entity : um.entrySet()) {
		// System.out.println(entity.getKey() + ":" + entity.getValue());
		// }
		//
		// TaskSpace newTs = mapToBean(um, TaskSpace.class);
		// System.out.println(newTs.getTaskId());
	}

	// @SuppressWarnings("unchecked")
	// public static void main(String[] args) throws IOException {
	// // testMapBean();
	// // testMapsToXml();
	// // testXmlToMaps();
	// // testMapToXml();
	// // testXmlToMap();
	// // testListToXml();
	// // testXmlToList();
	// testXmlFileToConMaps();
	// System.out.println("Done");
	//
	// }
	/** **********************************Test******************************************* */
}
