package common.util.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import common.util.file.UtilFile;
import common.util.json.UtilJackSon;
import common.util.string.UtilString;

public class UtilTree {

	/**
	 * @param pathArr
	 * @param rootPId 起始根节点
	 * @return json  将文件路径转换为树json结构
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String toTreeJson(String[] pathArr,String rootPId) throws JsonGenerationException, JsonMappingException, IOException{
		List<Map<String, String>> listTree = new ArrayList<Map<String, String>>();
		Map<String, String> mapDir = new HashMap<String, String>();
		for (String path : pathArr) {
			//System.out.println(path);
			if(path.lastIndexOf("demo.jsp")>=0)continue;
			if(UtilFile.isFile(path) &&(!path.endsWith(".jsp") &&!path.endsWith(".html") &&!path.endsWith(".htm")) )continue;
			if(path.indexOf("demo\\xbak")>=0)continue;
			String[] pathNameArr = path.split("\\\\");
			for (int i = 0; i < pathNameArr.length; i++) {
				String id = "";
				String parentId = "";
				for (int j = 0; j <= i; j++) {
					id += pathNameArr[j];
					if (j < pathNameArr.length - 1) {
						id += "\\";
					}
					if (j < i) {
						parentId += pathNameArr[j];
						if (!parentId.equals("")) {
							parentId += "\\";
						}
					}
				}
				if (mapDir.get(id) == null) {
					mapDir.put(id, "0");
					// System.out.println(id);
					if(!"".equals(UtilString.isNil(rootPId))){
						if(rootPId.indexOf(id)<0 ||rootPId.equals(id)){
							listTree.add(mapTreeNode(parentId, id, pathNameArr[i],id.replace(rootPId, "")));
						}
					}else{
						listTree.add(mapTreeNode(parentId, id, pathNameArr[i],id));
					}
				}
			}
		}
		return UtilJackSon.toJson(listTree);		
	}
	
	private static Map<String, String> mapTreeNode(String parentId, String id,
			String name,String url) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", parentId);
		map.put("id", id);
		map.put("name", name);
		map.put("src", url.replace("\\", "/"));
		System.out.println(url);
		if((url.indexOf("core\\")>=0 && getLevel(url)>=2)||(url.indexOf("plugin\\")>=0 && getLevel(url)>=1)||(url.indexOf("component\\")>=0 && getLevel(url)>=3)){
			map.put("open","false");
		}else{
			map.put("open", "true");
		}
		return map;
	}
	private static int  getLevel(String url){
		String[] arr=url.split("\\\\");
		if("".equals(arr[arr.length-1])){
			return arr.length-1;
		}
		return arr.length;
	}

	// public static void main(String[] args) throws JsonGenerationException,
	// JsonMappingException, IOException {
	// List<Map<String,String>>listTree=new ArrayList<Map<String,String>>();
	// Map<String,String>mapDir=new HashMap<String,String>();
	// String basePath = "E:\\360云盘\\百度云\\Projects\\Repository\\WebRoot\\sea\\";
	// mapDir.put(basePath, "0");
	// String[] pathArr = UtilFile.getAllFilePathByFilter(basePath,"");
	// for (String path : pathArr) {
	// System.out.println(path);
	// String[] pathNameArr=path.split("\\\\");
	// for(int i=0;i<pathNameArr.length;i++){
	// String id="";
	// String parentId="";
	// String name="";
	// for(int j=0;j<=i;j++){
	// id+=pathNameArr[j];
	// if(j<i){
	// parentId+=pathNameArr[j];
	// }
	// if(j<pathNameArr.length-1){
	// id+="\\";
	// if(!parentId.equals(""))
	// parentId+="\\";
	// }
	// }
	// if(mapDir.get(id)==null){
	// mapDir.put(id,"0");
	// System.out.println(id);
	// listTree.add(mapTreeNode(parentId,id,pathNameArr[i],""));
	// }
	// }
	// }
	// System.out.println(UtilJackSon.toJson(listTree));
	// }
}
