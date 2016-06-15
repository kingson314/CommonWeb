package common.code;
/** 
 * @Description: 
 * @date Feb 23, 2014
 * @author:fgq 
 */

public class CodeIDao {
	
	private static CodeIDao instance;
	
	public static CodeIDao getInstance(){
		if(instance==null){
			instance=new CodeIDao();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("	package "+entity.getPackagePath()+".dao;                             \n");
		sb.append("	                                                         \n");
		sb.append("	import framework.base.dao.IBaseDao;                  \n");
		sb.append("	import "+entity.getPackagePath()+".entity."+entity.getEntityName()+";                      \n");
		sb.append("	                                                         \n");
		sb.append("	public interface I"+entity.getEntityName()+"Dao extends IBaseDao<"+entity.getEntityName()+"> {       \n");
		sb.append("	}                                                        \n");
		return sb;
	}
}
