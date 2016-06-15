package common.code;

import common.util.conver.UtilConver;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */
public class CodeController {
	private static CodeController instance;
	
	public static CodeController getInstance(){
		if(instance==null){
			instance=new CodeController();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("		package "+entity.getPackagePath()+".controller;                              \n");
		sb.append("		import javax.annotation.Resource;                                            \n");
		sb.append("		import org.springframework.stereotype.Controller;                            \n");
		sb.append("		import org.springframework.web.bind.annotation.RequestMapping;               \n");
		sb.append("		import framework.base.controller.BaseContorller;                         \n");
		sb.append("		import framework.base.support.Result;                                    \n");
		sb.append("		import "+entity.getPackagePath()+".entity."+entity.getEntityName()+";                              \n");
		sb.append("		import "+entity.getPackagePath()+".service.I"+entity.getEntityName()+"Service;                     \n");
		sb.append("                                                                                  \n");
		sb.append("		/**                                                                          \n");
		sb.append("		 * @Description:                                                             \n");
		sb.append("		 * @date "+UtilConver.dateToStr("yyyy-MM-dd")+"                              \n");
		sb.append("		 * @author:fgq                                                               \n");
		sb.append("		 */                                                                          \n");
		sb.append("		@Controller                                                                  \n");
		sb.append("		@RequestMapping(\"/"+entity.getEntityName().toLowerCase()+"\")                                             \n");
		sb.append("		public class "+entity.getEntityName()+"Controller extends BaseContorller<"+entity.getEntityName()+"> {                   \n");
		sb.append("                                                                                  \n");
		sb.append("			@Resource                                                                \n");
		sb.append("			private I"+entity.getEntityName()+"Service "+entity.getEntityName().toLowerCase()+"Service;                                        \n");
		sb.append("                                                                                  \n");
		sb.append("			@RequestMapping(\"/list\")                                               \n");
		sb.append("			public void list() {                                                     \n");
		sb.append("				Result result = this."+entity.getEntityName().toLowerCase()+"Service.list(1, 10, \"from "+entity.getEntityName()+"\", null);   \n");
		sb.append("				this.print(result);                                                  \n");
		sb.append("			}                                                                        \n");
		sb.append("                                                                                  \n");
		sb.append("			@RequestMapping(\"/save\")                                               \n");
		sb.append("			public void save() {                                                     \n");
		sb.append("				this."+entity.getEntityName().toLowerCase()+"Service.save(this.getEntity());                             \n");
		sb.append("				this.print(new Result(true));                                        \n");
		sb.append("			}                                                                        \n");
		sb.append("                                                                                  \n");
		sb.append("			@RequestMapping(\"/update\")                                             \n");
		sb.append("			public void update() {                                                   \n");
		sb.append("				this."+entity.getEntityName().toLowerCase()+"Service.update(this.getEntity());                           \n");
		sb.append("				this.print(new Result(true));                                        \n");
		sb.append("			}                                                                        \n");
		sb.append("                                                                                  \n");
		sb.append("			@RequestMapping(\"/delete\")                                             \n");
		sb.append("			public void delete() {                                                   \n");
		sb.append("				this."+entity.getEntityName().toLowerCase()+"Service.delete(this.getEntity());                           \n");
		sb.append("				this.print(new Result(true));                                        \n");
		sb.append("			}                                                                        \n");
		sb.append("		}                                                                            \n");
		return sb;
	}
}
