package mj.webapp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mj.core.db.DBHelper;

/**
 * Action的存储与操作类
 * @author yichen.lei
 * 2013-09-13
 */
public class ActionUtil {

	private static Logger logger = Logger.getLogger(ActionUtil.class.getName());
	private static DBHelper dbHelper = new DBHelper();
	private static List<ActionDao> actionList = new ArrayList<ActionDao>();
	private static List<String> actionIdList = new ArrayList<String>();
	
	static{
		//初始化ActionUtil
		initActionUtil();
	}
	
	public static void initActionUtil(){
		loadActionList();
		logger.info("【成功加载】"+actionIdList.size()+"个ACION.");
	}
	
	/**
	 * 调用此方法可让static运行
	 */
	public static void doNothing(){
		
	}
	
	/**
	 * 加载action
	 */
	public static void loadActionList(){
		String sql = "SELECT ACTION_ID,ACTION_NAME,ACTION_TYPE,ACTION_CLASS,ACTION_METHOD,SUCCESS_PAGE,FAILURE_PAGE,NEXT_URL,IS_NEED_LOGIN,IS_NEED_PRIV,IS_TEMPLATE_PAGE FROM SYS_ACTION";
		List<Map<String,Object>> list = dbHelper.query(sql);
		for (Map<String, Object> map : list) {
			String actionId = map.get("ACTION_ID").toString();
			if(!actionIdList.contains(actionId)){
				String actionName = null==map.get("ACTION_NAME")?"":map.get("ACTION_NAME").toString();
				String actionType = null==map.get("ACTION_TYPE")?"":map.get("ACTION_TYPE").toString();
				String actionClass = null==map.get("ACTION_CLASS")?"":map.get("ACTION_CLASS").toString();
				String actionMethod = null==map.get("ACTION_METHOD")?"":map.get("ACTION_METHOD").toString();
				String successPage = null==map.get("SUCCESS_PAGE")?"":map.get("SUCCESS_PAGE").toString();
				String failurePage = null==map.get("FAILURE_PAGE")?"":map.get("FAILURE_PAGE").toString();
				String nextUrl = null==map.get("NEXT_URL")?"":map.get("NEXT_URL").toString();
				String isNeedLogin = null==map.get("IS_NEED_LOGIN")?"":map.get("IS_NEED_LOGIN").toString();
				String isNeedPriv = null==map.get("IS_NEED_PRIV")?"":map.get("IS_NEED_PRIV").toString();
				String isTemplatePage = null==map.get("IS_TEMPLATE_PAGE")?"":map.get("IS_TEMPLATE_PAGE").toString();
				
				ActionDao action = new ActionDao();
				action.setActionClass(actionClass);
				action.setActionId(actionId);
				action.setActionMethod(actionMethod);
				action.setActionName(actionName);
				action.setFailurePage(failurePage);
				action.setNeedLogin(convertString2boolean(isNeedLogin));
				action.setNeedPriv(convertString2boolean(isNeedPriv));
				action.setTemplatePage(convertString2boolean(isTemplatePage));
				action.setNextUrl(nextUrl);
				action.setSuccessPage(successPage);
				action.setActionType(actionType);
				
				actionList.add(action);
				actionIdList.add(actionId);
			}
		}
	}
	
	public static boolean convertString2boolean(String str){
		boolean bl = false;
		if(null!=str && "1".equals(str)){
			bl = true;
		}
		return bl;
	}
	/**
	 * 通过ID查询Action
	 * @param actionId
	 * @return
	 */
	public static ActionDao findActionById(String actionId){
		ActionDao action = null;
		for (ActionDao tmpAction : actionList) {
			String tmpActionId = tmpAction.getActionId();
			if(actionId.equals(tmpActionId)){
				action = tmpAction;
				break;
			}
		}
		return action;
	}
	
	public static void main(String[] args) {

	}

}
