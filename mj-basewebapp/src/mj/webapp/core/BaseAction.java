package mj.webapp.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mj.core.db.DBHelper;

import org.apache.log4j.Logger;

public abstract class BaseAction {
	
	private static Logger logger = Logger.getLogger(BaseAction.class.getName());

	protected DBHelper dbHelper = new DBHelper();
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected DataModel model = null;

	public BaseAction(){
		
	}
	/**
	 * 初始化Action，在对象构造之后执行
	 * @param request
	 * @param response
	 */
	public void initAction(HttpServletRequest request,HttpServletResponse response,DataModel model){
		this.setRequest(request);
		this.setResponse(response);
		this.setModel(model);
	}
	
	/**
	 * 在执行execute()方法之前执行
	 */
	public abstract void beforeExecute();
	/**
	 * 默认业务处理方法,需要根据具体业务编程实现；根据业务需要也可以增加其它方法。
	 */
	public abstract void execute();
	/**
	 * 在执行execute()方法之后执行
	 */
	public abstract void afterExecute();
	
	
	///////////////////////////////////////////////////////
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setModel(DataModel model) {
		this.model = model;
	}
	public DataModel getModel() {
		return model;
	}

	////////////////
	public void assertNotNull(Object object,String msg){
		if(null==object){
			this.model.set(Constant.ERROR_MSG, msg);
			throw new IllegalArgumentException(msg);
		}
	}
	
	public void assertTrue(boolean falg,String msg){
		if(!falg){
			this.model.set(Constant.ERROR_MSG, msg);
			throw new IllegalArgumentException(msg);
		}
	}
}
