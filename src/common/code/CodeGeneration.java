package common.code;

import common.util.file.UtilFile;
import common.util.log.UtilLog;

/**
 * @Description: 针对she框架的代码生产工具
 * @date Feb 23, 2014
 * @author:fgq
 */
public class CodeGeneration {
	public static void main(String[] args) {
		
		Entity entity=new Entity();
		entity.setTableName("patentwipo");
		entity.setEntityName("WIPO");
		entity.setPackagePath("com.patent.wipo");
		entity.setBasePathSrc("D:\\GitHub\\Repository\\src\\com\\patent\\");
		entity.setBaseUrl("/wipo");
		entity.setMenuName("页面配置");
		entity.init();
		UtilLog.logDebug(entity.getEntityName());
		
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
