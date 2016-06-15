package common.util.bean;

public class UtilBean {

	// 这个createObject处理基本类型和除枚举之外的类,将一个字符串的值转换为指定类型的对象
	public static <T> T createObject(Class<T> clazz, String value) {
		try {
			return (T) clazz.getConstructor(String.class).newInstance(value);
		} catch (Exception e) {
			// System.out.println(e.toString());
			return null;
		}
	}
}
