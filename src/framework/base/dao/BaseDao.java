package framework.base.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilSql;
import common.util.string.UtilString;
import framework.base.entity.BaseEntity;
import framework.base.entity.SuperEntity;
import framework.base.support.Result;

/**
 * @Description: 基础数据库操作Dao
 * @date Jan 27, 2014
 * @author:fgq
 */
public class BaseDao<Entity extends SuperEntity> implements IBaseDao<Entity> {

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	private static HibernateTemplate hibernateTemplate = null;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public HibernateTemplate getHibernateTemplate() {
		// 首先，检查原来的hibernateTemplate实例是否还存在
		if (hibernateTemplate == null) {
			// 如果不存在，新建一个HibernateTemplate实例
			hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
		return hibernateTemplate;
	}

	protected Connection getConnection() throws SQLException {
		return (Connection) SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
	}

	/** *核心类 ***/
	@SuppressWarnings("unchecked")
	public Class<Entity> getEntityClass() {
		return (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public String getEntityName() {
		return getEntityClass().getSimpleName();
	}

	public String getTableName() {
		AbstractEntityPersister classMetadata = (AbstractEntityPersister) sessionFactory.getClassMetadata(this.getEntityClass());
		return classMetadata.getTableName();
	}

	/** *查询类** */
	@SuppressWarnings("unchecked")
	public Entity get(String id) {
		return (Entity) getSession().get(this.getEntityClass(), id);
	}

	@SuppressWarnings("unchecked")
	public Entity get(String hql, Object[] params) {
		Query query = this.getSession().createQuery(hql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return (Entity) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Entity get(String hql, Map<String, Object> map) {
		Query query = this.getSession().createQuery(hql);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		return (Entity) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Entity get(Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass());
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		}
		return (Entity) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Entity> list() {
		String hql = "from " + this.getEntityName();
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Entity> list(String hql) {
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Entity> list(String hql, Object[] params) {
		Query query = this.getSession().createQuery(hql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Entity> list(String hql, Map<String, Object> mapParams) {
		Query query = this.getSession().createQuery(hql);
		if (mapParams != null) {
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Result list(int pageNo, int pageSize, String hql, Map<String, Object> map) {
		Query query = this.getSession().createQuery(hql);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Object> list = query.list();
		int rowCount = this.count(hql, map);
		return new Result(list, rowCount);
	}

	@SuppressWarnings("unchecked")
	public Result list(int pageNo, int pageSize, Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass());
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		} // 获取根据条件分页查询的总行数
		int rowCount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<Object> list = criteria.list();
		return new Result(list, rowCount);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listTree(String tableName, String parentIdStartWith, Map<String, Object> mapParams) {
		String sql = "select * from  " + tableName + " start with parentid='" + parentIdStartWith
				+ "'	connect by  prior  id=parentid	order  siblings by ord";
		Query query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		if (mapParams != null) {
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		return query.list();
	}

	// public List<Map<String,Object>> listModuleTree(List<Map<String, Object>>
	// listModuleTree ,String sqlTreeNode,Map<String, Object> mapParamsTreeNode)
	// {
	// List<Map<String, Object>> listTree = new ArrayList<Map<String,
	// Object>>();
	// for(Map<String,Object>mapModuleTree:listModuleTree){
	// String pid=UtilString.isNil(mapModuleTree.get("ID"));
	// String sql = String.format(sqlTreeNode, pid);
	// List<Map<String, Object>> listNode = this.listMap(sql,mapParamsTreeNode);
	// mapModuleTree.put("leaf", listNode.size() == 0 ? true : false);
	// listTree.add(UtilConver.getLowercaseKeyMap(mapModuleTree));
	// for (Map<String, Object> map : listNode) {
	// map.put("leaf", true );
	// listTree.add(UtilConver.getLowercaseKeyMap(map));
	// }
	// if (listTree.size() > 0) {
	// mapParent.put("data", listTree);
	// }
	// }
	// return listTree;
	// }

	public List<Map<String, Object>> listTree(String topSql, String childSql) {
		List<Map<String, Object>> list = this.listMap(topSql);
		List<Map<String, Object>> listTree = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			String id = map.get("ID").toString();
			map.put("expanded", true);
			int childCnt = getChildren(map, id, childSql);
			map.put("leaf", childCnt == 0 ? true : false);
			listTree.add(UtilConver.getTreeMap(map));
		}
		return listTree;
	}

	public List<Entity> listTreeEntity(String topSql, String childSql,Object[] params) {
		List<Entity> listEntity = new ArrayList<Entity>();
		List<Entity> list = this.list(topSql,params);
		for (Entity entity : list) {
			listEntity.add(entity);
			getChildrenEntity(listEntity,  entity.getId(),childSql);
		}
		return listEntity;
	}
	
	private void getChildrenEntity(List<Entity> listEntity,String id ,String childSql) {
		List<Entity> list = this.list(childSql,new Object[]{id});
		if (list.size() > 0) {
			for (Entity entity : list) {
				listEntity.add(entity);
				String pId =entity.getId();
				getChildrenEntity(listEntity,pId, childSql);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public List<Entity> listTree(String parentIdStartWith) {
		String sql = "select * from " + this.getTableName() + " start with parentId='" + parentIdStartWith
				+ "'	connect by  prior  id=parentId	order  siblings by ord";
		Query query = this.getSession().createSQLQuery(sql).addEntity(this.getEntityClass());
		return query.list();
	}

	/**
	 * 获取树节点数据集
	 * 
	 * @param mapParent
	 * @param id
	 */

	private int getChildren(Map<String, Object> mapParent, String id, String childSql) {
		int childCount = 0;
		String sql = String.format(childSql, id);
		List<Map<String, Object>> list = this.listMap(sql);
		List<Map<String, Object>> listTree = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				childCount += 1;
				String pId = map.get("ID").toString();
				int childCnt = getChildren(map, pId, childSql);
				map.put("leaf", childCnt == 0 ? true : false);
				listTree.add(UtilConver.getTreeMap(map));
			}
		}
		if (listTree.size() > 0) {
			mapParent.put("children", listTree);
		}
		return childCount;
	}

	public int count() {
		String hql = "select count(1) from  " + this.getEntityName();
		Query query = this.getSession().createQuery(hql);
		return ((Integer) query.uniqueResult()).intValue();
	}

	public int count(String hql) {
		Query query = this.getSession().createQuery(hql);
		return query.list().size();
	}

	public int count(String hql, Object[] params) {
		Query query = this.getSession().createQuery(hql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.list().size();
	}

	public int count(String hql, Map<String, Object> map) {
		Query query = this.getSession().createQuery(hql);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		return query.list().size();
	}

	public int count(Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass());
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		}
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	public Map<String, Object> map(String sql) {
		return listMap(sql).get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listMap(String sql) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.list();
		return list;

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listMap(String sql, Map<String, Object> mapParams) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		if (mapParams != null) {
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		return query.list();
	}

	public Map<String, Object> mapProcedure(String procedureName, String[][] arrParam) throws Exception {
		Map<String, Object> mapRs = new HashMap<String, Object>();
		CallableStatement ps = null;
		Connection con = null;
		try {
			String param = "";
			if (arrParam != null) {
				for (int i = 0; i < arrParam.length; i++) {
					param = param + "?,";
				}
				if (param.length() > 0)
					param = param.substring(0, param.length() - 1);
			}
			con =  this.getConnection();
			ps = con.prepareCall("{ call " + procedureName + "(" + param + ")" + "	 }");
			if (arrParam != null) {
				for (int i = 0; i < arrParam.length; i++) {
					// System.out.println(params[i][0]);
					// System.out.println(params[i][1]);
					// System.out.println(params[i][2]);
					// System.out.println(UtilString.isNil(params[i][3]));
					if (arrParam[i][2].equalsIgnoreCase("in")) {
						UtilSql.setParams(ps, UtilString.isNil(arrParam[i][3]), UtilString.isNil(arrParam[i][1]), i + 1, arrParam[i][4]);
						mapRs.put(arrParam[i][0], arrParam[i][3] == null ? "" : arrParam[i][3]);
					} else if (arrParam[i][2].equalsIgnoreCase("out")) {
						if (arrParam[i][1].equalsIgnoreCase("varchar") || arrParam[i][1].equalsIgnoreCase("String")) {
							ps.registerOutParameter(i + 1, Types.VARCHAR);
						} else if (arrParam[i][1].equalsIgnoreCase("number") || arrParam[i][1].equalsIgnoreCase("float")) {
							ps.registerOutParameter(i + 1, Types.NUMERIC);
						} else if (arrParam[i][1].equalsIgnoreCase("integer") || arrParam[i][1].equalsIgnoreCase("int")) {
							ps.registerOutParameter(i + 1, Types.INTEGER);
						} else if (arrParam[i][1].equalsIgnoreCase("date")) {
							ps.registerOutParameter(i + 1, Types.DATE);
						} else if (arrParam[i][1].equalsIgnoreCase("cursor")) {
							ps.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.CURSOR);
						}
					}
				}
			}
			ps.execute();

			if (arrParam != null) {
				for (int i = 0; i < arrParam.length; i++) {
					if (arrParam[i][2].equalsIgnoreCase("out")) {
						if (arrParam[i][1].equalsIgnoreCase("cursor")) {
							ResultSet rs = (ResultSet) ps.getObject(1);
							mapRs.put(arrParam[i][0], rs);
						} else {
							mapRs.put(arrParam[i][0], UtilString.isNil(ps.getObject(i + 1)).toString());
						}
					}
				}
			}
		} finally {
			UtilSql.close(ps, con);
		}
		return mapRs;
	}

	public Map<String, Object> mapProcedureWithWork(final String procedureName, final String[][] arrParam) {
		final Map<String, Object> mapRs = new HashMap<String, Object>();
		this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						CallableStatement ps = null;
						try {
							String param = "";
							if (arrParam != null) {
								for (int i = 0; i < arrParam.length; i++) {
									param = param + "?,";
								}
								if (param.length() > 0)
									param = param.substring(0, param.length() - 1);
							}
							ps = con.prepareCall("{ call " + procedureName + "(" + param + ")" + "	 }");
							if (arrParam != null) {
								for (int i = 0; i < arrParam.length; i++) {
									// System.out.println(params[i][0]);
									// System.out.println(params[i][1]);
									// System.out.println(params[i][2]);
									// System.out.println(UtilString.isNil(params[i][3]));
									if (arrParam[i][2].equalsIgnoreCase("in")) {
										UtilSql.setParams(ps, UtilString.isNil(arrParam[i][3]), UtilString.isNil(arrParam[i][1]), i + 1,
												arrParam[i][4]);
										mapRs.put(arrParam[i][0], arrParam[i][3] == null ? "" : arrParam[i][3]);
									} else if (arrParam[i][2].equalsIgnoreCase("out")) {
										if (arrParam[i][1].equalsIgnoreCase("varchar") || arrParam[i][1].equalsIgnoreCase("String")) {
											ps.registerOutParameter(i + 1, Types.VARCHAR);
										} else if (arrParam[i][1].equalsIgnoreCase("number") || arrParam[i][1].equalsIgnoreCase("float")) {
											ps.registerOutParameter(i + 1, Types.NUMERIC);
										} else if (arrParam[i][1].equalsIgnoreCase("integer") || arrParam[i][1].equalsIgnoreCase("int")) {
											ps.registerOutParameter(i + 1, Types.INTEGER);
										} else if (arrParam[i][1].equalsIgnoreCase("date")) {
											ps.registerOutParameter(i + 1, Types.DATE);
										} else if (arrParam[i][1].equalsIgnoreCase("cursor")) {
											ps.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.CURSOR);
										}
									}
								}
							}
							ps.execute();

							if (arrParam != null) {
								for (int i = 0; i < arrParam.length; i++) {
									if (arrParam[i][2].equalsIgnoreCase("out")) {
										if (arrParam[i][1].equalsIgnoreCase("cursor")) {
											ResultSet rs = (ResultSet) ps.getObject(1);
											mapRs.put(arrParam[i][0], rs);
										} else {
											mapRs.put(arrParam[i][0], UtilString.isNil(ps.getObject(i + 1)).toString());
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							UtilSql.close(ps, con);
						}
					}
				});
				return null;
			}
		});
		return mapRs;
	}

	/** *操作类** */
	public Entity saveOrUpdate(Entity entity) {
		if (entity instanceof BaseEntity) {
			((BaseEntity) entity).setCreateDate(new Date());
			((BaseEntity) entity).setVersion("");
			((BaseEntity) entity).setModifyDate(new Date());
			((BaseEntity) entity).setCreateUserId("");
			((BaseEntity) entity).setModifyUserId("");
		}
		entity.setId(UtilString.isNil(entity.getId(),null));
		getSession().saveOrUpdate(entity);
		getSession().flush();
		return entity;
	}
	
	public Entity save(Entity entity) {
		if (entity instanceof SuperEntity) {
			((SuperEntity) entity).setCreateDate(new Date());
			((SuperEntity) entity).setVersion("");
			((SuperEntity) entity).setModifyDate(new Date());
			((SuperEntity) entity).setCreateUserId("");
			((SuperEntity) entity).setModifyUserId("");
		}
		getSession().save(entity);
		getSession().flush();
		return entity;
	}

	public Entity update(Entity entity) {
		if (entity instanceof SuperEntity) {
			((SuperEntity) entity).setModifyDate(new Date());
			((SuperEntity) entity).setModifyUserId("");
		}
		getSession().update(entity);
		getSession().flush();
		return entity;
	}

	public Entity merge(Entity entity) {
		if (entity instanceof SuperEntity) {
			((SuperEntity) entity).setModifyDate(new Date());
			((SuperEntity) entity).setModifyUserId("");
		}
		getSession().merge(entity);
		getSession().flush();
		return entity;
	}

	public boolean delete(String[] ids) {
		for (String id : ids) {
			this.delete(this.get(id));
		}
		return true;
	}

	public boolean delete(Entity entity) {
		getSession().delete(entity);
		getSession().flush();
		return true;

	}

	public boolean delete(List<Entity> listEntity) {
		for (Entity entity : listEntity) {
			delete(entity);
		}
		return true;
	}

	public void executeSql(String sql) {
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	public void executeSql(String sql, Object[] params) {
		Query query = getSession().createSQLQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setString(i, String.valueOf(params[i]));
		}
		query.executeUpdate();
	}

	public void executeSql(String sql, Map<String, Object> map) {
		Query query = getSession().createSQLQuery(sql);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		query.executeUpdate();
	}

	/** *直接获取值** */
	public int getNextVal(String SequenceTable,String tableName) {
		tableName = tableName.toLowerCase();
		String sql = "select nextVal from  " + SequenceTable + " where tableName='" + tableName + "'";
		Query query = this.getSession().createSQLQuery(sql);
		Object val = query.uniqueResult();
		if (val == null) {
			val = 0;
			sql = "insert into " + SequenceTable + "(tableName,preVal,curVal,NextVal)values('" + tableName + "',-1,0,1)";
			this.executeSql(sql);
		} else {
			sql = "update " + SequenceTable + " set preVal=" + (Integer.valueOf(val.toString()) - 1) + ",curVal="
					+ Integer.valueOf(val.toString()) + ",NextVal=" + (Integer.valueOf(val.toString()) + 1) + " where tableName='" + tableName + "'";
			this.executeSql(sql);
		}
		getSession().flush();
		return Integer.valueOf(val.toString());
	}

	public int getPreVal(String SequenceTable,String tableName) {
		tableName = tableName.toLowerCase();
		String sql = "select preVal from  " + SequenceTable + " where tableName='" + tableName + "'";
		Query query = this.getSession().createSQLQuery(sql);
		Object val = query.uniqueResult();
		if (val == null) {
			val = -1;
			sql = "insert into " + SequenceTable + "(tableName,preVal,curVal,NextVal)values('" + tableName + "',-1,0,1)";
			this.executeSql(sql);
		}
		getSession().flush();
		return Integer.valueOf(val.toString());
	}

	public int getCurVal(String SequenceTable,String tableName) {
		tableName = tableName.toLowerCase();
		String sql = "select curVal from  " + SequenceTable + " where tableName='" + tableName + "'";
		Query query = this.getSession().createSQLQuery(sql);
		Object val = query.uniqueResult();
		if (val == null) {
			val = 0;
			sql = "insert into " + SequenceTable + "(tableName,preVal,curVal,NextVal)values('" + tableName + "',-1,0,1)";
			this.executeSql(sql);
		}
		getSession().flush();
		return Integer.valueOf(val.toString());
	}
}
