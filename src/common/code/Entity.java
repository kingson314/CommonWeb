package common.code;

import common.util.string.UtilString;

/**
 * @Description: 代码生成所需的实体参数类
 * @date Feb 23, 2014
 * @author:fgq
 */
public class Entity {
	// 表名称
	private String tableName;
	// 实体类名称
	private String entityName;
	// 存放在src的基本路径
	private String basePathSrc;
	//Src代码包路径
	private String packagePath;
	// 基本路径 比如 /system/user
	private String baseUrl;
	// 类型：grid、tree、treeGrid
	private String type;
	// 配置sys_menu表的菜单名称
	private String menuName;
	// 配置sys_menu表的父菜单名称
	private String menuParentName;

	/** *******以下为自动配置的参数************* */

	// controller的文件路径
	private String pathController;
	// service的文件路径
	private String pathService;
	// service接口的文件路径
	private String pathIService;
	// dao的文件路径
	private String pathDao;
	// dao接口的文件路径
	private String pathIDao;
	// entity的文件路径
	private String pathEntity;
	// hibernate配置文件的路径
	private String pathHibernate;
	// 配置sys_menu表的url路径
	private String menuUrl;

	public void init() {
		this.entityName=this.entityName;//(this.entityName);
		if (this.basePathSrc.lastIndexOf("/") < 0) {
			this.basePathSrc += "/";
		}
		this.pathController = this.basePathSrc + this.entityName.toLowerCase() + "/controller/" + (this.entityName) + "Controller.java";
		this.pathIDao = this.basePathSrc + this.entityName.toLowerCase() + "/dao/I" + (this.entityName) + "Dao.java";
		this.pathDao = this.basePathSrc + this.entityName.toLowerCase() + "/dao/" + (this.entityName) + "Dao.java";
		this.pathEntity = this.basePathSrc + this.entityName.toLowerCase() + "/entity/" + (this.entityName) + ".java";
		this.pathHibernate = this.basePathSrc + this.entityName.toLowerCase() + "/entity/hibernate.xml";
		this.pathIService= this.basePathSrc + this.entityName.toLowerCase() + "/service/I" + (this.entityName) + "Service.java";
		this.pathService = this.basePathSrc + this.entityName.toLowerCase() + "/service/" + (this.entityName) + "Service.java";
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuParentName() {
		return menuParentName;
	}

	public void setMenuParentName(String menuParentName) {
		this.menuParentName = menuParentName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getBasePathSrc() {
		return basePathSrc;
	}

	public void setBasePathSrc(String basePathSrc) {
		this.basePathSrc = basePathSrc;
	}


	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPathController() {
		return pathController;
	}

	public void setPathController(String pathController) {
		this.pathController = pathController;
	}

	public String getPathService() {
		return pathService;
	}

	public void setPathService(String pathService) {
		this.pathService = pathService;
	}

	public String getPathIService() {
		return pathIService;
	}

	public void setPathIService(String pathIService) {
		this.pathIService = pathIService;
	}

	public String getPathDao() {
		return pathDao;
	}

	public void setPathDao(String pathDao) {
		this.pathDao = pathDao;
	}

	public String getPathIDao() {
		return pathIDao;
	}

	public void setPathIDao(String pathIDao) {
		this.pathIDao = pathIDao;
	}

	public String getPathEntity() {
		return pathEntity;
	}

	public void setPathEntity(String pathEntity) {
		this.pathEntity = pathEntity;
	}

	public String getPathHibernate() {
		return pathHibernate;
	}

	public void setPathHibernate(String pathHibernate) {
		this.pathHibernate = pathHibernate;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}


	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
}
