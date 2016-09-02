package common.code;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */

public class CodeDao {
	private static CodeDao instance;
	
	public static CodeDao getInstance(){
		if(instance==null){
			instance=new CodeDao();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("package "+entity.getPackagePath()+".dao;                                      \n"); 
		sb.append("                                                                              \n");
		sb.append("import org.springframework.stereotype.Component;                              \n");
		sb.append("import framework.base.dao.BaseDao;                                        \n");
		sb.append("import "+entity.getPackagePath()+".entity."+entity.getEntityName()+";                               \n");
		sb.append("                                                                              \n");
		sb.append("@Component                                                                    \n");
		sb.append("public class "+entity.getEntityName()+"Dao extends BaseDao<"+entity.getEntityName()+"> implements I"+entity.getEntityName()+"Dao {              \n");
		sb.append("                                                                              \n");
		sb.append("}                                                                             \n");
		return sb;
	}
}
