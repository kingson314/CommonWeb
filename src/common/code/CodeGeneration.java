package common.code;

import common.util.file.UtilFile;
import common.util.log.UtilLog;
import common.util.string.UtilString;

/**
 * @Description: 针对she框架的代码生产工具
 * @date Feb 23, 2014
 * @author:fgq
 */
public class CodeGeneration {
	public static void main(String[] args) {
		final String projectBasePath=System.getProperty("user.dir").replace("CommonWeb", "Repository");
		//配置项
		String packpageParent="system";//本类的所属分类，即上一层包名称
		String tablename="sys_department";//表名称
		String menuName="";
		//----------
		Entity entity=new Entity();
		String[] tableNameArr=tablename.split("_");
		String entityName=UtilString.upperFirstChar(tableNameArr[0])+UtilString.upperFirstChar(tableNameArr[1]);
		String baseUrl="/"+tablename.replace("_", "")+"/";
		String packagePath="com."+packpageParent+"."+tablename.replace("_", "");
		String basePathSrc=projectBasePath+"\\src\\com\\"+packpageParent;
		entity.setTableName(tablename);
		entity.setEntityName(entityName);
		entity.setPackagePath(packagePath);
		entity.setBasePathSrc(basePathSrc);
		entity.setBaseUrl(baseUrl);
		entity.setMenuName(menuName);
		entity.init();
		UtilLog.logDebug(entity.getEntityName());
		create(entity);
	}
	
	private static void create(Entity entity){
		//entity
		StringBuilder codeEntity=CodeEntity.getInstance().getCode(entity);
		if(UtilFile.exists(entity.getPathEntity())){
			System.out.println("该功能已存在，请检查后覆盖！");
			return;
		}
		UtilFile.writeFile(entity.getPathEntity(), codeEntity);
		UtilLog.logDebug(entity.getPathEntity());
		//iDao
		StringBuilder codeIDao=CodeIDao.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathIDao(), codeIDao);
		UtilLog.logDebug(entity.getPathIDao());
		//dao
		StringBuilder codeDao=CodeDao.getInstance().getCode(entity);;
		UtilFile.writeFile(entity.getPathDao(), codeDao);
		UtilLog.logDebug(entity.getPathDao());
		//iService
		StringBuilder codeService=CodeService.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathService(), codeService);
		UtilLog.logDebug(entity.getPathService());
		//service
		StringBuilder codeIService=CodeIService.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathIService(), codeIService);
		UtilLog.logDebug(entity.getPathIService());
		//controller
		StringBuilder codeController=CodeController.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathController(), codeController);
		UtilLog.logDebug(entity.getPathController());
		//hibernate
		StringBuilder codeHibernate=CodeHibernate.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathHibernate(), codeHibernate);
		UtilLog.logDebug(entity.getPathHibernate());		
		
		UtilLog.logDebug("done");
	}
}
