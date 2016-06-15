package common.code;

public class CodeService {
	private static CodeService instance;
	
	public static CodeService getInstance(){
		if(instance==null){
			instance=new CodeService();
		}
		return instance;
	}

	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("package "+entity.getPackagePath()+".service;                                             \n");
		sb.append("                                                                             \n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;               \n");
		sb.append("import org.springframework.stereotype.Service;                               \n");
		sb.append("import framework.base.service.BaseService;                               \n");
		sb.append("import "+entity.getPackagePath()+".dao.I"+entity.getEntityName()+"Dao;                                         \n");
		sb.append("import "+entity.getPackagePath()+".entity."+entity.getEntityName()+";                                          \n");
		sb.append("                                                                             \n");
		sb.append("@Service                                                                     \n");
		sb.append("public class "+entity.getEntityName()+"Service extends BaseService<"+entity.getEntityName()+"> implements I"+entity.getEntityName()+"Service { \n");
		sb.append("                                                                             \n");
		sb.append("	@Autowired                                                                  \n");
		sb.append("	public void setDao(I"+entity.getEntityName()+"Dao dao) {                                          \n");
		sb.append("		setBaseDao(dao);                                                        \n");
		sb.append("	}                                                                           \n");
		sb.append("}                                                                            \n");
		return sb;
	}
}
