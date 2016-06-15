package common.util.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import common.util.bean.UtilBean;
import common.util.conver.UtilConver;
import common.util.log.UtilLog;
import common.util.string.UtilString;
import consts.Const;

/**
 * 数据库操作
 * 
 * @author fgq 20120815
 * 
 */
public class UtilSql {
    /**
     * 执行SQL语句操作(更新数据 有参数)
     * 
     * @param sql
     *                sql指令
     * @param prams
     *                参数列表
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public static int executeUpdate(Connection con, String sql, Object... params) throws Exception {
	int result = -1;
	try {
	    PreparedStatement pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    if (params != null)
		setParameters(pstmt, params);
	    result = pstmt.executeUpdate();
	    close(pstmt);
	} catch (Exception e) {
	    result = -1;
	    UtilLog.logError("Sql.executeUpdate:" + sql + "\n", e);
	    throw e;
	}
	return result;
    }

    public static int[] executeUpdate(Connection con, String sql, List<Object[]> paramsList) throws Exception {
	int[] result = null;
	try {
	    PreparedStatement pstmt = null;
	    int cnt = 0;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    for (int i = 0; i < paramsList.size(); i++) {
		if (paramsList.get(i) != null) {
		    setParameters(pstmt, paramsList.get(i));
		}
		pstmt.addBatch();
		cnt = cnt + 1;
		if (cnt % 100 == 0) {
		    pstmt.executeBatch();
		    pstmt.clearBatch();
		}
	    }

	    result = pstmt.executeBatch();
	    pstmt.clearBatch();
	    close(pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeUpdate:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	return result;
    }

    public static int[] executeUpdateByListParam(Connection con, String sql, List<List<Object>> paramsList) throws Exception {
	int[] result = null;
	try {
	    PreparedStatement pstmt = null;
	    int cnt = 0;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    for (int i = 0; i < paramsList.size(); i++) {
		if (paramsList.get(i) != null) {
		    setParameters(pstmt, paramsList.get(i));
		}
		pstmt.addBatch();
		cnt = cnt + 1;
	    }
	    if (cnt > 0)
		result = pstmt.executeBatch();
	    close(pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeUpdate:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	return result;
    }

    public static int[] executeUpdate(Connection con, String sql, List<List<Object>> paramsList, DataField[] dataFiled) throws Exception {
	int[] result = null;
	try {
	    PreparedStatement ps = null;
	    int cnt = 0;
	    ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    for (int i = 0; i < paramsList.size(); i++) {
		if (paramsList.get(i) != null) {
		    List<Object> lineList = paramsList.get(i);
		    for (int j = 0; j < lineList.size(); j++) {
			setParams(ps, dataFiled[j], lineList.get(j).toString());
		    }
		    ps.addBatch();
		}
		cnt = cnt + 1;
	    }
	    if (cnt > 0)
		result = ps.executeBatch();
	    close(ps);
	} catch (Exception e) {
	    result = null;
	    throw e;
	}
	return result;
    }

    // param:ps,参数值，参数类型，ps索引，参数格式
    public static void setParams(PreparedStatement ps, String dataValue, String dataType, int index, String format) throws Exception {
	try {
	    if (dataType.equalsIgnoreCase(Const.VARCHARTYPE) || dataType.equalsIgnoreCase(Const.STRINGTYPE)) {// 字符类型
		if ("".equalsIgnoreCase(dataValue) || null == dataValue)
		    dataValue = "";
		ps.setString(index, dataValue);
	    } else if (dataType.equalsIgnoreCase(Const.NUMBERTYPE) || dataType.equalsIgnoreCase(Const.INTEGERTYPE) || dataType.equalsIgnoreCase(Const.FLOATTYPE)
		    || dataType.equalsIgnoreCase(Const.DOUBLETYPE)) {// 数字类型
		if ("".equalsIgnoreCase(dataValue) || null == dataValue)
		    dataValue = "0";
		if (dataValue.indexOf(",") != -1)
		    dataValue = dataValue.replaceAll(",", "");
		ps.setDouble(index, Double.parseDouble(dataValue));
	    } else if (dataType.equalsIgnoreCase(Const.DATETYPE)) {// 日期类型
		if ("".equalsIgnoreCase(dataValue) || null == dataValue) {// 判断是否为空
		    ps.setDate(index, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(dataValue, format);
		    ps.setDate(index, new java.sql.Date(ud.getTime()));
		}
	    } else if (dataType.equalsIgnoreCase(Const.TIMETYPE)) {// 时间类型
		if ("".equalsIgnoreCase(dataValue) || null == dataValue) {// 判断是否为空
		    ps.setDate(index, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(dataValue, format);
		    ps.setTime(index, new java.sql.Time(ud.getTime()));
		}
	    } else if (dataType.equalsIgnoreCase(Const.TIMESTAMPTYPE)) {// 时间戳类型
		if ("".equalsIgnoreCase(dataValue) || null == dataValue) {// 判断是否为空
		    ps.setDate(index, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(dataValue, format);
		    ps.setTimestamp(index, new java.sql.Timestamp(ud.getTime()));
		}
	    }
	} catch (Exception e) {
	    throw e;
	}
    }

    public static void setParams(PreparedStatement ps, DataField dataField, String value) throws Exception {
	try {
	    // System.out.println(dataField.fieldIndex + " " + value);
	    if (dataField.fieldType.equalsIgnoreCase(Const.VARCHARTYPE) || dataField.fieldType.equalsIgnoreCase(Const.STRINGTYPE)) {// 字符类型
		if ("".equalsIgnoreCase(value) || null == value)
		    value = "";
		ps.setString(dataField.fieldIndex, value);
	    } else if (dataField.fieldType.equalsIgnoreCase(Const.NUMBERTYPE)) {// 数字类型
		if ("".equalsIgnoreCase(value) || null == value)
		    value = "0";
		if (value.indexOf(",") != -1)
		    value = value.replaceAll(",", "");
		ps.setDouble(dataField.fieldIndex, Double.parseDouble(value));
	    } else if (dataField.fieldType.equalsIgnoreCase(Const.DATETYPE)) {// 日期类型
		if ("".equalsIgnoreCase(value) || null == value) {// 判断是否为空
		    ps.setDate(dataField.fieldIndex, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(value, dataField.fieldFormat);
		    ps.setDate(dataField.fieldIndex, new java.sql.Date(ud.getTime()));
		}
	    } else if (dataField.fieldType.equalsIgnoreCase(Const.TIMETYPE)) {// 时间类型
		if ("".equalsIgnoreCase(value) || null == value) {// 判断是否为空
		    ps.setDate(dataField.fieldIndex, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(value, dataField.fieldFormat);
		    ps.setTime(dataField.fieldIndex, new java.sql.Time(ud.getTime()));
		}
	    } else if (dataField.fieldType.equalsIgnoreCase(Const.TIMESTAMPTYPE)) {// 时间戳类型
		if ("".equalsIgnoreCase(value) || null == value) {// 判断是否为空
		    ps.setDate(dataField.fieldIndex, null);
		} else {
		    java.util.Date ud = UtilConver.strToDate(value, dataField.fieldFormat);
		    ps.setTimestamp(dataField.fieldIndex, new java.sql.Timestamp(ud.getTime()));
		}
	    }
	} catch (Exception e) {
	    throw e;
	}
    }

    public static int executeUpdateBatch(Connection con, String[] strSqls) throws Exception {
	int result = -1;
	Statement stmt = null;
	try {
	    if (strSqls == null || strSqls.length == 0)
		return 0;
	    con.setAutoCommit(false);
	    stmt = con.createStatement();

	    for (int i = 0; i < strSqls.length; i++) {
		if (strSqls[i] != null && !strSqls[i].equals(""))
		    stmt.addBatch(strSqls[i]);
	    }
	    stmt.executeBatch();
	    con.commit();
	    con.setAutoCommit(true);

	} catch (Exception e) {
	    try {
		con.setAutoCommit(true);
	    } catch (Exception ce) {
	    }
	    UtilLog.logError("Sql.executeUpdateBatch:", e);
	    result = -1;
	    throw e;
	}
	close(stmt);
	return result;
    }

    /**
     * 执行存储过程，可带出入参 出入参格式:param,type,out/in,value;
     * 
     * @param con
     * @param ProcedureName
     * @param ParamsRule
     * @return
     * @throws Exception
     */
    public static Map<String, String> execProcedureAsMap(Connection con, String ProcedureName, String[][] params) throws Exception {
	Map<String, String> result = new HashMap<String, String>();
	CallableStatement ps = null;
	try {
	    String param = "";
	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    param = param + "?,";
		}
		if (param.length() > 0)
		    param = param.substring(0, param.length() - 1);
	    }

	    ps = con.prepareCall("{ call " + ProcedureName + "(" + param + ")" + "	 }");
	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    // System.out.println(params[i][0]);
		    // System.out.println(params[i][1]);
		    // System.out.println(params[i][2]);
		    // System.out.println(UtilString.isNil(params[i][3]));
		    if (params[i][2].equalsIgnoreCase("in")) {
			setParams(ps, UtilString.isNil(params[i][3]), UtilString.isNil(params[i][1]), i + 1, params[i][4]);
			result.put(params[i][0], params[i][3] == null ? "" : params[i][3]);
		    } else if (params[i][2].equalsIgnoreCase("out")) {
			if (params[i][1].equalsIgnoreCase("varchar") || params[i][1].equalsIgnoreCase("String")) {
			    ps.registerOutParameter(i + 1, Types.VARCHAR);
			} else if (params[i][1].equalsIgnoreCase("number") || params[i][1].equalsIgnoreCase("float")) {
			    ps.registerOutParameter(i + 1, Types.NUMERIC);
			} else if (params[i][1].equalsIgnoreCase("integer") || params[i][1].equalsIgnoreCase("int")) {
			    ps.registerOutParameter(i + 1, Types.INTEGER);
			} else if (params[i][1].equalsIgnoreCase("date")) {
			    ps.registerOutParameter(i + 1, Types.DATE);
			}
		    }
		}
	    }
	    ps.execute();

	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    if (params[i][2].equalsIgnoreCase("out")) {
			result.put(params[i][0], UtilString.isNil(ps.getObject(i + 1)).toString());
		    }
		}
	    }
	} catch (Exception e) {
	    result = null;

	    throw e;
	} finally {
	    close(ps);
	}
	return result;
    }

    public static List<String> execProcedureAsList(Connection con, String ProcedureName, String[][] params) throws Exception {
	List<String> result = new ArrayList<String>();
	CallableStatement ps = null;
	try {
	    String param = "";
	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    param = param + "?,";
		}
		if (param.length() > 0)
		    param = param.substring(0, param.length() - 1);
	    }

	    ps = con.prepareCall("{ call " + ProcedureName + "(" + param + ")" + "	 }");

	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    if (params[i][2].equalsIgnoreCase("in")) {
			setParams(ps, UtilString.isNil(params[i][3]), UtilString.isNil(params[i][1]), i + 1, params[i][4]);
			result.add(params[i][3] == null ? "" : params[i][3]);
		    } else if (params[i][2].equalsIgnoreCase("out")) {
			if (params[i][1].equalsIgnoreCase("varchar") || params[i][1].equalsIgnoreCase("String")) {
			    ps.registerOutParameter(i + 1, Types.VARCHAR);
			} else if (params[i][1].equalsIgnoreCase("number") || params[i][1].equalsIgnoreCase("float")) {
			    ps.registerOutParameter(i + 1, Types.NUMERIC);
			} else if (params[i][1].equalsIgnoreCase("integer") || params[i][1].equalsIgnoreCase("int")) {
			    ps.registerOutParameter(i + 1, Types.INTEGER);
			} else if (params[i][1].equalsIgnoreCase("date")) {
			    ps.registerOutParameter(i + 1, Types.DATE);
			}
		    }
		}
	    }
	    ps.execute();

	    if (params != null) {
		for (int i = 0; i < params.length; i++) {
		    if (params[i][2].equalsIgnoreCase("out")) {
			result.add(ps.getObject(i + 1).toString());
		    }
		}
	    }
	} catch (Exception e) {
	    result = null;
	    throw e;
	} finally {
	    close(ps);
	}
	return result;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> executeSql(Connection con, String sql, Object... params) throws Exception {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList<HashMap<String, String>> result = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		result = populate(rs, true);
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	close(rs, pstmt);
	return result;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static Vector<Vector<String>> executeSqlAsVector(Connection con, String sql, Object... params) throws Exception {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Vector<Vector<String>> result = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		ResultSetMetaData rsm = rs.getMetaData();
		result = new Vector<Vector<String>>();
		while (rs.next()) {
		    Vector<String> rowValue = new Vector<String>();
		    for (int i = 0; i < rsm.getColumnCount(); i++) {
			rowValue.add(rs.getString(i));
		    }
		    result.add(rowValue);
		}
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	close(rs, pstmt);
	return result;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 某字段的数组
     * 
     * @throws Exception
     */
    public static String[] executeSql(Connection con, String sql, String field) {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String[] result = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    rs = pstmt.executeQuery();
	    rs.last();
	    result = new String[rs.getRow()];
	    rs.beforeFirst();
	    int i = 0;
	    while (rs.next()) {
		result[i] = rs.getString(field);
		i += 1;
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql" + sql, e);
	    result = null;
	}
	close(rs, pstmt);
	return result;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                List[0]:Vector<String> Title;List[1]: Vector<Vector<String>>
     *                tableValue
     * @return 数组对象列表
     * @throws Exception
     */
    public static List<Object> executeSqlAsObject(Connection con, String sql, Object... params) throws Exception {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Object> result = new ArrayList<Object>();
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		ResultSetMetaData rsm = rs.getMetaData();
		Vector<String> title = new Vector<String>();

		for (int i = 0; i < rsm.getColumnCount(); i++) {
		    title.add(rsm.getColumnLabel(i + 1));
		}
		result.add(title);
		Vector<Vector<String>> tableValue = new Vector<Vector<String>>();
		while (rs.next()) {
		    Vector<String> rowValue = new Vector<String>();
		    for (int i = 0; i < rsm.getColumnCount(); i++) {
			rowValue.add(rs.getString(i + 1));
		    }
		    tableValue.add(rowValue);
		}
		result.add(tableValue);
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    throw e;
	}
	close(rs, pstmt);
	return result;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static ArrayList<HashMap<Integer, String>> executeSqlByOrder(Connection con, String sql, Object... params) throws Exception {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList<HashMap<Integer, String>> result = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		result = populateByOrder(rs);
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	close(rs, pstmt);
	return result;
    }

    public static Object[] getParamList(ResultSet rs, ResultSetMetaData rsmd, String[] exceptColumn) throws SQLException {
	int len = 0;
	if (exceptColumn != null)
	    len = rsmd.getColumnCount() - exceptColumn.length;
	else
	    len = rsmd.getColumnCount();
	Object[] params = new Object[len];
	int k = 0;
	boolean found;
	for (int i = 0; i < rsmd.getColumnCount(); i++) {
	    found = false;
	    if (exceptColumn != null) {
		for (int j = 0; j < exceptColumn.length; j++) {
		    // System.out.println(exceptColumn[j]
		    // + " "
		    // + rsmd.getColumnName(i + 1)
		    // + " "
		    // + exceptColumn[j].equalsIgnoreCase(rsmd
		    // .getColumnName(i + 1)));
		    if (exceptColumn[j].equalsIgnoreCase(rsmd.getColumnName(i + 1))) {
			found = true;
			break;
		    }
		}
	    }
	    if (!found) {
		params[k] = rs.getObject(i + 1);
		k += 1;
	    }
	}
	return params;
    }

    public static Object[] getParamList(ResultSet rs, ResultSetMetaData rsmd) throws Exception {
	Object[] params = new Object[rsmd.getColumnCount()];
	for (int i = 0; i < rsmd.getColumnCount(); i++) {
	    params[i] = rs.getObject(i + 1);
	}
	return params;
    }

    public static Object[] getParamListByFields(ResultSet rs, ResultSetMetaData rsmd, String compareKey, String compareFields) throws SQLException {
	String[] arrayKey = compareKey.toUpperCase().split(",");
	String[] arrayCompareFields = compareFields.toUpperCase().split(",");
	Object[] params = new Object[arrayKey.length + arrayCompareFields.length];
	// System.out.println(arrayKey.length);
	// System.out.println(arrayCompareFields.length);
	// System.out.println(params.length);
	int j = 0;
	// 先遍历比较字段，因为update语句，比较字段在前面
	for (int k = 0; k < arrayCompareFields.length; k++) {
	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		if (rsmd.getColumnName(i + 1).trim().equalsIgnoreCase(arrayCompareFields[k].trim())) {
		    // System.out.println(arrayCompareFields[k].trim() + " " +
		    // j);
		    params[j] = rs.getObject(arrayCompareFields[k].trim());
		    j += 1;
		}
	    }
	}
	// 后遍历key字段，因为update语句，key字段在后面
	for (int k = 0; k < arrayKey.length; k++) {
	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		if (rsmd.getColumnName(i + 1).trim().equalsIgnoreCase(arrayKey[k].trim())) {
		    // System.out.println(arrayKey[k].trim() + " " + j);
		    params[j] = rs.getObject(arrayKey[k].trim());
		    j += 1;
		}
	    }
	}
	return params;
    }

    /*
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql SQL语句 @param prams 参数列表 @return 数组对象列表 @throws Exception
     */
    public static Map<Object, HashMap<String, String>> executeSql(Connection con, String sql, Object[] params, String key) throws Exception {
	Map<Object, HashMap<String, String>> result = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		result = populate(rs, true, key);
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    result = null;
	    throw e;
	}

	return result;
    }

    /**
     * 转换记录集对象为数组列表对象
     * 
     * @param rs
     *                纪录集合对象
     * @return 数组列表对象
     * @throws Exception
     * @throws Exception
     */
    private static Map<Object, HashMap<String, String>> populate(ResultSet rs, boolean convertUpper, String key) throws Exception {
	Map<Object, HashMap<String, String>> tempMap = new HashMap<Object, HashMap<String, String>>();
	try {
	    // 获取rs 集合信息对象
	    ResultSetMetaData rsmd = rs.getMetaData();
	    // 创建数组列表集合对象

	    HashMap<String, String> tempHash = null;
	    // 填充数组列表集合
	    while (rs.next()) {
		// 创建键值对集合对象
		tempHash = new HashMap<String, String>();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
		    // 遍历每列数据，以键值形式存在对象tempHash中
		    if (convertUpper) {
			tempHash.put(rsmd.getColumnName(i + 1).toUpperCase(), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
			// System.out.println(rsmd.getColumnName(i + 1)
			// .toLowerCase()
			// + " "
			// + UtilString
			// .isNil(
			// rs.getObject(rsmd
			// .getColumnName(i + 1)))
			// .toString());
		    } else {
			tempHash.put(rsmd.getColumnName(i + 1), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
		    }
		}

		tempMap.put(getKeyValue(rs, key), tempHash);
	    }
	} catch (Exception e) {
	    // UtilLog.logError("Sql.populate", e);
	    throw e;
	}
	return tempMap;// 返回填充完毕的数组列表集合对象
    }

    public static Map<String, String> rsToMap(ResultSet rs, ResultSetMetaData rsmd, boolean convertUpper) throws Exception {
	HashMap<String, String> map = new HashMap<String, String>();
	try {

	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		// 遍历每列数据，以键值形式存在对象tempHash中
		if (convertUpper)
		    map.put(rsmd.getColumnName(i + 1).toUpperCase(), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
		else
		    map.put(rsmd.getColumnName(i + 1), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
	    }
	} catch (Exception e) {
	    // UtilLog.logError("Sql.populate", e);
	    throw e;
	}
	return map;
    }

    public static String getKeyValue(ResultSet rs, String key) throws Exception {
	String keyValue = "";
	String skey = "";
	try {
	    String[] arrayKey = key.split(",");
	    for (int i = 0; i < arrayKey.length; i++) {
		// System.out.println(arrayKey[i]);
		skey = arrayKey[i];
		keyValue = keyValue + UtilString.isNil(rs.getObject(skey)).toString() + "|";
	    }
	} catch (Exception e) {
	    UtilLog.logError(skey, e);
	    throw e;
	}
	return keyValue;
    }

    /**
     * 执行分页SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> executeSql(Connection con, String sql, int start, int limit, Object... params) throws Exception {
	ArrayList<HashMap<String, String>> result = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    // 按ExtJs规范，start一般从0开始编号!!
	    sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ") A WHERE ROWNUM <= " + (start + limit) + ")WHERE RN > " + start;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		result = populate(rs, true);
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql:" + sql + "\n", e);
	    result = null;
	    throw e;
	}
	return result;
    }

    /**
     * 获得该sql的总条数
     * 
     * @param sql
     * @param params
     * @return
     */
    private static Long getRowCount(Connection con, String sql, Object[] params) throws Exception {
	Long rowCount = 0l;
	StringBuffer sb;
	if (sql != null && !sql.equals("")) {
	    sql = sql.toUpperCase();
	    int indexOf = sql.indexOf("FROM");
	    if (indexOf > -1) {
		sb = new StringBuffer("SELECT COUNT(*) ");
		sb.append(sql.substring(indexOf));
		rowCount = QueryForSum(con, sql, params).longValue();
	    }
	}
	return rowCount;
    }

    /**
     * 执行分页SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static Map<String, Object> QueryPageForMap(Connection con, String sql, int start, int limit, Object... params) throws Exception {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
	try {
	    resultMap.put("result", result);
	    Long rowCount = getRowCount(con, sql, params);
	    resultMap.put("rowCount", rowCount);

	    if (rowCount == 0)
		return resultMap;

	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    // 按ExtJs规范，start一般从0开始编号!!

	    String tempSql = sql.toUpperCase();
	    int colBegin = tempSql.indexOf("SELECT") + "SELECT".length();

	    int colEnd = tempSql.indexOf("FROM");
	    @SuppressWarnings("unused")
	    String colSql = sql.substring(colBegin, colEnd);

	    sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ") A WHERE ROWNUM <= " + (start + limit) + ")WHERE RN > " + start;

	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		java.sql.ResultSetMetaData rsm = pstmt.getMetaData();
		Integer fieldCount = rsm.getColumnCount();
		String tmpStr = "";
		while (rs.next()) {
		    HashMap<String, Object> ds = new HashMap<String, Object>();
		    for (int i = 0; i < fieldCount; i++) {
			if (rs.getObject(i + 1) != null)
			    tmpStr = UtilString.isNil(rs.getObject(i + 1)).toString().trim();
			else
			    tmpStr = "";
			ds.put(rsm.getColumnName(i + 1), tmpStr);
		    }
		    result.add(ds);
		}
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryPageForMap:" + sql + "\n", e);
	    result = null;
	    throw e;
	}

	return resultMap;
    }

    /**
     * 执行存储过程(查询数据 无参数)
     * 
     * @param procName
     *                存储过程名称
     * @return 数组列表对象
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> executeProcedureQuery(Connection con, String procName) throws Exception {
	ArrayList<HashMap<String, String>> result = null;
	String callStr = "";
	try {
	    CallableStatement cs = null;
	    ResultSet rs = null;
	    callStr = "{call " + procName + "}";// 构造执行存储过程的sql指令
	    cs = con.prepareCall(callStr);
	    rs = cs.executeQuery();
	    result = populate(rs, true);
	    close(rs, cs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeProcedureQuery:" + callStr, e);
	    result = null;
	    throw e;
	}
	return result;
    }

    /**
     * 执行存储过程(查询数据,带参数)返回结果集合
     * 
     * @param procName
     *                存储过程名称
     * @param parameters
     *                参数对象数组
     * @param al
     *                数组列表对象
     * @return 数组列表对象
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> executeProcedureQuery(Connection con, String procName, Object... params) throws Exception {
	ArrayList<HashMap<String, String>> result = null;
	try {
	    CallableStatement cs = null;
	    ResultSet rs = null;
	    int parameterPoint = 0;
	    // 获取存储过程信息列表集合
	    ArrayList<HashMap<String, String>> procedureInfo = getProcedureInfo(con, procName);
	    // 获取存储过程的完全名称
	    String procedureCallName = getProcedureCallName(procName, params.length);
	    // 初始化 存储过程 执行对象
	    cs = con.prepareCall(procedureCallName);
	    // 参数下标变量
	    int index = 0;
	    // 获取 存储过程信息列表集合的 迭代器 对象
	    Iterator<HashMap<String, String>> iter = procedureInfo.iterator();
	    // 遍历存储过程信息列表集合
	    while (iter.hasNext()) {
		HashMap<String, String> hm = iter.next();
		parameterPoint++;
		// 如果参数是输入参数 way = 0
		if (hm.get("WAY").equals("0")) {
		    // 设置参数到cs
		    cs.setObject(parameterPoint, params[index]);
		    // 参数下标+1
		    index++;
		}
	    }
	    // 释放这个对象,做为第二次使用
	    procedureInfo = null;
	    rs = cs.executeQuery();
	    result = populate(rs, true);
	    close(rs, cs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeProcedureQuery:" + procName, e);
	    result = null;
	    throw e;
	}

	return result;
    }

    /**
     * 执行存储过程(更新，查询数据[简单查询、非纪录集]，返回输出参数[非纪录集])
     * 
     * @param procName
     *                存储过程名称
     * @param parameters
     *                参数对象数组
     * @param os
     *                输出参数对象数组
     * @return 输出参数对象数组
     * @throws Exception
     */
    public static Object[] executeProcedureUpdate(Connection con, String procName, Object... params) throws Exception {

	Object[] result = null;
	try {
	    CallableStatement cs = null;
	    // 获取 存储过程 调用全名
	    String fullPCallName = getProcedureCallName(procName, params.length);
	    // 获取存储过程参数信息
	    ArrayList<HashMap<String, String>> p_Call_Info_List = getProcedureInfo(con, procName);
	    // 创建 存储过程 执行对象
	    cs = con.prepareCall(fullPCallName);
	    // 数组下标
	    int index = 1;
	    // 输出参数下标 纪录
	    ArrayList<Integer> outPutIndexList = new ArrayList<Integer>();
	    for (HashMap<String, String> tempHash : p_Call_Info_List) {
		if ("0".equals(tempHash.get("WAY"))) {
		    // 设置输入参数
		    cs.setObject(index, params[index - 1]);
		} else {
		    // 注册输出参数
		    cs.registerOutParameter(index, getDataType(tempHash.get("TYPENAME").toString()));
		    // 纪录输出参数的下标
		    outPutIndexList.add(index);
		}
		index++;
	    }

	    // -------------------- 执行 -----------------
	    if (!cs.execute()) {
		result = new Object[outPutIndexList.size()];
		// 取输 出参数的 返回值
		for (int i = 0; i < outPutIndexList.size(); i++) {
		    result[i] = cs.getObject(outPutIndexList.get(i));
		}
	    }
	    close(cs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeProcedureUpdate:" + procName, e);
	    result = null;
	    throw e;
	}
	return result;
    }

    /**
     * 设置Sql 指令参数
     * 
     * @param p_stmt
     *                PreparedStatement
     * @param pramets
     *                HashMap
     */
    private static PreparedStatement setParameters(PreparedStatement p_stmt, Object[] params) throws Exception {
	// 设置参数
	if (params != null && params.length != 0) {
	    for (int i = 0; i < params.length; i++) {
		// UtilLog.logInfo(params[i].toString());
		p_stmt.setObject(i + 1, params[i]);
	    }
	}
	return p_stmt;
    }

    private static PreparedStatement setParameters(PreparedStatement ps, List<Object> params) throws Exception {
	// 设置参数
	if (params != null && params.size() != 0) {
	    for (int i = 0; i < params.size(); i++) {
		ps.setObject(i + 1, params.get(i));
	    }
	}
	return ps;
    }

    /**
     * 转换记录集对象为数组列表对象
     * 
     * @param rs
     *                纪录集合对象
     * @return 数组列表对象
     * @throws Exception
     */
    private static ArrayList<HashMap<String, String>> populate(ResultSet rs, boolean convertUpper) throws Exception {
	// 获取rs 集合信息对象
	ResultSetMetaData rsmd = rs.getMetaData();
	// 创建数组列表集合对象
	ArrayList<HashMap<String, String>> tempList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> tempHash = null;
	// 填充数组列表集合
	while (rs.next()) {
	    // 创建键值对集合对象
	    tempHash = new HashMap<String, String>();
	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		// 遍历每列数据，以键值形式存在对象tempHash中
		if (convertUpper)
		    tempHash.put(rsmd.getColumnName(i + 1).toUpperCase(), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
		else
		    tempHash.put(rsmd.getColumnName(i + 1), UtilString.isNil(rs.getObject(rsmd.getColumnName(i + 1))).toString());
	    }
	    // 第一个键值对，存储在tempList列表集合对象中
	    tempList.add(tempHash);
	}
	return tempList;// 返回填充完毕的数组列表集合对象
    }

    /**
     * 转换记录集对象为数组列表对象
     * 
     * @param rs
     *                纪录集合对象
     * @return 数组列表对象
     * @throws Exception
     */
    private static ArrayList<HashMap<Integer, String>> populateByOrder(ResultSet rs) throws Exception {
	// 获取rs 集合信息对象
	ResultSetMetaData rsmd = rs.getMetaData();
	// 创建数组列表集合对象
	ArrayList<HashMap<Integer, String>> tempList = new ArrayList<HashMap<Integer, String>>();
	HashMap<Integer, String> tempHash = null;
	// 填充数组列表集合
	while (rs.next()) {
	    // 创建键值对集合对象
	    tempHash = new HashMap<Integer, String>();
	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		tempHash.put(i, UtilString.isNil(rs.getObject(i + 1)).toString());
	    }
	    // 第一个键值对，存储在tempList列表集合对象中
	    tempList.add(tempHash);
	}
	return tempList;// 返回填充完毕的数组列表集合对象
    }

    /**
     * 从数据库得到存储过程信息
     * 
     * @param procName
     *                存储过程名称
     * @return 数组列表对象
     * @throws Exception
     */
    private static ArrayList<HashMap<String, String>> getProcedureInfo(Connection con, String procName) throws Exception {
	// 比较垃圾，仅适用于MS SqlServer库。
	return executeSql(con,
		"select Syscolumns.isoutparam as Way,systypes.name as TypeName from sysobjects,syscolumns,systypes where systypes.xtype=syscolumns.xtype and syscolumns.id=sysobjects.id and sysobjects.name='"
			+ procName + "' order by Syscolumns.isoutparam");
    }

    /**
     * 从数据库得到存储过程参数个数
     * 
     * @param procName
     *                存储过程名称
     * @return 数组列表对象
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static int getParametersCount(Connection con, String procName) throws Exception {
	int returnVal = 0;
	for (HashMap<String, String> tempHas : executeSql(con,
		"select count(*) as RowsCount from sysobjects,syscolumns,systypes where systypes.xtype=syscolumns.xtype and syscolumns.id=sysobjects.id and sysobjects.name='" + procName + "'")) {
	    returnVal = Integer.parseInt(tempHas.get("ROWSCOUNT").toString());
	}
	return returnVal;
    }

    /**
     * 得到调用存储过程的全名
     * 
     * @param procName
     *                存储过程名称
     * @return 调用存储过程的全名
     * @throws Exception
     */
    private static String getProcedureCallName(String procName, int prametCount) throws Exception {
	String procedureCallName = "{call " + procName;
	for (int i = 0; i < prametCount; i++) {
	    if (0 == i) {
		procedureCallName = procedureCallName + "(?";
	    }
	    if (0 != i) {
		procedureCallName = procedureCallName + ",?";
	    }
	}
	procedureCallName = procedureCallName + ")}";
	return procedureCallName;
    }

    /**
     * 得到数据类型的整型值
     * 
     * @param typeName
     *                类型名称
     * @return 数据类型的整型值
     */
    private static int getDataType(String typeName) {
	if (typeName.equals("varchar"))
	    return Types.VARCHAR;
	if (typeName.equals("int"))
	    return Types.INTEGER;
	if (typeName.equals("bit"))
	    return Types.BIT;
	if (typeName.equals("float"))
	    return Types.FLOAT;
	return 0;
    }

    // 将查询结果转换到一个PO对象列表中
    public static <T extends Object> List<T> QueryForObjectList(Connection con, Class<T> clazz, String sql, Object... params) throws Exception {
	List<T> result = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    String[] columnsName = getColumnName(rsmd);
	    // Class clazz = entity.getClass();
	    T bean = null;

	    Field[] fields = clazz.getDeclaredFields();
	    Map<String, Field> fieldMap = new HashMap<String, Field>();
	    for (Field field : fields) {
		fieldMap.put(field.getName().toLowerCase(), field);
	    }
	    result = new ArrayList<T>();
	    while (rs.next()) {
		bean = clazz.newInstance();
		for (int i = 0; i < columnsName.length; i++) {
		    if (fieldMap.containsKey(columnsName[i].toLowerCase())) {

			Field field = fieldMap.get(columnsName[i].toLowerCase());
			String fieldName = field.getName();
			Method m = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
			if (field.getType().getName().equals("java.util.Date")) {
			    java.sql.Timestamp value = rs.getTimestamp(columnsName[i]);
			    if (value != null && m != null) {
				m.invoke(bean, new java.util.Date(value.getTime()));
			    }
			} else {
			    String value = UtilString.isNil(rs.getObject(columnsName[i])).toString();
			    if (value != null && m != null) {
				m.invoke(bean, UtilBean.createObject(field.getType(), value));
			    }
			}
		    }
		}
		result.add(bean);
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForObjectList::" + sql, e);
	    result = null;
	    throw e;
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForSimpleObjectList(Connection con, String sql, Object... params) throws Exception {
	List<T> result = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    result = new ArrayList<T>();
	    while (rs.next()) {
		result.add((T) rs.getObject(1));
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    result = null;
	    UtilLog.logError("Sql.queryForSimpleObjectList:" + sql, e);
	    throw e;
	}
	return result;
    }

    // 将分页查询结果转换到一个PO对象列表中
    @SuppressWarnings("unchecked")
    public static <T extends Object> List<T> QueryForObjectList(Connection con, Class<T> clazz, String sql, int start, int limit, Object... params) throws Exception {
	List<T> result = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ") A WHERE ROWNUM <= " + (start + limit) + ")WHERE RN > " + start;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    String[] columnsName = getColumnName(rsmd);
	    // Class clazz = entity.getClass();
	    Object bean = null;

	    Field[] fields = clazz.getDeclaredFields();
	    Map<String, Field> fieldMap = new HashMap<String, Field>();
	    for (Field field : fields) {
		fieldMap.put(field.getName().toLowerCase(), field);
	    }

	    result = new ArrayList<T>();
	    while (rs.next()) {
		bean = clazz.newInstance();
		for (int i = 0; i < columnsName.length; i++) {
		    if (fieldMap.containsKey(columnsName[i].toLowerCase())) {

			Field field = fieldMap.get(columnsName[i].toLowerCase());
			String fieldName = field.getName();
			Method m = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
			if (field.getType().getName().equals("java.util.Date")) {
			    java.sql.Timestamp value = rs.getTimestamp(columnsName[i]);
			    if (value != null && m != null) {
				m.invoke(bean, new java.util.Date(value.getTime()));
			    }
			} else {
			    String value = UtilString.isNil(rs.getObject(columnsName[i])).toString();
			    if (value != null && m != null) {
				m.invoke(bean, UtilBean.createObject(field.getType(), value));
			    }
			}
		    }
		}
		result.add((T) bean);
	    }
	    close(rs, pstmt);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForObjectList:" + sql, e);
	    result = null;
	    throw e;
	}
	return result;
    }

    private static String[] getColumnName(ResultSetMetaData rsmd) throws SQLException {
	String[] columnName = null;
	try {
	    columnName = new String[rsmd.getColumnCount()];
	    for (int i = 0; i < rsmd.getColumnCount(); i++) {
		columnName[i] = rsmd.getColumnName(i + 1);
	    }
	} catch (SQLException e) {
	    // UtilLog.logError("Sql.getColumnName:", e);
	    throw e;
	}
	return columnName;
    }

    public static <T> T QueryForObject(Connection con, Class<T> clazz, String sql, Object... params) throws Exception {
	T bean = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    String[] columnsName = getColumnName(rsmd);
	    // Class clazz = entity.getClass();

	    Field[] fields = clazz.getDeclaredFields();
	    Map<String, Field> fieldMap = new HashMap<String, Field>();
	    for (Field field : fields) {
		fieldMap.put(field.getName().toLowerCase(), field);
	    }

	    // 只取第一条
	    if (rs.next()) {
		bean = clazz.newInstance();
		for (int i = 0; i < columnsName.length; i++) {
		    if (fieldMap.containsKey(columnsName[i].toLowerCase())) {

			Field field = fieldMap.get(columnsName[i].toLowerCase());
			String fieldName = field.getName();
			Method m = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
			if (field.getType().getName().equals("java.util.Date")) {
			    java.sql.Timestamp value = rs.getTimestamp(columnsName[i]);
			    if (value != null && m != null) {
				m.invoke(bean, new java.util.Date(value.getTime()));
			    }
			} else {
			    String value = UtilString.isNil(rs.getObject(columnsName[i])).toString();
			    if (value != null && m != null) {
				m.invoke(bean, UtilBean.createObject(field.getType(), value));
			    }
			}
		    }

		}
	    }
	    close(pstmt, rs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForObject:" + sql, e);
	    bean = null;
	    throw e;
	}
	return bean;
    }

    public static <T> T QueryForObject(Class<T> clazz, Connection con, String sql, Object... params) throws Exception {
	T bean = null;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    String[] columnsName = getColumnName(rsmd);
	    // Class clazz = entity.getClass();

	    Field[] fields = clazz.getDeclaredFields();
	    Map<String, Field> fieldMap = new HashMap<String, Field>();
	    for (Field field : fields) {
		fieldMap.put(field.getName().toLowerCase(), field);
	    }

	    // 只取第一条
	    if (rs.next()) {
		bean = clazz.newInstance();
		for (int i = 0; i < columnsName.length; i++) {
		    if (fieldMap.containsKey(columnsName[i].toLowerCase())) {

			Field field = fieldMap.get(columnsName[i].toLowerCase());
			String fieldName = field.getName();
			Method m = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
			if (field.getType().getName().equals("java.util.Date")) {
			    java.sql.Timestamp value = rs.getTimestamp(columnsName[i]);
			    if (value != null && m != null) {
				m.invoke(bean, new java.util.Date(value.getTime()));
			    }
			} else {
			    String value = UtilString.isNil(rs.getObject(columnsName[i])).toString();
			    if (value != null && m != null) {
				m.invoke(bean, UtilBean.createObject(field.getType(), value));
			    }
			}
		    }

		}
	    }
	    close(pstmt, rs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForObject:" + sql, e);
	    bean = null;
	    throw e;
	}
	return bean;
    }

    public static long queryForCount(Connection con, String sql, Object... params) throws Exception {
	long result = -1;
	try {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();

	    // 只取第一条
	    if (rs.next()) {
		result = rs.getLong(1);
	    }
	    close(pstmt, rs);
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForCount:" + sql, e);
	    result = -1;
	    throw e;
	}
	return result;
    }

    public static boolean isExist(Connection con, String sql, Object... params) throws Exception {
	if (queryForCount(con, sql, params) > 0)
	    return true;
	else
	    return false;
    }

    public static Float QueryForSum(Connection con, String sql, Object... params) throws Exception {
	Float result = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();

	    // 只取第一条
	    if (rs.next()) {
		result = rs.getFloat(1);
	    }
	} catch (Exception e) {
	    UtilLog.logError("QueryForSum:" + sql, e);
	    result = null;
	    throw e;
	} finally {
	    close(pstmt, rs);
	}
	return result;
    }

    public static String QueryForMax(Connection con, String sql, Object... params) throws Exception {
	String result = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();

	    // 只取第一条
	    if (rs.next()) {
		result = UtilString.isNil(rs.getObject(1)).toString();
	    }
	    // 正常查询没有结果时说明没有记录，应置为0
	    if (result == null || "".equals(result))
		result = "0";

	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryForMax:" + sql, e);
	    result = null;
	    throw e;
	} finally {
	    close(pstmt, rs);
	}
	return result;
    }

    public static <T> T Rs2Object(Class<T> clazz, ResultSet rs) throws Exception {
	T bean = null;
	try {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    String[] columnsName = getColumnName(rsmd);
	    Field[] fields = clazz.getDeclaredFields();
	    bean = clazz.newInstance();
	    for (int i = 0; i < columnsName.length; i++) {
		for (int k = 0; k < fields.length; k++) {
		    if (fields[k].getName().toUpperCase().equals(columnsName[i].toUpperCase())) {
			if (fields[k].getType().getName().equals("java.util.Date")) {
			    java.sql.Timestamp value = rs.getTimestamp(columnsName[i]);
			    if (value != null)
				fields[k].set(bean, new java.util.Date(value.getTime()));
			} else {
			    String value = UtilString.isNil(rs.getObject(columnsName[i])).toString();
			    if (value != null)
				fields[k].set(bean, UtilBean.createObject(fields[k].getType(), value));
			}
		    }
		}
	    }
	} catch (Exception e) {
	    // UtilLog.logError("Sql.Rs2Object:", e);
	    throw e;
	}
	return bean;

    }

    /*
     * 返回查询结果的全部记录
     */
    public static List<HashMap<String, Object>> QueryM(Connection con, String sql) {
	List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
	Statement stmt = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	int j = 0;
	String tmpStr;
	HashMap<String, Object> ds;
	try {

	    if (con == null)
		return null;
	    stmt = con.createStatement();
	    tmpRs = stmt.executeQuery(sql);
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();

	    if (tmpRs == null) {
		return null;
	    }
	    while (tmpRs.next()) {
		ds = new HashMap<String, Object>();
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1), tmpStr);
		}

		l.add(ds);
	    }

	    if (j == 1)
		return l;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryM:" + sql, e);
	} finally {
	    close(tmpRs, stmt);
	}
	return null;
    }

    /*
     * 返回查询结果的全部记录
     */
    public static List<HashMap<String, Object>> QueryM(Connection con, String sql, Object... params) {
	List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
	PreparedStatement ps = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	int j = 0;
	String tmpStr;
	HashMap<String, Object> ds;
	try {
	    if (con == null)
		return null;
	    ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(ps, params);
	    tmpRs = ps.executeQuery();
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();

	    if (tmpRs == null) {
		return null;
	    }
	    while (tmpRs.next()) {
		ds = new HashMap<String, Object>();
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1), tmpStr);
		}
		l.add(ds);
	    }
	    if (j == 1)
		return l;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryM:" + sql, e);
	} finally {
	    close(tmpRs, tmpRs);
	}
	return null;
    }

    // public static String genJSONStr(Connection con, String nameStr, String
    // sql) {
    // Statement stmt = null;
    // ResultSet tmpRs = null;
    // ResultSetMetaData mm;
    // int total;
    //
    // try {
    // stmt = con.createStatement();
    // tmpRs = stmt.executeQuery(sql);
    // mm = tmpRs.getMetaData();
    //
    // int fieldType;
    // int fieldCount = mm.getColumnCount();
    // JSONArray ja = new JSONArray();
    //
    // total = 0;
    // while (tmpRs.next()) {
    // JSONObject jo = new JSONObject();
    // for (int i = 0; i < fieldCount; i++) {
    // fieldType = mm.getColumnType(i + 1);
    //
    // if (fieldType == java.sql.Types.BIGINT || fieldType ==
    // java.sql.Types.INTEGER)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getInt(i + 1));
    // else if (fieldType == java.sql.Types.FLOAT)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getDouble(i + 1));
    // else
    // jo.put(mm.getColumnName(i + 1).toLowerCase(),
    // UtilString.isNil(tmpRs.getObject(i + 1)).toString());
    // }
    // ja.add(jo);
    // total++;
    // }
    // JSONObject jop = new JSONObject();
    // jop.put("totalCount", String.valueOf(total));
    // jop.put("name", nameStr);
    // jop.put("data", ja);
    // return jop.toString();
    // } catch (Exception e) {
    // return "";
    // } finally {
    // close(tmpRs, stmt);
    // }
    // }

    // public static JSONObject genJSON(Connection con, String nameStr, String
    // sql) {
    // Statement stmt = null;
    // ResultSet tmpRs = null;
    // ResultSetMetaData mm;
    // int total;
    // try {
    // stmt = con.createStatement();
    // tmpRs = stmt.executeQuery(sql);
    // mm = tmpRs.getMetaData();
    // int fieldType;
    // int fieldCount = mm.getColumnCount();
    // JSONArray ja = new JSONArray();
    // total = 0;
    // while (tmpRs.next()) {
    // JSONObject jo = new JSONObject();
    // for (int i = 0; i < fieldCount; i++) {
    // fieldType = mm.getColumnType(i + 1);
    //
    // if (fieldType == java.sql.Types.BIGINT || fieldType ==
    // java.sql.Types.INTEGER)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getInt(i + 1));
    // else if (fieldType == java.sql.Types.FLOAT)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getDouble(i + 1));
    // else
    // jo.put(mm.getColumnName(i + 1).toLowerCase(),
    // UtilString.isNil(tmpRs.getObject(i + 1)).toString());
    // }
    //
    // ja.add(jo);
    // total++;
    // }
    //
    // if (total > 0) {
    // JSONObject jop = new JSONObject();
    // jop.put("totalCount", String.valueOf(total));
    // jop.put("name", nameStr);
    // jop.put("data", ja);
    // return jop;
    // } else
    // return null;
    // } catch (Exception e) {
    // UtilLog.logError("Sql.genJSON:", e);
    // return null;
    // } finally {
    // close(tmpRs, stmt);
    // }
    // }

    /*
     * 返回查询结果的第1条记录
     */
    public static HashMap<String, String> QueryA(Connection con, String sql) {
	Statement stmt = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	int j = 0;
	String tmpStr;
	HashMap<String, String> ds = new HashMap<String, String>();
	try {
	    if (con == null)
		return null;
	    stmt = con.createStatement();
	    tmpRs = stmt.executeQuery(sql);
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();

	    if (tmpRs == null) {
		return null;
	    }
	    while (tmpRs.next()) {
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1), tmpStr);
		}
		break;
	    }
	    if (j == 1)
		return ds;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryA" + sql, e);
	} finally {
	    close(tmpRs, stmt);
	}
	return null;
    }

    /**
     * 
     * @param 返回第一条记录，全部以字符表达，sqlStr中不能带BLOG/CLOG
     * @return
     */
    public static HashMap<String, String> QueryA(Connection con, String sql, Object... params) {
	PreparedStatement stmt = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	String tmpStr;
	int j = 0;

	HashMap<String, String> ds = new HashMap<String, String>();
	try {
	    stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(stmt, params);
	    tmpRs = stmt.executeQuery();
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();
	    while (tmpRs.next()) {
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1).toUpperCase(), tmpStr);
		}
		break;
	    }

	    if (j == 1)
		return ds;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.QueryA/2" + sql, e);
	} finally {
	    close(tmpRs, stmt);
	}
	return null;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     * 
     * @param sql
     *                SQL语句
     * @param prams
     *                参数列表
     * @return 数组对象列表
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> executeSql(Connection con, String sql, boolean convertUpper, Object... params) {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList<HashMap<String, String>> result = null;
	try {
	    pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    setParameters(pstmt, params);
	    rs = pstmt.executeQuery();
	    if (null != rs) {
		result = populate(rs, convertUpper);
	    }
	} catch (Exception e) {
	    UtilLog.logError("Sql.executeSql" + sql, e);
	    result = null;
	}
	close(rs, pstmt);
	return result;
    }

    // SQL
    /*
     * @param 返回指定Seqence的新值
     */
    public static String querySeq(Connection con, String seqName) {
	Statement stmt = null;
	ResultSet tmpRs = null;
	String ss = null;
	try {
	    stmt = con.createStatement();
	    tmpRs = stmt.executeQuery("select " + seqName + ".nextval from dual");

	    while (tmpRs.next()) {
		ss = UtilString.isNil(tmpRs.getObject(1)).toString().trim();
		break;
	    }

	    return ss;
	} catch (Exception e) {
	    UtilLog.logError("Sql.querySeq: " + seqName, e);
	    return ss;
	} finally {
	    close(tmpRs, stmt);
	}
    }

    /*
     * 返回查询结果的全部记录
     */
    public static List<HashMap<String, String>> queryM(Connection con, String sql) {
	List<HashMap<String, String>> rtnList = new ArrayList<HashMap<String, String>>();
	Statement stmt = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	int j = 0;
	String tmpStr;
	HashMap<String, String> ds;
	try {
	    if (con == null)
		return null;
	    stmt = con.createStatement();
	    tmpRs = stmt.executeQuery(sql);
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();

	    if (tmpRs == null) {
		return null;
	    }
	    while (tmpRs.next()) {
		ds = new HashMap<String, String>();
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1), tmpStr);
		}

		rtnList.add(ds);
	    }

	    if (j == 1)
		return rtnList;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.queryM:" + sql + "\n", e);
	    return null;
	} finally {
	    close(tmpRs, stmt);
	}
    }

    /*
     * 返回查询结果的全部记录，并转为Map
     */
    public static HashMap<String, HashMap<String, String>> queryM_Ex(Connection con, String keyField, String sql) {
	HashMap<String, HashMap<String, String>> returnM = new HashMap<String, HashMap<String, String>>();

	Statement stmt = null;
	ResultSet tmpRs = null;
	ResultSetMetaData mm;
	int fieldCount;
	int i = 0;
	int j = 0;
	String tmpStr;

	HashMap<String, String> ds;

	try {
	    stmt = con.createStatement();
	    tmpRs = stmt.executeQuery(sql);
	    mm = tmpRs.getMetaData();
	    fieldCount = mm.getColumnCount();

	    if (tmpRs == null) {
		return null;
	    }
	    while (tmpRs.next()) {
		ds = new HashMap<String, String>();
		j = 1;
		for (i = 0; i < fieldCount; i++) {
		    if (tmpRs.getObject(i + 1) != null)
			tmpStr = UtilString.isNil(tmpRs.getObject(i + 1)).toString().trim();
		    else
			tmpStr = "";

		    ds.put(mm.getColumnName(i + 1), tmpStr);
		}
		returnM.put(ds.get(keyField), ds);
	    }

	    if (j == 1)
		return returnM;
	    else
		return null;
	} catch (Exception e) {
	    UtilLog.logError("Sql.queryM_Ex:" + sql + "\n", e);
	    return null;
	} finally {
	    close(tmpRs, stmt);
	}
    }

    public static boolean exectueSQL(Connection con, String sql) {
	Statement stmt = null;
	try {
	    stmt = con.createStatement();
	    stmt.executeUpdate(sql);
	    return true;
	} catch (Exception e) {
	    UtilLog.logError("Sql.exectueSQL:" + sql + "\n", e);
	    return false;
	} finally {
	    close(stmt);
	}
    }

    public static int exectueSQLBatch(Connection con, String[] sql) {
	Statement stmt = null;
	if (sql == null || sql.length == 0)
	    return 0;
	try {
	    if (con == null)
		return -1;

	    con.setAutoCommit(false);
	    stmt = con.createStatement();

	    for (int i = 0; i < sql.length; i++) {
		if (sql[i] != null && !sql[i].equals(""))
		    stmt.addBatch(sql[i]);
	    }

	    stmt.executeBatch();
	    con.commit();

	    return 1;
	} catch (Exception e) {
	    UtilLog.logError("Sql.exectueSQLBatch:" + sql + "\n", e);
	    return -1;
	} finally {
	    try {
		con.setAutoCommit(true);
	    } catch (SQLException e) {
		// UtilLog.logError("Sql.exectueSQLBatch:con.setAutoCommit(true)"+
		// "\n", e);
	    }
	    close(stmt);
	}
    }

    public static int exectueSQLBatch(Connection con, ArrayList<String> sql) {
	Statement stmt = null;

	if (sql == null || sql.size() == 0)
	    return 0;
	try {
	    if (con == null)
		return -1;

	    con.setAutoCommit(false);
	    stmt = con.createStatement();

	    int commitCount = 0;
	    for (int i = 0; i < sql.size(); i++) {
		// System.out.println(sql.get(i));
		if (sql.get(i) != null && !sql.get(i).equals(""))
		    stmt.addBatch(sql.get(i));
		if (i == 1000) {
		    stmt.executeBatch();
		    commitCount += 1000;
		}
	    }
	    commitCount += stmt.executeBatch().length;
	    // System.out.println(sql.size());
	    // System.out.println("commitcount : " + commitCount);

	    if (commitCount == sql.size()) {
		con.commit();
		return 1;
	    } else {
		con.rollback();
		return -1;
	    }

	} catch (Exception e) {
	    UtilLog.logError("Sql.exectueSQLBatch:" + sql + "\n", e);
	    return -1;
	} finally {
	    close(stmt);
	    if (con != null) {
		try {
		    con.setAutoCommit(true);
		} catch (SQLException e) {
		}
	    }
	}
    }

    //
    // public static String xJSONStr(Connection con, String nameStr, String
    // sql) {
    // Statement stmt = null;
    // ResultSet tmpRs = null;
    // ResultSetMetaData mm;
    // int total;
    //
    // try {
    // stmt = con.createStatement();
    // tmpRs = stmt.executeQuery(sql);
    // mm = tmpRs.getMetaData();
    //
    // int fieldType;
    // int fieldCount = mm.getColumnCount();
    // JSONArray ja = new JSONArray();
    //
    // total = 0;
    // while (tmpRs.next()) {
    // JSONObject jo = new JSONObject();
    // for (int i = 0; i < fieldCount; i++) {
    // fieldType = mm.getColumnType(i + 1);
    //
    // if (fieldType == java.sql.Types.BIGINT || fieldType ==
    // java.sql.Types.INTEGER)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getInt(i + 1));
    // else if (fieldType == java.sql.Types.FLOAT)
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getDouble(i + 1));
    // else
    // jo.put(mm.getColumnName(i + 1).toLowerCase(), tmpRs.getObject(i + 1));
    // }
    // ja.add(jo);
    // total++;
    // }
    // JSONObject jop = new JSONObject();
    // jop.put("totalCount", String.valueOf(total));
    // jop.put("name", nameStr);
    // jop.put("data", ja);
    //
    // return jop.toString();
    // } catch (Exception e) {
    // UtilLog.logError("Sql.xJSONStr" + sql + "\n", e);
    // try {
    // con.rollback();
    // } catch (Exception ex) {
    // }
    // return "";
    // } finally {
    // close(tmpRs, stmt);
    // }
    // }

    public static void close(Object... objects) {
	if (objects.length > 0) {
	    for (Object o : objects) {
		if (null != o) {
		    try {
			if (o instanceof Connection)
			    ((Connection) o).close();
			else if (o instanceof Statement)
			    ((Statement) o).close();
			else if (o instanceof ResultSet)
			    ((ResultSet) o).close();
			else
			    o = null;
		    } catch (SQLException e) {
			o = null;
			// UtilLog.logError("关闭对象错误:", e);
		    }
		}
	    }
	}
    }

    public static class DataField {
	public String fieldName;// 字段名称
	public int fieldIndex; // 字段索引
	public String fieldType; // 字段类型
	public String fieldFormat;// 字段格式
	public int fieldColumnIndex;// 文件中的该字段对应的列号

    }

    /**
     * 添加数据
     * 
     * @param tableName
     *                表名
     */
    public static int insert(Connection con, String tableName, Map<String, Object> params, String sequenceName, String sequnceField) throws SQLException {
	int num = -1;
	if (params != null) {
	    StringBuffer sql = new StringBuffer("insert into " + tableName + "(");
	    if (sequenceName != null) {
		sql = new StringBuffer("insert into " + tableName + "(" + sequnceField + ",");
	    } else {
		sql = new StringBuffer("insert into " + tableName + "(");
	    }
	    for (Map.Entry<String, Object> m : params.entrySet()) {
		sql.append(m.getKey() + ",");
	    }
	    sql.replace(sql.length() - 1, sql.length(), "");
	    if (sequenceName != null) {
		sql.append(")values(" + sequenceName + ",'");
	    } else {
		sql.append(")values('");
	    }
	    for (Map.Entry<String, Object> m : params.entrySet()) {
		sql.append(m.getValue() + "','");
	    }
	    sql.replace(sql.length() - 2, sql.length(), "");
	    sql.append(")");
	    PreparedStatement ps = null;
	    try {
		con.setAutoCommit(false);
		ps = con.prepareStatement(sql.toString());
		num = ps.executeUpdate();
		con.commit();
	    } catch (Exception e) {
		e.printStackTrace();
		if (con != null)
		    con.rollback();
	    } finally {
		close(ps);
	    }
	}
	return num;
    }

    /**
     * 更新 param tableName 表名
     */
    public static int update(Connection con, String tableName, Map<String, Object> params, String byWhat, String otherCondition) throws SQLException {
	int num = -1;
	PreparedStatement ps = null;
	ResultSet rs = null;
	if (params != null) {
	    try {
		con.setAutoCommit(false);
		String sql = "select *from " + tableName + " where " + byWhat + "='" + params.get(byWhat) + "'";
		if (otherCondition != null) {
		    sql += otherCondition;
		}
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		rs.last();
		if (rs.getRow() <= 0)
		    return num;
		StringBuffer sb = new StringBuffer("update " + tableName + " set ");
		for (Map.Entry<String, Object> m : params.entrySet()) {
		    sb.append(m.getKey() + "='" + m.getValue() + "',");
		}
		sb.replace(sql.length() - 1, sql.length(), "");
		if (!"".equals(params.get(byWhat)) && params.get(byWhat) != null) {
		    sb.append("where " + byWhat + " ='" + params.get(byWhat) + "'");
		}
		if (otherCondition != null)
		    sb.append(otherCondition);
		ps = con.prepareStatement(sb.toString());
		num = ps.executeUpdate();
		con.commit();
	    } catch (Exception e) {
		e.printStackTrace();
		if (con != null)
		    con.rollback();
	    } finally {
		close(rs, ps);
	    }
	}
	return num;
    }

    /**
     * 删除
     * 
     * @param tableName
     *                表名
     */
    public static int delete(Connection con, String tableName, Map<String, Object> mapCondition, String operationLogic) throws SQLException {
	int num = -1;
	if (mapCondition != null) {
	    StringBuffer sql = new StringBuffer("delete from " + tableName);
	    sql.append(" where  ");
	    for (Map.Entry<String, Object> m : mapCondition.entrySet()) {
		sql.append(m.getKey() + "=" + "'" + m.getValue() + "' " + operationLogic + " ");
	    }
	    sql.replace(sql.length() - 5, sql.length(), "");
	    PreparedStatement ps = null;
	    try {
		con.setAutoCommit(false);
		ps = con.prepareStatement(sql.toString());
		num = ps.executeUpdate();
		con.commit();
	    } catch (Exception e) {
		e.printStackTrace();
		if (con != null)
		    con.rollback();
	    } finally {
		close(ps);
	    }
	}
	return num;
    }

}
