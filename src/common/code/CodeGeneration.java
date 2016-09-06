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
		//配置项
		String packpageParent="system";//本类的所属分类，即上一层包名称
		String tablename="sys_department";//表名称
		String type="SuperEntity";//BaseEntiry,SuperEntity
		String menuName="";
		//----------
		final String projectBasePath=System.getProperty("user.dir").replace("CommonWeb", "Repository");
		String[] tableNameArr=tablename.split("_");
		String entityName="";
		for(String str:tableNameArr){
			entityName+=UtilString.upperFirstChar(str);
		}
		String baseUrl="/"+entityName+"/";
		String packagePath="com."+packpageParent+"."+entityName;
		String basePathSrc=projectBasePath+"\\src\\com\\"+packpageParent;

		Entity entity=new Entity();
		entity.setTableName(tablename);
		entity.setEntityName(entityName);
		entity.setPackagePath(packagePath);
		entity.setBasePathSrc(basePathSrc);
		entity.setBaseUrl(baseUrl);
		entity.setMenuName(menuName);
		entity.setType(type);
		entity.init();
		UtilLog.logDebug(entity.getEntityName());
		new CodeGeneration(entity);
	}
	
	private CodeGeneration(Entity entity){
		if(UtilFile.exists(entity.getPathEntity())){
			System.out.println("该功能已存在，请检查后覆盖！");
			return;
		}

		//entity
		StringBuilder codeEntity=CodeEntity.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathEntity(), codeEntity);
		UtilLog.logDebug(entity.getPathEntity());
		
		//hibernate
		StringBuilder codeHibernate=CodeHibernate.getInstance().getCode(entity);
		UtilFile.writeFile(entity.getPathHibernate(), codeHibernate);
		UtilLog.logDebug(entity.getPathHibernate());	
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
		
		UtilLog.logDebug("done");
	}
}
