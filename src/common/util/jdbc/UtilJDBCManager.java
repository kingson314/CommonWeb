//package common.util.jdbc;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Properties;
//import java.util.concurrent.ConcurrentHashMap;
//import common.util.log.UtilLog;
//import common.util.security.UtilCrypt;
//
//import consts.Const;
//
///**
// * 数据库连接池
// * 
// * @author fgq 20120815
// * 
// */
//public class UtilJDBCManager {
//	public static ConcurrentHashMap<String, ComboPooledDataSource> MapConPool = new ConcurrentHashMap<String, ComboPooledDataSource>();
//	public static ConcurrentHashMap<String, Connection> MapCon = new ConcurrentHashMap<String, Connection>();
//
//	public static Connection getConnection(DbConnection dbcon) {
//		// Odbc连接方式:不存放在c3p0连接池(不稳定)
//		if ("Access".equalsIgnoreCase(dbcon.getDbType())
//				|| "DBF".equalsIgnoreCase(dbcon.getDbType())
//				|| "MDB".equalsIgnoreCase(dbcon.getDbType())) {
//			return getCommonCon(dbcon);
//		} else {
//			return getPoolCon(dbcon);
//		}
//	}
//
//	public static Connection getConnection(String dbName) {
//		DbConnection dbcon = DbConnectionDao.getInstance().getMapDbConn(dbName);
//		return getConnection(dbcon);
//	}
//
//	// c3p0连接池连接
//	private synchronized static Connection getPoolCon(DbConnection dbcon) {
//		Connection con = null;
//		try {
//			ComboPooledDataSource cpds = MapConPool.get(dbcon.getDbName());
//			if (cpds == null) {
//				cpds = getConnPool(dbcon);
//				MapConPool.put(dbcon.getDbName(), cpds);
//			}
//			con = cpds.getConnection();
//		} catch (SQLException e) {
//			con = null;
//			UtilLog.logError("getConnection[" + dbcon.getDbName() + "]:", e);
//		}
//		return con;
//	}
//
//	// 普通jdbc方式连接
//	private static Connection getCommonCon(DbConnection dbcon) {
//		Connection con = MapCon.get(dbcon.getDbName());
//		try {
//			if (con != null && !con.isClosed())
//				return con;
//		} catch (SQLException e) {
//			UtilLog.logError("判断数据库连接状态错误:", e);
//		}
//		Properties prop = new Properties();
//		try {
//			prop.put("charSet", "gbk"); // 解决中文乱码
//			Class.forName(dbcon.getDbClassName());
//			DriverManager.setLoginTimeout(60);
//			String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="
//					+ dbcon.getDbCon();
//			con = DriverManager.getConnection(url, prop);
//			// con = DriverManager.getConnection(dbcon.getDbCon(),
//			// dbcon.getDbUser(),
//			// UtilCrypt.getInstance().decryptAES(dbcon.getDbPassword(),
//			// UtilCrypt.key));
//		} catch (Exception e) {
//			String url="jdbc:odbc:"+ Const.DefaultDir.substring(Const.DefaultDir.lastIndexOf("\\")+1);
//			try {
//				con = DriverManager.getConnection(url, prop);
//			} catch (SQLException e1) {
//				con = null;
//				UtilLog.logError("数据库连接错误:", e);
//			}
//		}
//		MapCon.put(dbcon.getDbName(), con);
//		return con;
//	}
//
//	public static void setConnPool(DbConnection dbconn) {
//		ComboPooledDataSource cpds = MapConPool.get(dbconn.getDbName());
//		if (cpds == null) {
//			cpds = getConnPool(dbconn);
//			MapConPool.put(dbconn.getDbName(), cpds);
//		}
//	}
//
//	// 创建数据库连接池
//	private static ComboPooledDataSource getConnPool(DbConnection dbconn) {
//		ComboPooledDataSource cpds = new ComboPooledDataSource();
//		try {
//			// 设置驱动
//			cpds.setDriverClass(dbconn.getDbClassName());
//			// 设置URL
//			cpds.setJdbcUrl(dbconn.getDbCon());
//			// 设置用户名
//			cpds.setUser(dbconn.getDbUser());
//			// 设置解密后密码
//			String psw = UtilCrypt.getInstance().decryptAES(
//					dbconn.getDbPassword(), UtilCrypt.key);
//			// System.out.println(psw);
//			cpds.setPassword(psw);
//			// 当连接池中的连接用完时,c3p0一次性创建新的连接数目
//			cpds.setAcquireIncrement(3);
//			// 定义从数据库获取新的连接失败后重复尝试获取的次数，默认为30
//			cpds.setAcquireRetryAttempts(30);
//			// 两次连接中间时间默认误1000毫秒
//			cpds.setAcquireRetryDelay(1000);
//			// 连接关闭时默认将所有为体检的操作回滚，默认为false
//			cpds.setAutoCommitOnClose(false);
//			// 获取连接失败将会引起所有等待获取连接的线程异常，但数据源仍有效的保留，并在下次
//			// 调用getConnection()的时候继续尝试获取连接，如果设为true,那么尝试获取
//			// 连接失败后该数据源将声明已经断开并永久关闭，默认为false
//			cpds.setBreakAfterAcquireFailure(false);
//			// 当连接池用完时客户端调用getConnection()后等待获取新的连接的时间，超时
//			// 后将抛出SQLException,如果设为0则无限期等待，单位为毫秒，默认为0
//			cpds.setCheckoutTimeout(0);
//			// 隔多少秒检查所有连接池中的空闲连接，默认为0不检查;
//			cpds.setIdleConnectionTestPeriod(0);
//			// 初始化时创建的连接数，应在minPoolSize与maxPoolSize之间取值。默认为3
//			cpds.setInitialPoolSize(10);
//			// 最大空闲时间，超出空闲时间的连接将被丢弃，为0或负数则永不丢弃。默认为0
//			cpds.setMaxIdleTime(0);
//			// 连接池中保留的最大连接数据。默认为15
//			cpds.setMaxPoolSize(100);
//			// JDBC的标准参数，用以控制数据源内加载的preparedStatement数据。
//			// 但由于预缓存的Statement属于单个Connection而不是整个连接池，
//			// 所以设置这个参数需要考虑多个方面的因素，如果maxStatements与maxStatementPerConnection
//			// 均为0，则缓存被关闭，默认为0；
//			cpds.setMaxStatements(0);
//			// 连接池内单个连接所拥有的最大缓存被关闭，默认为0
//			cpds.setMaxStatementsPerConnection(0);
//			// c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些
//			// 操作可以有效提高性能，通过多线程实现多个操作同时被执行。默认为3
//			cpds.setNumHelperThreads(3);
//			// 用户修改系统参数配置，参数执行前等待的秒数。默认为300
//			cpds.setPropertyCycle(300);
//		} catch (Exception e) {
//			UtilLog.logError("getConnPool:", e);
//		}
//		return cpds;
//	}
//
//	// 开始事务
//	public static void beginTransaction(Connection con) {
//		try {
//			con.setAutoCommit(false);
//		} catch (SQLException e) {
//			UtilLog.logError("开始事务时出错:", e);
//		}
//		return;
//	}
//
//	// 提交事务
//	public static void commitTransaction(Connection con) {
//		try {
//			con.commit();
//			con.setAutoCommit(true);
//		} catch (SQLException e) {
//			UtilLog.logError("提交事务时出错:", e);
//		}
//	}
//
//	// 回滚事务
//	public static void rollbackTransaction(Connection con) {
//		try {
//			con.rollback();
//		} catch (SQLException e) {
//			UtilLog.logError("回滚事务时出错:", e);
//		}
//	}
//
//	// 关闭连接
//	public static void closeConnection(Connection con) {
//		try {
//			close(con);
//		} catch (SQLException e) {
//			UtilLog.logError("关闭连接时出错:", e);
//		}
//		return;
//	}
//
//	// 连接测试
//	public static boolean TestConnection(DbConnection dbcon)
//			throws SQLException {
//		boolean rs = true;
//		Connection con = null;
//		try {
//			// System.out.println(dbcon.getDbUser()
//			// + "/"
//			// + CryptUtil.getInstance().decryptAES(dbcon.getDbPassword(),
//			// CryptUtil.key));
//			con = getConnection(dbcon);
//			if (con == null)
//				rs = false;
//		} catch (Exception e) {
//			rs = false;
//			UtilLog.logError(dbcon.getDbCon() + "测试连接不上原因:  "
//					+ dbcon.getDbCon() + "|" + dbcon.getDbUser() + "  ", e);
//		} finally {
//			close(con);
//		}
//		return rs;
//	}
//
//	// 关闭对象
//	private static void close(Object... objects) throws SQLException {
//		if (objects.length > 0) {
//			for (Object o : objects) {
//				if (null != o) {
//					if (o instanceof Connection)
//						((Connection) o).close();
//					else if (o instanceof Statement)
//						((Statement) o).close();
//					else if (o instanceof ResultSet)
//						((ResultSet) o).close();
//					else
//						o = null;
//				}
//			}
//		}
//	}
//
//	// 获取DBF连接
//	public static DbConnection getDbfConn(String dbName, String dbfFileParent) {
//		DbConnection conDbf = new DbConnection();
//		conDbf.setDbName(dbName);
//		conDbf.setDbType("DBF");
//		conDbf.setDbClassName("sun.jdbc.odbc.JdbcOdbcDriver");
//		conDbf
//				.setDbCon("jdbc:odbc:DRIVER={Microsoft dBase Driver (*.dbf)};DBQ="
//						+ dbfFileParent + ";");
//		conDbf.setDbUser("");
//		conDbf.setDbPassword("");
//		return conDbf;
//	}
//
//	public static Connection getCon(String dbName)
//			throws ClassNotFoundException, SQLException {
//		DbConnection dbcon = DbConnectionDao.getInstance().getMapDbConn(dbName);
//		Class.forName(dbcon.getDbClassName());
//		Connection con = DriverManager.getConnection(dbcon.getDbCon(), dbcon
//				.getDbUser(), UtilCrypt.getInstance().decryptAES(
//				dbcon.getDbPassword(), UtilCrypt.key));
//		return con;
//	}
//
//	public static Connection getCon(String classDriver, String url,
//			String user, String password) throws Exception {
//		Connection con = null;
//		Class.forName(classDriver).newInstance();
//		con = DriverManager.getConnection(url, user, password);
//		return con;
//	}
//
//	// 获取Oracle连接
//	public static Connection getOracleCon(String url, String user,
//			String password) throws Exception {
//		String classDriver = "oracle.jdbc.driver.OracleDriver";
//		return getCon(classDriver, url, user, password);
//	}
//
//	// 获取Oracle连接
//	public static Connection getOracleCon(String ip, String port,
//			String database, String user, String password) throws Exception {
//		String classDriver = "oracle.jdbc.driver.OracleDriver";
//		return getCon(classDriver, "jdbc:oracle:thin:@" + ip + ":" + port + ":"
//				+ database, user, password);
//	}
//
//	public static void clear() {
//		MapCon.clear();
//		MapConPool.clear();
//	}
//	// public static void main(String[] args) throws SQLException {
//	// DbConnection dbconn = DbConnectionDao.getInstance().getDbConn("otc");
//	// Connection con = getConnection(dbconn);
//	// Statement sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//	// ResultSet.CONCUR_READ_ONLY);
//	// ResultSet rs = sm.executeQuery("select sysdate from dual");
//	// while (rs.next()) {
//	// System.out.println(rs.getString(1));
//	// }
//	// con.close();
//	// MapConPool.get(dbconn.getDbName()).close();
//	// }
//
//}
