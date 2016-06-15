package common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import common.util.conver.UtilConver;
import consts.Const;

public class UtilDate {
	/**
	 * @Description:
	 * @date Jan 27, 2014
	 * @author:fgq
	 */

	// 计算时间差
	public static String diffTime(String begTime, String endTime, String dateFormat) throws ParseException {
		long dif = UtilConver.strToDate(endTime, dateFormat).getTime() - UtilConver.strToDate(begTime, dateFormat).getTime();
		long day = dif / (24 * 60 * 60 * 1000);
		long hour = (dif / (60 * 60 * 1000) - day * 24);
		long min = ((dif / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (dif / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		return day + "天" + hour + "小时" + min + "分" + s + "秒";
	}

	// 计算当期时间的前后N分钟的时间
	public static String addDateMinut(String day, int x, String dateFormat) {
		Date date = null;
		try {
			date = UtilConver.strToDate(day, dateFormat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return UtilConver.dateToStr(date, dateFormat);
	}

	public static long diffMinute(String begTime, String endTime, String dateFormat) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date beg = df.parse(begTime);
		Date end = df.parse(endTime);
		long dif = end.getTime() - beg.getTime();
		long min = dif / 60000;
		return min;
	}

	public static long diffSecond(String begTime, String endTime, String dateFormat) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date beg = df.parse(begTime);
		Date end = df.parse(endTime);
		long dif = end.getTime() - beg.getTime();
		long min = dif / 1000;
		return min;
	}

	public static String addMinute(String begTime, int minute, String dateFormat) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date beg = df.parse(begTime);
		long endtime = beg.getTime() + minute * 60000;
		return df.format(new Date(endtime));
	}

	public static String addSecond(String begTime, int second, String dateFormat) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date beg = df.parse(begTime);
		long endtime = beg.getTime() + second * 1000;
		return df.format(new Date(endtime));
	}

	// 判断是否为HH:mm类型的字符串
	public static boolean isTime(String str) {
		boolean rs = false;
		try {
			if (str.indexOf(":") < 0)
				return false;
			UtilConver.strToDate(str, "HH:mm");
			rs = true;
		} catch (Exception e) {
		}
		return rs;
	}

	public static String getDayOfWeek(String date_yyyyMMdd) throws ParseException {
		String[] weekArr = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(UtilConver.strToDate(date_yyyyMMdd, Const.fm_yyyyMMdd));
		return weekArr[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public static void main(String[] agr) throws Exception {
		// String begTime="20140508 1624";
		// String endTime=UtilDate.addMinute(begTime,-1,"yyyyMMdd HHmm");
		// System.out.println(endTime);
		System.out.print(getDayOfWeek("20140719"));
	}
}
