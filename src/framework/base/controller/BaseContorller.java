package framework.base.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
<<<<<<< HEAD

=======
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
>>>>>>> 5aae4e3874a423c4b8ce7d3b202e2149fbb6412b
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import common.util.conver.UtilConver;
import common.util.file.UtilFile;
import common.util.json.UtilJackSon;
import common.util.log.UtilLog;
import common.util.string.UtilString;

import framework.base.entity.SuperEntity;
import framework.base.support.Result;
import framework.base.support.upload.FileBean;
import framework.base.support.upload.Upload;

public abstract class BaseContorller<Entity extends SuperEntity> {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected Map<String, Object> mapParams ;//非分页的其他参数
	protected Map<String, Integer> pageParams ;//分页参数 [pageSize,start,end,pageIndex]4个关键字
	protected List<HashMap<String, Object>> listUpload = null;

	@ModelAttribute
	protected void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = this.request.getSession();
		this.mapParams= new HashMap<String, Object>();
		this.pageParams=new HashMap<String,Integer>();
		this.getHttpServletRequestData();
	}

	protected void print(Object obj) {
		try {
			String json ="";
			if(obj instanceof  Result){
				 json = ((Result) obj).toJson();
			}else {
				json=UtilJackSon.toJson(obj);
			}
			this.print(json);
		} catch (Exception e) {
			UtilLog.logError("响应出错：", e);
		}
	}
	
	protected void print(String json) {
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.setStatus(HttpServletResponse.SC_OK);
			this.response.setHeader("Cache-Control", "no-cache");
			// response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			//UtilLog.logDebug(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			UtilLog.logError("响应出错：", e);
		}
	}

	// url不能加上文件后缀而且只能是JSP文件 例如：return this.forward("app/main/main") ;
	protected String forward(String url) {
		if ("".equals(UtilString.isNil(url))) {
			url = "";
		} else if (!"/".equals(url.substring(0, 1))) {
			url = "/" + url;
		}
		return url;
	}

	protected String forward(String url, String paramName, Object paramValue) {
		this.request.setAttribute(paramName, paramValue);
		return forward(url);
	}

	protected String forward(String url, Map<String, Object> mapParam) throws ServletException, IOException  {
		for (Map.Entry<String, Object> entry : mapParam.entrySet()) {
			this.request.setAttribute(entry.getKey(), entry.getValue());
		}
		//this.request.getRequestDispatcher(url).forward(this.request, this.response);
		//return null;
		return forward(url);
	}

	// url需加上完整的文件后缀 例如：return this.redirect("app/main/main.jsp") ;
	protected String redirect(String url) {
		if ("".equals(UtilString.isNil(url))) {
			url = "";
		} else if (!"/".equals(url.substring(0, 1))) {
			url = "/" + url;
		}
		return "redirect:" + url;
	}

	// 线程级别的属性存储，可考虑在使用后是否删除
	protected String redirect(String url, String paramName, Object paramValue) {
		this.session.setAttribute(paramName, paramValue);
		return redirect(url);
	}

	// 线程级别的属性存储，可考虑在使用后是否删除
	protected String redirect(String url, Map<String, Object> mapParam) {
		for (Map.Entry<String, Object> entry : mapParam.entrySet()) {
			this.session.setAttribute(entry.getKey(), entry.getValue());
		}
		return redirect(url);
	}

	/**
	 * @Title: getReqData
	 * @Description: 用于将request中的请求数据转换成请求对象， 同时包含对文本请求和二进制请求的处理，使处理后的内容统一
	 * @param
	 * @param request
	 * @param
	 * @return 设定文件
	 * @return ReqData 返回类型
	 * @throws
	 */
<<<<<<< HEAD
	@SuppressWarnings("unchecked")
=======
>>>>>>> 5aae4e3874a423c4b8ce7d3b202e2149fbb6412b
	private void getHttpServletRequestData() {
		
		String charsetName = request.getCharacterEncoding();
		if (charsetName == null) {
			charsetName = "utf-8";
		}
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
<<<<<<< HEAD
			List<MultipartFile> listUpload=new ArrayList<MultipartFile>();
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					listUpload.add(file);
				}
//				// 记录上传过程起始时的时间，用来计算上传时间
//				int pre = (int) System.currentTimeMillis();
//				// 取得上传文件
//				MultipartFile file = multiRequest.getFile(iter.next());
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					String myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						System.out.println(myFileName);
//						// 重命名上传后的文件名
//						String fileName = file.getOriginalFilename();
//						// 定义上传路径
//						String filePath = UploadPath + "/" + fileName;
//						UtilFile.writeFile(filePath, file.getBytes());
//					}
//				}
//				// 记录上传该文件后的时间
//				int finaltime = (int) System.currentTimeMillis();
//				System.out.println(finaltime - pre);
			}
			this.mapParams.put("listUpload", listUpload);
=======
			// 转型为MultipartHttpRequest  
	        try {  
	            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
	            List<MultipartFile> fileList = multipartRequest.getFiles("file");  
	            for (MultipartFile mf : fileList) {  
	                if(!mf.isEmpty()){  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        
	        
			Upload upload=new Upload(this.request,this.request.getSession().getServletContext().getRealPath("/upload/"));
			Map<String, String> fds = upload.getFormDatas();
			List<FileBean> fileBeanList =upload.getFileBeanList();
>>>>>>> 5aae4e3874a423c4b8ce7d3b202e2149fbb6412b
		}
		Map<String, String[]> map = request.getParameterMap();  
	    for(Map.Entry<String, String[]>entry:map.entrySet()){
	    	if("_".equals(entry.getKey().trim()))continue;
	    	if("pageNumber".equals(entry.getKey())||"offset".equals(entry.getKey())||"pageSize".equals(entry.getKey())||"pageIndex".equals(entry.getKey())||"start".equals(entry.getKey())||"end".equals(entry.getKey())){
	    		this.pageParams.put(entry.getKey(),Integer.valueOf(entry.getValue()[0]));
	    	}else{
	        	if(entry.getValue()!=null){
	        		if(entry.getKey().endsWith("[]")){
	        				this.mapParams.put(entry.getKey().replace("[]",""),entry.getValue());
	        		}else{
	        			this.mapParams.put(entry.getKey(),entry.getValue()[0]);
	        		}
	        	}
	    	}
	    }
	}

	@SuppressWarnings("unchecked")
	public Entity getEntity() {
		Class<?>  cl= (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return  (Entity) UtilConver.mapToBean(this.mapParams, cl);
	}
}
