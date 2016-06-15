package common.util.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;


import common.util.conver.UtilConver;
import consts.Const;

/**
 * 记录日志
 * 
 * @author fgq 20120815
 * 
 */
public class UtilLog {

	private static Logger logger = Logger.getLogger(UtilLog.class);
	public static String LogPath = System.getProperty("user.dir") + "\\log\\";// 日志路径

	private static boolean isLoggerProving(String level) {
		boolean rs = false;
		// if (Fun.systemParamsValue.getLoggerLevel().equals(""))
		// rs = true;
		// else if (Fun.systemParamsValue.getLoggerLevel().indexOf(level) >= 0)
		rs = true;
		return rs;
	}

	// 日志级别从高到低:ERROR[0]、WARN[1]、INFO[2]、DEBUG[3]
	// 错误级别
	public static void logError(String message, Exception e) {
		if (!isLoggerProving("0"))
			return;
		message = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) + ":" + message;
		logger.error(message + getStrackTrace(e, "1"));
		System.out.println(message);
	}

	// 警告级别级别
	public static void logWarn(String message) {
		if (!isLoggerProving("1"))
			return;
		message = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) + ":" + message;
		logger.warn(message);
		System.out.println(message);
	}

	// 消息级别
	public static void logInfo(String message) {
		if (!isLoggerProving("2"))
			return;
		message = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) + ":" + message;
		logger.info(message);
		System.out.println(message);
	}

	// 调试级别
	public static void logDebug(String message) {
		if (!isLoggerProving("3"))
			return;
		message = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) + ":" + message;
		logger.debug(message);
		System.out.println(message);
	}

	// 获取异常代码
	public static String getStrackTrace(Exception e, String logType) {
		e.printStackTrace();
		String stackTraceMsg = "";
		if (logType == null)
			logType = "1";
		if (logType.equals("-1")) {
			return stackTraceMsg;
		} else if (logType.equals("0")) {
			stackTraceMsg = e.getMessage() + "\n";
			return stackTraceMsg;
		} else if (logType.equals("1")) {
			stackTraceMsg = e.getMessage() + "\n";
			stackTraceMsg = stackTraceMsg + "errorList:";
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				stackTraceMsg = stackTraceMsg + element.toString() + "\n";
			}
			return stackTraceMsg;
		}
		if (logType.equals("2")) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				stackTraceMsg = stackTraceMsg + element.toString() + "\n";
			}
			return stackTraceMsg;
		} else
			return stackTraceMsg;
	}

	// 编写文件日志
	public synchronized static void WriteLog(String fileName, String log, int LogLevel) {// 写日志
		if (LogLevel != 0)
			return;// 只记录日志级别为0的日志
		String sFilePath = LogPath + UtilConver.dateToStr("yyyy-MM-dd") + "\\";
		File fpath = new File(sFilePath);
		if (fpath.exists() == false)
			fpath.mkdirs();

		String fName = sFilePath + fileName + ".log";
		File f = new File(fName);
		File fPath = new File(f.getParent());
		if (fPath.exists() == false)
			fPath.mkdirs();
		if (f.exists() == false) // 文件不存在则创建一个
		{
			try {
				f.createNewFile();
			} catch (IOException e) {

				logError("WriteFile错误:", e);
			}
		}
		try {
			String content = "";
			String tmpline = "";
			BufferedReader fReader = new BufferedReader(new FileReader(fName)); // 读取文件原有内容
			while ((tmpline = fReader.readLine()) != null) {
				content += tmpline + "\n";
			}
			fReader.close();
			String dateTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
			content += dateTime + ":" + log;
			BufferedWriter fWriter = new BufferedWriter(new FileWriter(fName));
			fWriter.write(content);
			fWriter.flush();
			fWriter.close();
			// 每20k新增一个文件
			if ((double) (f.length() / 1024) > 20) {
				int i = 0;
				while (true) {
					String tmpname = sFilePath + fileName + (i) + ".log";// i转换字符
					File tmpf = new File(tmpname);
					if (tmpf.exists() == false) {
						f.renameTo(tmpf);
						break;
					}
					i = i + 1;
				}
			}
		} catch (IOException e) {
			logError("WriteLog IO错误:", e);
		} catch (Exception e) {
			logError("WriteLog 错误:", e);
		}
	}
}
