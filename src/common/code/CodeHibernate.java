package common.code;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */
public class CodeHibernate {
	private static CodeHibernate instance;
	
	public static CodeHibernate getInstance(){
		if(instance==null){
			instance=new CodeHibernate();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE hibernate-configuration PUBLIC                                  \n");
		sb.append("        \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"              \n");
		sb.append("       \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">  \n");
		sb.append("<hibernate-configuration>                                                 \n");
		sb.append("    <session-factory>                                                     \n");
		sb.append("        <mapping class=\""+entity.getPackagePath()+".entity."+entity.getEntityName()+"\"/>      \n");
		sb.append("    </session-factory>                                                    \n");
		sb.append("</hibernate-configuration>                                                \n");
		return sb;
	}
}
