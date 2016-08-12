package common.util.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import common.util.log.UtilLog;

public class UtilFile {
	/**
	 * @Description:
	 * @date Jan 27, 2014
	 * @author:fgq
	 */
	// 创建临时ok文件
	public static String createOkFile(String okFilePath) {
		String okFile = okFilePath + ".ok";
		try {
			// okFile = okFile.replace("\\", "/");
			// okFile = okFile.substring(0, okFile.indexOf("."));
			// okFile = okFile.trim() + "ok.txt";
			File newFile = new File(okFile);
			File fPath = new File(newFile.getParent());
			if (fPath.exists() == false)
				fPath.mkdirs();
			try {
				if (!newFile.exists())
					newFile.createNewFile();
			} catch (IOException e) {
				UtilLog.logError("新建" + okFilePath + "IO错误:", e);
			}
		} catch (Exception e) {
			UtilLog.logError("新建" + okFilePath + "错误:", e);
			return okFile;
		} finally {
		}
		return okFile;
	}

	// 路径补上完整盘符
	public static String formatFilePath(String filePath) {
		String rs = filePath;
		try {
			if (filePath == null)
				return "";
			if (filePath.trim().length() < 1)
				return "";
			String sfilePath = filePath.replace("\\", "/");
			if (!sfilePath
					.substring(sfilePath.length() - 1, sfilePath.length())
					.equals("/"))
				rs = sfilePath + "/";
			else
				rs = sfilePath;
		} catch (Exception e) {
			UtilLog.logError("格式化文件路径错误:", e);
			return rs;
		} finally {
		}
		return rs;
	}

	// 锁定文件
	public static boolean locked(File File, String FileName) {
		Boolean rs = true;
		try {
			FileChannel channel = new RandomAccessFile(File, "rw").getChannel();
			FileLock lock = channel.tryLock();
			lock.release();
			channel.close();
		} catch (Exception e) {
			UtilLog.logError("判断文件是否锁定错误:", e);
			return false;
		} finally {
		}
		return rs;
	}

	// 删除文件
	public static void delAllFile(String path) {
		try {
			if (path == null)
				return;
			if (path.trim().length() < 1)
				return;
			LinkedList<File> list = new LinkedList<File>();
			File dir = new File(path);
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					list.add(file[i]);
				else {
					file[i].delete();
				}
			}
			File tmp;
			while (!list.isEmpty()) {
				tmp = (File) list.removeFirst();
				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
							file[i].delete();
						}
					}
				} else {
					tmp.delete();
				}
			}
		} catch (Exception e) {
			UtilLog.logError("删除所有文件错误:", e);
		} finally {
		}
	}

	// 复制文件
	public static boolean copyFile(String oldPath, String newPath) {
		boolean rs = true;
		try {
			File file = new File(oldPath);
			if (file.isDirectory()) {
				return false;
			}
			Long fileTime = file.lastModified();
			if (file.exists()) {
				File destFile = new File(newPath);
				if (!destFile.exists()) {
					destFile.mkdirs();
					destFile.createNewFile();
				}
				FileInputStream inputStream = new FileInputStream(file);
				FileOutputStream outputStream = new FileOutputStream(newPath);

				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}
				inputStream.close();
				outputStream.close();
				destFile.setLastModified(fileTime);
			} else {
				rs = false;
			}
		} catch (IOException e) {
			UtilLog.logError("复制文件IO错误:", e);
		} catch (Exception e) {
			UtilLog.logError("复制文件错误:", e);
		}
		return rs;
	}

	// 读取文件
	public static String readFile(String filePath) {
		StringBuilder sb = new StringBuilder();
		try {
			String tmpline = "";
			BufferedReader fReader = new BufferedReader(
					new FileReader(filePath));
			// 读取文件原有内容
			while ((tmpline = fReader.readLine()) != null) {
				sb.append(tmpline + "\n");
			}
			fReader.close();
		} catch (Exception e) {
			UtilLog.logError("读取文件" + filePath + "错误:", e);
		} finally {
		}
		return sb.toString();
	}

	// 写文本文件
	public synchronized static void writeFile(String sFilePath,
			StringBuilder sbContent) {// 写文件
		writeFile(sFilePath, sbContent.toString());
	}

	// 写文本文件
	public synchronized static void writeFile(String sFilePath, String content) {// 写文件
		try {
			File f = new File(sFilePath);
			File fpath = new File(f.getParent());
			if (fpath.exists() == false)
				fpath.mkdirs();
			if (f.exists() == false) // 文件不存在则创建一个
			{
				try {
					f.createNewFile();
				} catch (IOException e) {
					UtilLog.logError("WriteFile错误:", e);
				}
			}
			BufferedWriter fWriter = new BufferedWriter(new FileWriter(
					sFilePath));
			// byte[] byt=content.getBytes("ISO-8859-1");//这里写原编码方式
			// String newStr=new String(byt,"utf-8");//这里写转换后的编码方式

			// content = new String(content.getBytes("UTF-8"));
			// content = URLEncoder.encode(content, "UTF-8");
			fWriter.write(content);
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
			UtilLog.logError("写文件" + sFilePath + "IO错误:", e);
		} catch (Exception e) {
			UtilLog.logError("写文件" + sFilePath + "错误:", e);
		} finally {
		}

	}
	
	// 写文本文件
	public synchronized static void writeFile(String filePath, byte[] content) throws IOException {
		BufferedOutputStream bos = null;
		try {
			File file = new File(filePath);
			// 判断文件路径是否存在
			if (!file.getParentFile().exists()) {
				// 文件路径不存在时，创建保存文件所需要的路径
				file.getParentFile().mkdirs();
			}
			// 创建文件（这是个空文件，用来写入上传过来的文件的内容）
			file.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(content);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("文件不存在。");
		} finally {
			if (null != bos) {
				bos.close();
			}
		}
	}

	// 获取所有的文件路径
	public static String[] getAllFilePath(String path) {
		String[] sfile = null;
		try {
			if (path == null)
				return null;
			if (path.trim().length() < 1)
				return null;
			List<String> filePath = new ArrayList<String>();
			LinkedList<File> list = new LinkedList<File>();
			File dir = new File(path);
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					list.add(file[i]);
				else {
					// System.out.println(file[i].getAbsolutePath());
					filePath.add(file[i].getAbsolutePath());
				}
			}
			File tmp;
			while (!list.isEmpty()) {
				tmp = (File) list.removeFirst();
				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
							// System.out.println(file[i].getAbsolutePath());
							filePath.add(file[i].getAbsolutePath());
						}
					}
				} else {
					// System.out.println(tmp.getAbsolutePath());
					filePath.add(tmp.getAbsolutePath());
				}
			}

			sfile = new String[filePath.size()];
			for (int i = 0; i < filePath.size(); i++) {
				sfile[i] = filePath.get(i);

			}
		} catch (Exception e) {
			UtilLog.logError("获取所有的文件路径错误:", e);
			return sfile;
		} finally {
		}
		return sfile;
	}

	// 获取当前目录下的包括所有子目录的文件路径/文件夹路径
	public static String[] getAllFilePath(String path, int flag) {
		String[] sfile = null;
		try {
			if (path == null)
				return null;
			if (path.trim().length() < 1)
				return null;
			List<String> filePath = new ArrayList<String>();
			LinkedList<File> list = new LinkedList<File>();
			File dir = new File(path);
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					list.add(file[i]);
				else {
					// System.out.println(file[i].getAbsolutePath());
					if (flag == 0) {
						filePath.add(file[i].getAbsolutePath());
					} else {
						filePath.add(file[i].getParent());
					}
				}
			}
			File tmp;
			while (!list.isEmpty()) {
				tmp = (File) list.removeFirst();
				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
							// System.out.println(file[i].getAbsolutePath());
							if (flag == 0) {
								filePath.add(file[i].getAbsolutePath());
							} else {
								filePath.add(file[i].getParent());
							}
						}
					}
				} else {
					// System.out.println(tmp.getAbsolutePath());
					if (flag == 0) {
						filePath.add(tmp.getAbsolutePath());
					} else {
						filePath.add(tmp.getParent());
					}
				}
			}

			sfile = new String[filePath.size()];
			for (int i = 0; i < filePath.size(); i++) {
				sfile[i] = filePath.get(i);
			}
		} catch (Exception e) {
			UtilLog.logError("根据标志获取所有的文件路径错误:", e);
			return sfile;
		} finally {
		}
		return sfile;
	}

	// 获取当前目录下的文件路径
	public static String[] getFilePathInCurrentDir(String path, int flag) {
		String[] sfile = null;
		try {
			if (path == null)
				return null;
			if (path.trim().length() < 1)
				return null;
			List<String> filePath = new ArrayList<String>();
			LinkedList<File> list = new LinkedList<File>();
			File dir = new File(path);
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					list.add(file[i]);
				else {
					// System.out.println(file[i].getAbsolutePath());
					if (flag == 0) {
						filePath.add(file[i].getAbsolutePath());
					} else {
						filePath.add(file[i].getParent());
					}
				}
			}
			File tmp;
			while (!list.isEmpty()) {
				tmp = (File) list.removeFirst();
				if (tmp.isDirectory()) {
					// file = tmp.listFiles();
					// if (file == null)
					// continue;
					// for (int i = 0; i < file.length; i++) {
					// if (file[i].isDirectory())
					// list.add(file[i]);
					// else {
					// // System.out.println(file[i].getAbsolutePath());
					// if (flag == 0) {
					// filePath.add(file[i].getAbsolutePath());
					// } else {
					// filePath.add(file[i].getParent());
					// }
					// }
					// }
				} else {
					// System.out.println(tmp.getAbsolutePath());
					if (flag == 0) {
						filePath.add(tmp.getAbsolutePath());
					} else {
						filePath.add(tmp.getParent());
					}
				}
			}

			sfile = new String[filePath.size()];
			for (int i = 0; i < filePath.size(); i++) {
				sfile[i] = filePath.get(i);

			}
		} catch (Exception e) {
			UtilLog.logError("根据标志获取所有的文件路径错误:", e);
			return sfile;
		} finally {
		}
		return sfile;
	}

	// 获取所有的文件路径 过滤后的
	public static String[] getAllFilePathByFilter(String path, String filter) {
		String[] sfile = null;
		try {
			if (path == null)
				return null;
			if (path.trim().length() < 1)
				return null;
			List<String> filePath = new ArrayList<String>();
			LinkedList<File> list = new LinkedList<File>();
			File dir = new File(path);
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					list.add(file[i]);
				else {
					// System.out.println(file[i].getAbsolutePath());
					if (file[i].getAbsolutePath().lastIndexOf(filter) >= 0)// 增加过滤
						filePath.add(file[i].getAbsolutePath());
				}
			}
			File tmp;
			while (!list.isEmpty()) {
				tmp = (File) list.removeFirst();
				if (tmp.isDirectory()) {
					file = tmp.listFiles();
					if (file == null)
						continue;
					for (int i = 0; i < file.length; i++) {
						if (file[i].isDirectory())
							list.add(file[i]);
						else {
							if (file[i].getAbsolutePath().lastIndexOf(filter) >= 0)// 增加过滤
								filePath.add(file[i].getAbsolutePath());
						}
					}
				} else {
					if (tmp.getAbsolutePath().lastIndexOf(filter) >= 0)// 增加过滤
						filePath.add(tmp.getAbsolutePath());
				}
			}

			sfile = new String[filePath.size()];
			for (int i = 0; i < filePath.size(); i++) {
				sfile[i] = filePath.get(i);
			}
		} catch (Exception e) {
			UtilLog.logError("根据过滤获取所有的文件路径错误:", e);
			return sfile;
		} finally {
		}
		return sfile;
	}

	public static boolean exists(String filePath) {
		File f = new File(filePath);
		return f.exists();
	}

	public static boolean isFile(String path){
		return new File(path).isFile();
	}
	public static void main(String[] args) {
//		String oldPath = "E:\\360云盘\\百度云\\Projects\\Repository\\WebRoot";
//		String[] pathArr = UtilFile.getAllFilePath(oldPath);
//		for (String path : pathArr) {
//			UtilFile.copyFile(path,"E:\\360云盘\\百度云\\Projects\\apache-tomcat-9.0.0.M1\\webapps\\Repository"
//							+ path.replace(oldPath, ""));
//		}
//		System.out.println("finished");
		String[] pathArr=UtilFile.getAllFilePath("E:\\360云盘\\百度云\\Projects\\Repository\\WebRoot\\sea\\demo\\component");
		for(String path:pathArr){
			System.out.println(path);
		}
	}
}
