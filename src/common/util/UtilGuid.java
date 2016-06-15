package common.util;

import java.util.UUID;

/**
 * @Description: 获取全局唯一标识
 * @date Feb 14, 2014
 * @author:fgq
 */
public class UtilGuid {
	public static String getGuid() {
		return UUID.randomUUID().toString();
	}
	public static void main(String[] args) {
		System.out.println(UtilGuid.getGuid());
		System.out.println(UtilGuid.getGuid().length());
	}
}
