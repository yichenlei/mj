package mj.webapp.core;

import org.lilystudio.smarty4j.Context;

/**
 * 页面之间的数据传递对象
 * @author yichen.lei
 *
 */
public class DataModel {
	
	private Context context = null;
	private ActionDao actionDao = null;

	/**
	 * 构造方法
	 * @param context
	 */
	public DataModel(Context context, ActionDao actionDao){
		this.setContext(context);
		this.setActionDao(actionDao);
	}

	public ActionDao getActionDao() {
		return actionDao;
	}

	public void setActionDao(ActionDao actionDao) {
		this.actionDao = actionDao;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void set(String name,Object value){
		this.getContext().set(name, value);
	}
	
	public Object get(String name){
		return this.getContext().get(name);
	}
}
