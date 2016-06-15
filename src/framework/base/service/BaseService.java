package framework.base.service;

import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import framework.base.dao.IBaseDao;
import framework.base.entity.SuperEntity;
import framework.base.support.Result;

/**
 * @Description:基础服务类
 * @date Jan 27, 2014
 * @author:fgq
 */
public abstract class BaseService<Entity extends SuperEntity> implements IBaseService<Entity> {

	protected IBaseDao<Entity> baseDao;

	public void setBaseDao(IBaseDao<Entity> baseDao) {
		this.baseDao = baseDao;
	}

	/** *查询类** */
	public Entity get(String id) {
		return this.baseDao.get(id);
	}

	public Entity get(String hql, Object[] params) {
		return this.baseDao.get(hql, params);
	}

	public Entity get(String hql, Map<String, Object> map) {
		return this.baseDao.get(hql, map);
	}

	public Entity get(Criterion... criterions) {
		return this.baseDao.get(criterions);
	}

	public List<Entity> list() {
		return this.baseDao.list();
	}

	public List<Entity> list(String hql) {
		return this.baseDao.list(hql);
	}

	public List<Entity> list(String hql, Object[] params) {
		return this.baseDao.list(hql, params);
	}

	public List<Entity> list(String hql, Map<String, Object> mapParams) {
		return this.baseDao.list(hql, mapParams);
	}

	public Result list(int pageNo, int pageSize, String hql, Map<String, Object> map) {
		return this.baseDao.list(pageNo, pageSize, hql, map);
	}

	public Result list(int pageNo, int pageSize, Criterion... criterions) {
		return this.baseDao.list(pageNo, pageSize, criterions);
	}

	public List<Map<String, Object>> listTree(String tableName, String parentIdStartWith, Map<String, Object> map) {
		return this.baseDao.listTree(tableName, parentIdStartWith, map);
	}
	
	public List<Entity> listTreeEntity(String topSql, String childSql,Object[] params){
		return this.baseDao.listTreeEntity(topSql, childSql,params);
	}

	public Result resultTree(String tableName, String parentIdStartWith, Map<String, Object> map) {
		List<Map<String, Object>> list = this.baseDao.listTree(tableName, parentIdStartWith, map);
		return new Result(true, "执行成功", list);
	}

	public Result resultTree(String topSql, String childSql) {
		List<Map<String, Object>> listTree = this.baseDao.listTree(topSql, childSql);
		return new Result(listTree, -1);
	}

	public List<Map<String, Object>> listTree(String topSql, String childSql) {
		return this.baseDao.listTree(topSql, childSql);
	}

	public int count() {
		return this.baseDao.count();
	}

	public int count(String hql) {
		return this.baseDao.count(hql);
	}

	public int count(String hql, Object[] params) {
		return this.baseDao.count(hql, params);
	}

	public int count(String hql, Map<String, Object> map) {
		return this.baseDao.count(hql, map);
	}

	public int count(Criterion... criterions) {
		return this.baseDao.count(criterions);
	}

	public Map<String, Object> map(String sql) {
		return this.baseDao.map(sql);
	}

	public List<Map<String, Object>> listMap(String sql) {
		return this.baseDao.listMap(sql);
	}

	public List<Map<String, Object>> listMap(String sql, Map<String, Object> mapParams) {
		return this.baseDao.listMap(sql, mapParams);
	}

	public Map<String, Object> mapProcedure(String procedureName, String[][] arrParam) throws Exception {
		return this.baseDao.mapProcedure(procedureName, arrParam);
	}

	public Map<String, Object> mapProcedureWithWork(final String procedureName, final String[][] arrParam) {
		return this.baseDao.mapProcedureWithWork(procedureName, arrParam);
	}

	/** *操作类** */
	public Entity saveOrUpdate(Entity entity) {
		return this.baseDao.saveOrUpdate(entity);
	}
	public Entity save(Entity entity) {
		return this.baseDao.save(entity);
	}

	public Entity update(Entity entity) {
		return this.baseDao.update(entity);
	}

	public Entity merge(Entity entity) {
		return this.baseDao.merge(entity);
	}

	public boolean delete(String[] ids) {
		this.baseDao.delete(ids);
		return true;
	}

	public boolean delete(Entity entity) {
		this.baseDao.delete(entity);
		return true;
	}

	public boolean delete(List<Entity> listEntity) {
		this.baseDao.delete(listEntity);
		return true;
	}

	public void executeSql(String sql) {
		this.baseDao.executeSql(sql);
	}

	public void executeSql(String sql, Object[] params) {
		this.baseDao.executeSql(sql, params);
	}

	public void executeSql(String sql, Map<String, Object> map) {
		this.baseDao.executeSql(sql, map);
	}

	/** *直接获取值** */
	public int getNextVal(String SequenceTable,String tableName) {
		return this.baseDao.getNextVal(SequenceTable,tableName);
	}

	public int getPreVal(String SequenceTable,String tableName) {
		return this.baseDao.getPreVal(SequenceTable,tableName);
	}

	public int getCurVal(String SequenceTable,String tableName) {
		return this.baseDao.getCurVal(SequenceTable,tableName);
	}

}
