package framework.base.support.upload;

/**
 * 文件屬性bean
 * 
 * @author lushuifa
 */
public class FileBean {
	private String fileName;// 文件名
	private String fileContentType;// 上传文件的数据类型
	private long fileSize;// 文件的大小;单位byte
	private String extName;// 文件扩展名的大小
	private String fieldName;
	private String filePath;
	private String fileNote;// 文件说明
	private String newFileName;// 新文件名

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileNote() {
		return fileNote;
	}

	public void setFileNote(String fileNote) {
		this.fileNote = fileNote;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
}