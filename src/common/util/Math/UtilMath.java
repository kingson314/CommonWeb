package common.util.Math;

import javax.swing.JRadioButton;
import common.util.log.UtilLog;

public class UtilMath {
	/**
	 * @Description:
	 * @date Jan 27, 2014
	 * @author:fgq
	 */

	// 0、1取反
	public synchronized static int oppositeVaule(int oldValue) {
		int rs = 0;
		try {
			if (oldValue == 0)
				rs = 1;
			else
				rs = 0;
		} catch (Exception e) {
			UtilLog.logError("01取反错误:", e);
			return rs;
		}
		return rs;

	}

	// 布尔取反
	public static void oppositeVaule(JRadioButton r1, JRadioButton r2) {
		if (r1.isSelected())
			r2.setSelected(false);
		else
			r2.setSelected(true);

	}

	// 数字比较
	public static boolean compare(String compareType, long value1, long value2) {
		boolean rs = false;
		if (compareType.equals("大于等于")) {
			if (value1 >= value2) {
				rs = true;
			}
		} else if (compareType.equals("大于")) {
			if (value1 > value2) {
				rs = true;
			}
		} else if (compareType.equals("等于")) {
			if (value1 == value2) {
				rs = true;
			}
		} else if (compareType.equals("不等于")) {
			if (value1 != value2) {
				rs = true;
			}
		} else if (compareType.equals("小于等于")) {
			if (value1 <= value2) {
				rs = true;
			}
		} else if (compareType.equals("小于")) {
			if (value1 < value2) {
				rs = true;
			}
		}
		return rs;
	}

	/**
	 * 
	 * @param 浮点数
	 * @return 浮点数的小数位数
	 * @date 2013-01-21
	 */
	public static int getDigits(double value) {
		int digits = 0;
		String sValue = String.valueOf(value);
		digits = sValue.substring(sValue.indexOf(".") + 1).length();
		return digits;
	}

	/**
	 * @Description:保留n为小数，4舍5入
	 * @param number
	 * @param index
	 * @return double
	 * @date May 26, 2014
	 * @author:fgq
	 */
	public static double round(double number, int index) {
		double result = 0;
		double temp = Math.pow(10, index);
		result = Math.round(number * temp) / temp;
		return result;
	}

	public static void main(String[] p) {
//		String t="123456";
//		System.out.println(t.substring(0,2)+":"+t.substring(2,4)+":"+t.substring(4,6));
	    System.out.println(round(2/28.0,5));
	}
}
