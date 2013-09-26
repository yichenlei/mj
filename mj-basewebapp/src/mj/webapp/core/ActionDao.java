package mj.webapp.core;

import org.springframework.util.StringUtils;

public class ActionDao {
	
	private String actionId = null;
	private String actionName = null;
	private String actionType = null;
	private String actionClass = null;
	private String actionMethod = null;
	private String successPage = null;
	private String failurePage = null;
	private String nextUrl = null;
	private boolean isNeedLogin = false;
	private boolean isNeedPriv = true;
	private boolean isTemplatePage = true;
	
	public boolean hasSuccessPage(){
		if(StringUtils.hasText(this.successPage)){
			return true;
		}else{
			return false;
		}
	}
	public boolean isNeedLogin() {
		return isNeedLogin;
	}
	public void setNeedLogin(boolean isNeedLogin) {
		this.isNeedLogin = isNeedLogin;
	}
	public boolean isNeedPriv() {
		return isNeedPriv;
	}
	public void setNeedPriv(boolean isNeedPriv) {
		this.isNeedPriv = isNeedPriv;
	}
	public boolean isTemplatePage() {
		return isTemplatePage;
	}
	public void setTemplatePage(boolean isTemplatePage) {
		this.isTemplatePage = isTemplatePage;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionClass() {
		return actionClass;
	}
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}
	public String getActionMethod() {
		return actionMethod;
	}
	public void setActionMethod(String actionMethod) {
		this.actionMethod = actionMethod;
	}
	public String getSuccessPage() {
		return successPage;
	}
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}
	public String getFailurePage() {
		return failurePage;
	}
	public void setFailurePage(String failurePage) {
		this.failurePage = failurePage;
	}
	public String getNextUrl() {
		return nextUrl;
	}
	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
}
