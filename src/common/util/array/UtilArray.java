package common.util.array;

import common.util.log.UtilLog;
import common.util.string.UtilString;

public class UtilArray {
	/**
	 * @Description:
	 * @date Jan 27, 2014
	 * @author:fgq
	 */

	// 获取数组索引
	public static int getArrayIndex(String[] arr, String item) {
		for (int i = 0; i < arr.length; i++) {
			if (UtilString.isNil(item).equalsIgnoreCase(arr[i]))
				return i;
		}
		return -1;
	}

	// 交换位置
	private static void swap(int[] array, int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}// 冒泡排序

	public static int[] bubbleOrder(int[] array, int method) {
		for (int i = 0; i < array.length; i++) {
			for (int j = array.length - 1; j > i; j--) {
				if (method == 2) {
					if (array[i] < array[j])
						swap(array, i, j);
				} else if (method == 1) {
					if (array[i] > array[j]) {
						swap(array, i, j);
					}
				}
			}
		}
		return array;
	}

	// 获取执行时段数组
	public static String[][] getArray2(String execTime) {
		String[][] etime = null;
		try {
			if (execTime == null)
				return null;
			if (execTime.trim().length() < 1 || execTime.equalsIgnoreCase("全部"))
				return null;
			String[] execTimeArray = execTime.split(";");
			int len = execTimeArray.length;

			etime = new String[len][2];
			for (int i = 0; i < len; i++) {
				execTimeArray[i] = execTimeArray[i].replace("[", "");
				execTimeArray[i] = execTimeArray[i].replace("]", "").trim();
				String[] p = execTimeArray[i].split(",");
				if (p.length > 0)
					etime[i][0] = p[0];
				else
					etime[i][0] = "";
				if (p.length > 1)
					etime[i][1] = p[1];
				else
					etime[i][1] = "";
			}
		} catch (Exception e) {
			UtilLog.logError("获取执行时段错误:", e);
			return etime;
		} finally {
		}
		return etime;
	}
}
