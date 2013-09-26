package mj.webapp.core;

import org.springframework.util.StringUtils;

public class PageDao {
	
	private String pageId = null;
	private String pagePath = null;
	private String pageName = null;
	private boolean isNeedLogin = false;
	private boolean isNeedPriv = true;
	private boolean isTemplatePage = true;
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getPagePath() {
		return pagePath;
	}
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
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
	
	
}
