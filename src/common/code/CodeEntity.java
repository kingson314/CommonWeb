package common.code;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */
public class CodeEntity {

	private static CodeEntity instance;

	public static CodeEntity getInstance() {
		if (instance == null) {
			instance = new CodeEntity();
		}
		return instance;
	}

	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("package "+entity.getPackagePath()+".entity;                     \n");
		sb.append("	                                                               \n");
		//sb.append("import javax.persistence.Column;                                \n");
		sb.append("import javax.persistence.Entity;                                \n");
		sb.append("import javax.persistence.Table;                                 \n");
		sb.append("import framework.base.entity.BaseEntity;                    \n");
		sb.append("	                                                               \n");
		sb.append("@Entity                                                         \n");
		sb.append("@Table(name = \""+entity.getTableName()+"\", catalog = \"\", schema = \"\") \n");
		sb.append("public class "+entity.getEntityName()+" extends BaseEntity {                          \n");
		sb.append("	private static final long serialVersionUID = 1L;               \n");
		sb.append("}                                                               \n");
		return sb;
	}
}
