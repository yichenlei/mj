package mj.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties属性文件读取工具类
 * @author yichen.lei
 *
 */
public class PropertiesUtil {
	
	private static PropertiesUtil propertiesUtil = null;
	private static Map PROPERTIES_CACHE_POOL = new HashMap();
	
	private PropertiesUtil(){}
	
	public static PropertiesUtil getInstance(){
		if(null==propertiesUtil){
			propertiesUtil = new PropertiesUtil();
		}
		return propertiesUtil;
	}
	
	public static Properties getPropertiesWithCache(String filePath){
		Properties prop = null;
		if(PROPERTIES_CACHE_POOL.containsKey(filePath)){
			prop = (Properties)PROPERTIES_CACHE_POOL.get(filePath);
		}else{
			prop = getProperties(filePath);
			PROPERTIES_CACHE_POOL.put(filePath, prop);
		}
		return prop;
	}

	public static Properties getProperties(String filePath){
		Properties prop = new Properties();
		InputStream in = PropertiesUtil.getInstance().getClass().getResourceAsStream(filePath);
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
