package mj.webapp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mj.util.common.IpAdress;

import org.apache.log4j.Logger;
import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

public class ActionExecute extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(ActionExecute.class.getName());
	private static Engine engine = new Engine();//加载模板引擎  

	public ActionExecute() {
		super();
	}
	
	public void init() throws ServletException {
		engine.setEncoding("utf-8");
	}
	
	public void destroy() {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String clientIp = IpAdress.getIpAddr(request);
		//接收前台页面传过来的参数
		String actionId = request.getParameter("action");
		if(logger.isDebugEnabled()){
			logger.debug("【actionId】"+actionId);
		}
		Assert.hasText(actionId, "对不起，系统没有接收到操作指令！");
		ActionDao actionDao = ActionUtil.findActionById(actionId);
		Assert.notNull(actionDao, "对不起，系统没有此功能！");
		String actionName = actionDao.getActionName();
		String actionClass = actionDao.getActionClass();
		String actionMethod = actionDao.getActionMethod();
		String actionType = actionDao.getActionType();
		String successPage = actionDao.getSuccessPage();

        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        
		
        Context ctx = new Context(); // 生成数据容器对象  
        DataModel model = new DataModel(ctx,actionDao);
        model.set("msg", "hello smarty4j!");  
        model.set(Constant.ERROR_MSG, "此功能执行出错了！");
        model.set(Constant.BASE_PATH, basePath);
        model.set("request", request);  
        model.set("response", response);  
        model.set("session", request.getSession());
        model.set("actionDao", actionDao);

		//反射加载action
		Class<?> action = null;
		boolean executeSuccess = false;
		String errorMsg = "";
		
		//监控任务执行耗时
		StopWatch stopWatch = null;
		if(logger.isDebugEnabled()){
			stopWatch = new StopWatch(actionName+System.currentTimeMillis());  
			stopWatch.start();  
		}

        try {
            action = Class.forName(actionClass);
        	//实例化action
        	Object actionInstance = action.newInstance();
            Method method=null;
            //执行init, 初始化action
            method=action.getMethod("initAction", HttpServletRequest.class,HttpServletResponse.class,DataModel.class);
            method.invoke(actionInstance,request,response,model);
            //执行beforeExecute
            method = action.getMethod("beforeExecute");
            method.invoke(actionInstance);
            //执行业务处理方法
            method = action.getMethod(actionMethod);
            method.invoke(actionInstance);
            //执行afterExecute
            method = action.getMethod("afterExecute");
            method.invoke(actionInstance);
            
            executeSuccess = true;
        }catch (Exception e) {
        	executeSuccess = false;
        	errorMsg = e.getMessage();
            e.printStackTrace();
        }
        
        
        if(logger.isDebugEnabled()){
        	stopWatch.stop();
        	logger.debug("=================================================");
        	logger.debug("||-->> [actionName  ]:"+actionName);
        	logger.debug("||-->> [exe Success ]:"+executeSuccess);
        	logger.debug("||-->> [usedtime(ms)]:"+stopWatch.getTotalTimeMillis());
        	logger.debug("||-->> [ActionClass ]:"+model.getActionDao().getActionClass());
        	logger.debug("||-->> [ActionMethod]:"+model.getActionDao().getActionMethod());
        	logger.debug("||-->> [success page]:"+model.getActionDao().getSuccessPage());
        	logger.debug("||-->> [clientIp    ]:"+clientIp);
        	logger.debug("=================================================");
        }
		
        Assert.isTrue(executeSuccess, model.get(Constant.ERROR_MSG).toString());
        
        //如果有成功页面
        if(actionDao.hasSuccessPage()){
        	//如果成功页面是模版文件
        	if(actionDao.isTemplatePage()){
        		try {
        			//合并渲染页面
        			if(logger.isDebugEnabled()){
        				logger.debug("【渲染模板页面】"+model.getActionDao().getSuccessPage());
        			}
        			render(request,response,model);
        		} catch (FileNotFoundException e) {
        			logger.error("找不到要跳转的页面");
        			e.printStackTrace();
        		} catch (Throwable e) {
        			e.printStackTrace();
        		}
        	}else{//如果成功页面不是模板页面，如jsp，当然也可以是html
        		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Constant.WEB_ROOT_DIR+successPage); 
        		dispatcher.forward(request, response); 
        	}
        		
        }else{
        	//如果是没有成功页面，就不需要做任何动作
        }
	}

	 /* 渲染页面 
     * @see org.nutz.mvc.View#render(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object) 
     */  
    public void render(HttpServletRequest request, HttpServletResponse response, DataModel model) throws Throwable {  
    	
    	//设置模版目录
    	String successPage = model.getActionDao().getSuccessPage();
    	//页面以项目根目录为相对路径
    	StringBuffer templateDir = new StringBuffer();
    	templateDir.append(request.getSession().getServletContext().getRealPath("/"));  
    	//如果页面与class在同一个目录
    	if(successPage.startsWith("@")){
    		successPage = successPage.substring(1, successPage.length());
    		String classPath = model.getActionDao().getActionClass();
    		classPath = classPath.substring(0, classPath.lastIndexOf(".")+1).replace(".", File.separator);
    		templateDir.append("WEB-INF").append(File.separator).append("classes").append(File.separator).append(classPath);
    	}
    	if(logger.isDebugEnabled()){
    		logger.debug("templateDir: "+templateDir.toString());
    	}
    	engine.setTemplatePath(templateDir.toString());
  
        Template template = engine.getTemplate(successPage);  
        template.merge(model.getContext(), response.getWriter());  
    }  


}
