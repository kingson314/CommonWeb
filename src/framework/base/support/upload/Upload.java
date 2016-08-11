package framework.base.support.upload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 上传工具类
 * 
 * @author Administrator
 * 
 */
public class Upload {
	protected final static Log log = LogFactory.getLog(Upload.class);
	private List<FileBean> fileBeanList = null;
	// 上传表单的其它信息，除file类型的input
	private Map<String, String> formDatas = null;
	private String uploadStatus = "";
	// 文件单个最大大小
	private long maxSize;
	// 文件允许的格式为
	private String allowExts;
	// 文件不允许的格式为
	private String unAllowExts;
	public Upload(HttpServletRequest request,String savePath){
		fileBeanList = new ArrayList<FileBean>();
		formDatas = new HashMap<String, String>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);// 上传文件解析
			// 表单元素集合
			List<FileItem> formFieldList = new ArrayList<FileItem>();
			// 文件元素集合
			List<FileItem> fileFieldList = new ArrayList<FileItem>();
			if (items.size() > 0) {
				// 将表单元素和文件元素装载到不同的集合中
				for (FileItem fileItem : items) {
					if (fileItem.isFormField()) {
						formFieldList.add(fileItem);
					} else {
						fileFieldList.add(fileItem);
					}
				}
				/**
				 * 分解表单元素
				 */
				// 初始化文件上传属性bean
				FileBean fileBean = null;
				for (FileItem fileItem : formFieldList) {
					System.out.println("表单参数名:" + fileItem.getFieldName()
							+ "，表单参数值:" + fileItem.getString("UTF-8"));
					formDatas.put(fileItem.getFieldName(), fileItem.getString(
							"UTF-8").replace("'", "''"));
					if (fileItem.getFieldName().equals("articleUUID")) {
						savePath = savePath.replace("uuid", fileItem
								.getString("UTF-8"));
					}
				}
				log.info("保存的真实路径为：" + savePath);

				/**
				 * 分解文件元素
				 */
				for (FileItem fileItem : fileFieldList) {
					// 先判断文件件域是否选择了文件
					if (fileItem.getName() != null
							&& !fileItem.getName().equals("")) {
						// 文件名
						String fileName = fileItem.getName();
						// IE 和 FireFox 下的 getName() 得出的值不同
						// IE 下得到的是文件的完整路径
						if (fileName.contains("\\")) {
							// 截取得到完整路径后的文件名
							fileName = fileName.substring(fileName
									.lastIndexOf("\\") + 1);
						}
						// 文件大小
						long fileSize = fileItem.getSize();
						// 检查扩展名
						String extName = fileName.substring(
								fileName.lastIndexOf(".") + 1).toLowerCase();
						// 文件属性bean
						fileBean = new FileBean();
						fileBean.setFileName(fileName);
						fileBean.setExtName(extName);
						fileBean.setFileSize(fileSize);
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
						// 文件名转拼音
						String pingyingFileName =fileName;
						String newFileName = df.format(new Date()) + "_"
								+ pingyingFileName;

						fileBean.setFilePath(savePath + newFileName);
						fileBean.setFieldName(fileItem.getFieldName());
						fileBean.setNewFileName(newFileName);
						File f1 = new File(savePath);
						if (!f1.exists()) {
							f1.mkdirs();
						}
						System.out.println("新文件路径：" + newFileName);
						File uploadedFile = new File(savePath, newFileName);
						fileItem.write(uploadedFile);
						// 获取根目录对应的真实物理路径
						// 保存文件在服务器的物理磁盘中
						System.out.println("上传文件的大小:" + fileItem.getSize());
						System.out.println("上传文件的类型:"
								+ fileItem.getContentType());
						System.out.println("上传文件的名称:" + fileName);
						fileBeanList.add(fileBean);
						uploadStatus = "上传成功";
					} else {
						uploadStatus = "没有选择文件！";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			uploadStatus = "上传文件失败！";
		}
	}

	public List<FileBean> getFileBeanList() {
		return fileBeanList;
	}

	public void setFileBeanList(List<FileBean> fileBeanList) {
		this.fileBeanList = fileBeanList;
	}

	public Map<String, String> getFormDatas() {
		return formDatas;
	}

	public void setFormDatas(Map<String, String> formDatas) {
		this.formDatas = formDatas;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public String getAllowExts() {
		return allowExts;
	}

	public void setAllowExts(String allowExts) {
		this.allowExts = allowExts;
	}

	public String getUnAllowExts() {
		return unAllowExts;
	}

	public void setUnAllowExts(String unAllowExts) {
		this.unAllowExts = unAllowExts;
	}
}