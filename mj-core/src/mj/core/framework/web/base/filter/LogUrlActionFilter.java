package mj.core.framework.web.base.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import mj.util.common.IpAdress;

public class LogUrlActionFilter implements Filter {
	
	protected FilterConfig filterConfig = null;
	private String user_ip = null;
	private String session_id = null;
	private StringBuffer url = null;
	private String optTime = null;
	static Logger logger = Logger.getLogger(LogUrlActionFilter.class.getName());

	@Override
	public void destroy() {
		// 销毁变量释放内存
		
		this.filterConfig = null;
		this.user_ip = null;
		this.url = null;
		this.optTime = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//记录用户的访问操作
        HttpServletRequest request2use = (HttpServletRequest)request;
        HttpServletResponse response2use = (HttpServletResponse)response;
        
        url = request2use.getRequestURL();
        
        //对url进行过滤，如果是js/css/image则不进行处理
        if (judgeFile(url.toString())){
            optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                              format(new java.util.Date());
            this.user_ip = IpAdress.getIpAddr(request2use);
            session_id = request2use.getRequestedSessionId();
            String queryString = request2use.getQueryString();            
            
            //拼接url
            if (null != queryString) {
                url.append('?');
                url.append(queryString);
            }
 
            //保存到数据库中
            logAction(session_id,user_ip,url.toString(),optTime);
        }


		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	public void logAction(String session_id,String user_ip,String url,String optTime){
//		System.out.println("[session_id]"+session_id);
//		System.out.println("[user_ip]"+user_ip);
//		System.out.println("[url]"+url);
//		System.out.println("[optTime]"+optTime);
	}
	
	public boolean judgeFile(String url){
		if(logger.isDebugEnabled()){
			logger.debug("【访问URL】"+url);
		}
        if (url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".png")
            || url.endsWith(".bmp") || url.endsWith(".css") || url.endsWith(".js")
                || url.endsWith(".jsx")){
            return false;
        } else {
            return true;
        }
    }
}
