package common.code;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */

public class CodeIService {
	private static CodeIService instance;
	
	public static CodeIService getInstance(){
		if(instance==null){
			instance=new CodeIService();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("package "+entity.getPackagePath()+".service;                             \n");
		sb.append("                                                             \n");
		sb.append("import framework.base.service.IBaseService;              \n");
		sb.append("import "+entity.getPackagePath()+".entity."+entity.getEntityName()+";                          \n");
		sb.append("                                                             \n");
		sb.append("public interface I"+entity.getEntityName()+"Service extends IBaseService<"+entity.getEntityName()+"> {   \n");
		sb.append("                                                             \n");
		sb.append("}                                                            \n");
		return sb;
	}
}
