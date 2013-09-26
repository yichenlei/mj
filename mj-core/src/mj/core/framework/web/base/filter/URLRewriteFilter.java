package mj.core.framework.web.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * url重写
 * 1、解决在引入js、css时需要写项目名的问题
 * 2、后续可以添加url的伪静态化功能、url伪装功能
 * @author yichen.lei
 * 2013-09-15
 */
public class URLRewriteFilter implements Filter {   
	
	static Logger logger = Logger.getLogger(URLRewriteFilter.class.getName());
	
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,ServletException   
    {   
       HttpServletRequest request = (HttpServletRequest) servletRequest;   
       HttpServletResponse response = (HttpServletResponse) servletResponse;   
       
       //获取rewriteUrl
       String rewriteUrl = getRewriteUrl(request, response);
       
       if (null != rewriteUrl){
    	   //rewriteUrl不为空，说明请求的URL符合重定向规则
           //request.getRequestDispatcher(rewriteUrl).forward(request, response);   
           //return;
       } 
       
       filterChain.doFilter(servletRequest, servletResponse);   
    }   
  
    private String getRewriteUrl(HttpServletRequest request, HttpServletResponse response) {   
        String url = request.getServletPath();
        String rewriteUrl = null;
        if(url.indexOf("{PROJECT_NAME}")!=-1){
        	rewriteUrl = url.substring(url.indexOf("{PROJECT_NAME}")+14);
        	if(logger.isDebugEnabled()){
        		logger.debug("【URLRewrite->RequestUrl】"+url);
        		logger.debug("【URLRewrite->RewriteUrl】"+rewriteUrl);
        	}
        }
        
        if(url.equals(rewriteUrl)){
        	return null;
        }else{
        	return rewriteUrl;
        }
    }

	@Override
	public void destroy() {
		
	}

	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}   
}   
