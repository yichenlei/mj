package mj.webapp.base.sys;

import mj.webapp.core.ActionDao;
import mj.webapp.core.BaseAction;
import mj.webapp.core.PageDao;
import mj.webapp.core.PageUtil;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class GotoPage extends BaseAction {

	@Override
	public void beforeExecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		String pageId = this.request.getParameter("pageId");
		String pagePath = this.request.getParameter("pagePath");
		
		boolean hasTarget = false;
		PageDao pageDao = null;
		if(StringUtils.hasText(pageId)){
			hasTarget = true;
			pageDao = PageUtil.findPageById(pageId);
		}else if(StringUtils.hasText(pagePath)){
			hasTarget = true;
			pageDao = PageUtil.findPageByPath(pagePath);
		}
		this.assertTrue(hasTarget, "你得告诉我你要去哪里，我才能带你去哪里！");
		this.assertNotNull(pageDao, "系统找不到您要的资源！");
		
		ActionDao action = this.model.getActionDao();
		action.setSuccessPage(pageDao.getPagePath());
		action.setTemplatePage(pageDao.isTemplatePage());
		action.setNeedLogin(pageDao.isNeedLogin());
		action.setNeedPriv(pageDao.isNeedPriv());
	}

	@Override
	public void afterExecute() {
		// TODO Auto-generated method stub

	}

}
