package mj.core.db.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
  
public class TestC3p0 {   
	Properties p = new Properties();
    public TestC3p0() {   
    	//这种获取项目中配置文件的方法，不管在app项目中还是在web项目中都适用
    	InputStream in = null;
    	String filePath = "/c3p0.properties";
		in = this.getClass().getResourceAsStream(filePath);
        try {
			p.load(in);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    }   
    
    public String test(){
    	String time = null;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();   
  
        dataSource.setDataSourceName("myDataSource");   
  
        dataSource.setJdbcUrl(p.getProperty("c3p0.jdbcUrl"));   
  
        try {   
            dataSource.setDriverClass(p.getProperty("c3p0.driverClass").trim());   
        } catch (PropertyVetoException e1) {   
            System.out.println(e1.getMessage());   
        }   
        dataSource.setUser(p.getProperty("c3p0.user"));   
        dataSource.setPassword(p.getProperty("c3p0.password"));   
        dataSource.setMaxPoolSize(Integer.valueOf(p.getProperty(   
                "c3p0.maxPoolSize").trim()));   
        dataSource.setMinPoolSize(Integer.valueOf(p.getProperty(   
                "c3p0.minPoolSize").trim()));   
        dataSource.setAcquireIncrement(Integer.valueOf(p.getProperty(   
                "c3p0.acquireIncrement").trim()));   
        dataSource.setInitialPoolSize(Integer.valueOf(p.getProperty(   
                "c3p0.initialPoolSize").trim()));   
        dataSource.setMaxIdleTime(Integer.valueOf(p.getProperty(   
                "c3p0.maxIdleTime").trim()));   
  
        // 获取连接池，连接   
        try {   
            Connection conn = dataSource.getConnection();   
  
            PreparedStatement ps = conn.prepareStatement("select sysdate from dual");   
  
            ResultSet rs = ps.executeQuery();   
  
            while (rs.next()) {   
            	time = rs.getString(1);
                System.out.println("----当前时间："+time);   
            }   
            rs.close();   
            ps.close();   
            conn.close();   
  
        } catch (SQLException e) {   
            e.printStackTrace();   
        }   
        return time;
    }
  
    public static void main(String[] args) {
    	TestC3p0 t = new TestC3p0();
    	t.test();
        
    }   
  
}  
