package framework.base.support.upload;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadListener implements ProgressListener {  
	  
    @Override  
    public void update(long arg0, long arg1, int arg2) {  
        //arg0 已经上传多少字节  
        //arg1 一共多少字节  
        //arg2 正在上传第几个文件  
        System.out.println(arg0 +"\t" + arg1 +"\t" + arg2);  
    }  
}