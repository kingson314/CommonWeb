package common.code;

/**
 * @Description:
 * @date Feb 23, 2014
 * @author:fgq
 */

public class CodeExtjs {
	private static CodeExtjs instance;
	
	public static CodeExtjs getInstance(){
		if(instance==null){
			instance=new CodeExtjs();
		}
		return instance;
	}
	public StringBuilder getCode(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("var UtilGrid = Ext.create('extjs.core.util.UtilGrid', {});              \n");
		sb.append("var columnTitles = ['用户ID', '用户名称', '用户密码','用户说明'];            \n");
		sb.append("var columnFileds = ['id', 'name', 'password', 'memo'];                  \n");
		sb.append("var columnTypes = ['', '', '', ''];                                     \n");
		sb.append("                                                                        \n");
		sb.append("var fieldItems = [{                                                     \n");
		sb.append("			hidden : true                                                  \n");
		sb.append("		}, {}, {}, {}];                                                    \n");
		sb.append("                                                                        \n");
		sb.append("Ext.define('"+entity.getExtjsPackagePath()+"."+entity.getEntityName()+"', {                   \n");
		sb.append("			extend : 'extjs.core.plugins.grid.GridPanel',                  \n");
		sb.append("			baseUrl : 'system/"+entity.getEntityName().toLowerCase()+"',                                       \n");
		sb.append("			columns : UtilGrid.getColumns(columnFileds, columnTitles),     \n");
		sb.append("			fields : UtilGrid.getFields(columnFileds, columnTypes),        \n");
		sb.append("			fieldItems : UtilGrid.getFieldItems(columnFileds, columnTitles,\n");
		sb.append("					fieldItems)                                            \n");
		sb.append("		});                                                                \n");
		return sb;
	}
}
