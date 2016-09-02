package framework.base.dao;

import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import framework.base.entity.SuperEntity;
import framework.base.support.Result;

/**
 * @Description:基础数据库dao接口
 * @date Jan 27, 2014
 * @author:fgq
 */
public interface IBaseDao<Entity extends SuperEntity> {

	/** *核心类 ***/
	public Class<Entity> getEntityClass();

	public String getEntityName();

	public String getTableName();

	/** *查询类** */

	public Entity get(String id);

	public Entity get(String hql, Object[] params);

	public Entity get(String hql, Map<String, Object> map);

	public Entity get(Criterion... criterions);

	public List<Entity> list();

	public List<Entity> list(String hql);

	public List<Entity> list(String hql, Object[] params);

	public List<Entity> list(String hql, Map<String, Object> mapParams);
	
	public Result list(Map<String, Object> map);
	
	public Result list(int pageNo, int pageSize,Map<String, Object> map);
	
	public Result list(int pageNo, int pageSize, String hql, Map<String, Object> map);

	public Result list(int pageNo, int pageSize, Criterion... criterions);

	public List<Map<String, Object>> listTree(String tableName, String parentIdStartWith, Map<String, Object> map);

	public List<Map<String, Object>> listTree(String topSql, String childSql);

	public List<Entity> listTreeEntity(String topSql, String childSql,Object[] params);
	
	public List<Entity> listTree(String parentIdStartWith);

	public int count();

	public int count(String hql);

	public int count(String hql, Object[] params);

	public int count(String hql, Map<String, Object> map);

	public int count(Criterion... criterions);

	public Map<String, Object> map(String sql);

	public List<Map<String, Object>> listMap(String sql);

	public List<Map<String, Object>> listMap(String sql, Map<String, Object> mapParams);

	public Map<String, Object> mapProcedure(String procedureName, String[][] arrParam) throws Exception;

	public Map<String, Object> mapProcedureWithWork(final String procedureName, final String[][] arrParam);

	/** *操作类** */
	public Entity saveOrUpdate(Entity entity) ;
	
	public Entity save(Entity entity);

	public Entity update(Entity entity);

	public Entity merge(Entity entity);

	public boolean delete(String[] ids);

	public boolean delete(Entity entity);

	public boolean delete(List<Entity> listEntity);

	public void executeSql(String sql);

	public void executeSql(String sql, Object[] params);

	public void executeSql(String sql, Map<String, Object> map);

	/** *直接获取值** */
	public int getNextVal(String SequenceTable,String tableName);

	public int getPreVal(String SequenceTable,String tableName);

	public int getCurVal(String SequenceTable,String tableName);

}
