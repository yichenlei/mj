package mj.core.db;

import java.beans.PropertyVetoException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import mj.util.common.PropertiesUtil;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库操作帮助类
 * 
 * @author yichen.lei
 * 
 */
public class DBHelper {

	private static Logger logger = Logger.getLogger(DBHelper.class.getName());;
	private static final Map<String,ComboPooledDataSource> DATA_SOURCE_POOL_MAP = new HashMap<String,ComboPooledDataSource>();
	private JdbcTemplate jdbcTemplate;
	// 数据库连接参数
	private String dataBaseName = null;
	// 用于处理大字段
	private DefaultLobHandler lobHandler = new DefaultLobHandler();
	// 查询结果集最大值
	private int maxRows = 0;
	// 查询结果集传递批次大小,一般与分页大小一致
	private int fetchSize = 0;

	private String jdbcDriver = null;
	private String jdbcUrl = null;
	private String jdbcUser = null;
	private String jdbcPwd = null;

	public DBHelper() {
		// 加载数据库配置文件
		Properties prop = PropertiesUtil
				.getPropertiesWithCache("/c3p0.properties");
		this.setDataBaseName(prop.getProperty("c3p0.dataBaseName").trim());
		this.setJdbcDriver(prop.getProperty("c3p0.driverClass").trim());
		this.setJdbcUrl(prop.getProperty("c3p0.jdbcUrl").trim());
		this.setJdbcUser(prop.getProperty("c3p0.user").trim());
		this.setJdbcPwd(prop.getProperty("c3p0.password").trim());
		
		this.jdbcTemplate = new JdbcTemplate(this.getDataSource(prop));
		this.jdbcTemplate.setNativeJdbcExtractor(new C3P0NativeJdbcExtractor());
		this.setLobHandler();
		this.setQueryParameter();
	}

	public DBHelper(String dataBaseName,String driverClass,String jdbcUrl,String user,String password) {
		// 加载数据库配置文件
		this.setDataBaseName(dataBaseName.trim());
		this.setJdbcDriver(driverClass.trim());
		this.setJdbcUrl(jdbcUrl.trim());
		this.setJdbcUser(user.trim());
		this.setJdbcPwd(password.trim());
		
		this.jdbcTemplate = new JdbcTemplate(this.getDataSource());
		this.jdbcTemplate.setNativeJdbcExtractor(new C3P0NativeJdbcExtractor());
		this.setLobHandler();
		this.setQueryParameter();
	}
	
	public DBHelper(String dataBaseName,String driverClass,String jdbcUrl,String user,String password,int minPoolSize,int maxPoolSize) {
		// 加载数据库配置文件
		this.setDataBaseName(dataBaseName.trim());
		this.setJdbcDriver(driverClass.trim());
		this.setJdbcUrl(jdbcUrl.trim());
		this.setJdbcUser(user.trim());
		this.setJdbcPwd(password.trim());
		
		this.jdbcTemplate = new JdbcTemplate(this.getDataSource(minPoolSize,maxPoolSize));
		this.jdbcTemplate.setNativeJdbcExtractor(new C3P0NativeJdbcExtractor());
		this.setLobHandler();
		this.setQueryParameter();
	}
	
	private ComboPooledDataSource getNewDataSource(String dataSourceName) {
		
		if(logger.isDebugEnabled())
			logger.debug("新建数据库连接池:"+dataSourceName);
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		dataSource.setDataSourceName("dataSourceName");
		dataSource.setJdbcUrl(this.getJdbcUrl());
		dataSource.setUser(this.getJdbcUser());
		dataSource.setPassword(this.getJdbcPwd());
		try {
			dataSource.setDriverClass(this.getJdbcDriver());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return dataSource;
	}
	
	private String getDataSourceName(){
		return new StringBuffer().append(this.getDataBaseName())
				.append("<->").append(this.getJdbcUrl())
				.append("<->").append(this.getJdbcUser()).toString();
	}
	private ComboPooledDataSource getDataSource() {
		String dsName = this.getDataSourceName();
		ComboPooledDataSource dataSource = null;
		if (DATA_SOURCE_POOL_MAP.containsKey(dsName)) {
			dataSource = (ComboPooledDataSource) DATA_SOURCE_POOL_MAP
					.get(dsName);
		} else {
			dataSource = this.getNewDataSource(dsName);

			DATA_SOURCE_POOL_MAP.put(dsName, dataSource);
		}
		return dataSource;
	}
	
	private ComboPooledDataSource getDataSource(int minPoolSize,int maxPoolSize) {
		String dsName = this.getDataSourceName();
		ComboPooledDataSource dataSource = null;
		if (DATA_SOURCE_POOL_MAP.containsKey(dsName)) {
			dataSource = (ComboPooledDataSource) DATA_SOURCE_POOL_MAP
					.get(dsName);
		} else {
			dataSource = this.getNewDataSource(dsName);
			dataSource.setMinPoolSize(minPoolSize);
			dataSource.setMaxPoolSize(maxPoolSize);

			DATA_SOURCE_POOL_MAP.put(dsName, dataSource);
		}
		return dataSource;
	}

	private ComboPooledDataSource getDataSource(Properties prop) {
		String dsName = this.getDataSourceName();
		ComboPooledDataSource dataSource = null;
		if (DATA_SOURCE_POOL_MAP.containsKey(dsName)) {
			dataSource = (ComboPooledDataSource) DATA_SOURCE_POOL_MAP
					.get(dsName);
		} else {
			dataSource = this.getNewDataSource(dsName);
			dataSource.setMinPoolSize(Integer.valueOf(prop.getProperty(
					"c3p0.minPoolSize").trim()));
			dataSource.setInitialPoolSize(Integer.valueOf(prop.getProperty(
					"c3p0.initialPoolSize").trim()));
			dataSource.setMaxPoolSize(Integer.valueOf(prop.getProperty(
					"c3p0.maxPoolSize").trim()));
			dataSource.setAcquireIncrement(Integer.valueOf(prop.getProperty(
					"c3p0.acquireIncrement").trim()));

			dataSource.setMaxIdleTime(Integer.valueOf(prop.getProperty(
					"c3p0.maxIdleTime").trim()));
			dataSource.setIdleConnectionTestPeriod(Integer.valueOf(prop
					.getProperty("c3p0.idleConnectionTestPeriod").trim()));
			dataSource.setPreferredTestQuery(prop.getProperty(
					"c3p0.preferredTestQuery").trim());
			dataSource.setCheckoutTimeout(Integer.valueOf(prop.getProperty(
					"c3p0.checkoutTimeout").trim()));

			DATA_SOURCE_POOL_MAP.put(dsName, dataSource);
		}
		return dataSource;
	}

	private void setLobHandler() throws DataAccessResourceFailureException {
		String dataBaseName = this.getDataBaseName().trim().toUpperCase();
		if (null == dataBaseName)
			return;
		// logger.debug("目前提供数据库服务的数据库产品为" + dbProductName);
		if ("ORACLE9".equals(dataBaseName) || "ORACLE9I".equals(dataBaseName))
			lobHandler.setCreateTemporaryLob(true);
	}

	/**
	 * 查询sql以 list<map>的形式返回结果
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> query(String sql) {
		return this.queryForListWithMap(sql);
	}
	public List<Map<String, Object>> query(String sql,String[] args,int[] argTypes) {
		return this.queryForListWithMap(sql, args, argTypes);
	}
	/**
	 * 查询sql以 list<map>的形式返回结果
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryForListWithMap(String sql) {
		return this.jdbcTemplate.queryForList(sql);
	}
	public List<Map<String, Object>> queryForListWithMap(String sql,String[] args,int[] argTypes) {
		return this.jdbcTemplate.queryForList(sql, args, argTypes);
	}
	/**
	 * 查询sql以 map的形式返回结果
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql) {
		return this.jdbcTemplate.queryForMap(sql);
	}
	public Map<String, Object> queryForMap(String sql,String[] args,int[] argTypes) {
		return this.jdbcTemplate.queryForMap(sql, args, argTypes);
	}
	/**
	 * 查询sql以 String的形式返回结果第一条第一列
	 * @param sql
	 * @return
	 */
	public String queryForString(String sql) {
		return this.jdbcTemplate.queryForObject(sql, String.class);
	}
	public String queryForString(String sql,String[] args,int[] argTypes) {
		return this.jdbcTemplate.queryForObject(sql, args, argTypes, String.class);
	}
	/**
	 * 查询sql以 int的形式返回结果第一条第一列
	 * @param sql
	 * @return
	 */
	public int queryForInt(String sql) {
		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}
	public int queryForInt(String sql,String[] args,int[] argTypes) {
		return this.jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
	}
	/**
	 * 查询sql以 long的形式返回结果第一条第一列
	 * @param sql
	 * @return
	 */
	public long queryForLong(String sql) {
		return this.jdbcTemplate.queryForObject(sql, Long.class);
	}
	public long queryForLong(String sql,String[] args,int[] argTypes) {
		return this.jdbcTemplate.queryForObject(sql, args, argTypes, Long.class);
	}
	/**
	 * 执行insert、update、delete语句
	 * @param sql
	 * @return
	 */
	public int update(String sql){
		return this.jdbcTemplate.update(sql);
	}
	/**
	 * 以绑定变量的方式执行insert、update、delete语句
	 * @param sql
	 * @return
	 */
	public int update(String sql,String[] args,int[] argTypes){
		return this.jdbcTemplate.update(sql, args, argTypes);
	}
	/**
	 * 批量执行sql语句
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(String[] sql){
		return this.jdbcTemplate.batchUpdate(sql);
	}
	/**
	 * 以绑定变量的方式批量执行sql语句
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(String sql,List<Object[]> batchArgs,int[] argTypes){
		return this.jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcPwd() {
		return jdbcPwd;
	}

	public void setJdbcPwd(String jdbcPwd) {
		this.jdbcPwd = jdbcPwd;
	}
	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public void setQueryParameter(){
		this.jdbcTemplate.setFetchSize(this.getFetchSize());
		this.jdbcTemplate.setMaxRows(this.getMaxRows());
	}

	public static void main(String[] args) throws SQLException {
		long start = (new Date()).getTime();
		DBHelper dbHelper = new DBHelper();
		
//		for(int i=0;i<300;i++){
//			dbHelper.update("insert into test_size(id) values('"+i+"')");
//		}
		
		//dbHelper.jdbcTemplate.setFetchSize(100);
		System.out.println("当前时间："+dbHelper.jdbcTemplate.getFetchSize()+":"+dbHelper.query("select id time from test_size").size());
		//dbHelper.jdbcTemplate.setMaxRows(10);
		System.out.println("当前时间："+dbHelper.jdbcTemplate.getMaxRows()+":"+dbHelper.query("select id time from test_size").size());
		//System.out.println("clob："+dbHelper.queryForString("select CONTENT time from test_clob"));
		
		long end = (new Date()).getTime();
		System.out.println("-----耗时："+(end-start)+"毫秒");
		DatabaseMetaData md = dbHelper.jdbcTemplate.getDataSource().getConnection().getMetaData();  
		System.out.println("getDatabaseProductName:"+md.getDatabaseProductName());  
		System.out.println("getDatabaseProductVersion:"+md.getDatabaseProductVersion());  

	}
}
