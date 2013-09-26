package mj.util.common;

import javax.servlet.http.HttpServletRequest;

import mj.core.framework.web.base.filter.SetCharacterEncodingFilter;

import org.apache.log4j.Logger;

public class IpAdress {
	
	static Logger logger = Logger.getLogger(IpAdress.class.getName());

	public static String getIpAddr(HttpServletRequest request) {
		
		String ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}

	public static void main(String[] args) {
		logger.debug("------------test----------------");
	}
}
