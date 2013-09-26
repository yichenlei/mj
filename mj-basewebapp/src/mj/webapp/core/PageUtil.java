package mj.webapp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mj.core.db.DBHelper;

/**
 * Page的存储与操作类
 * @author yichen.lei
 * 2013-09-13
 */
public class PageUtil {

	private static Logger logger = Logger.getLogger(ActionUtil.class.getName());
	private static DBHelper dbHelper = new DBHelper();
	private static List<PageDao> pageList = new ArrayList<PageDao>();
	private static List<String> pageIdList = new ArrayList<String>();
	
	static{
		//初始化ActionUtil
		initPageUtil();
	}
	
	public static void initPageUtil(){
		loadPageList();
		logger.info("【成功加载】"+pageIdList.size()+"个PAGE.");
	}
	
	/**
	 * 调用此方法可让static运行
	 */
	public static void doNothing(){
		
	}
	
	/**
	 * 加载action
	 */
	public static void loadPageList(){
		String sql = "SELECT PAGE_ID,PAGE_NAME,PAGE_PATH,IS_NEED_LOGIN,IS_NEED_PRIV,IS_TEMPLATE_PAGE FROM SYS_PAGE";
		List<Map<String,Object>> list = dbHelper.query(sql);
		for (Map<String, Object> map : list) {
			String pageId = map.get("PAGE_ID").toString();
			if(!pageIdList.contains(pageId)){
				String pageName = null==map.get("PAGE_NAME")?"":map.get("PAGE_NAME").toString();
				String pagePath = null==map.get("PAGE_PATH")?"":map.get("PAGE_PATH").toString();
				String isNeedLogin = null==map.get("IS_NEED_LOGIN")?"":map.get("IS_NEED_LOGIN").toString();
				String isNeedPriv = null==map.get("IS_NEED_PRIV")?"":map.get("IS_NEED_PRIV").toString();
				String isTemplatePage = null==map.get("IS_TEMPLATE_PAGE")?"":map.get("IS_TEMPLATE_PAGE").toString();
				
				PageDao page = new PageDao();
				page.setPageId(pageId);
				page.setPageName(pageName);
				page.setPagePath(pagePath);
				page.setNeedLogin(convertString2boolean(isNeedLogin));
				page.setNeedPriv(convertString2boolean(isNeedPriv));
				page.setTemplatePage(convertString2boolean(isTemplatePage));
				
				pageList.add(page);
				pageIdList.add(pageId);
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
	 * 通过ID查询page
	 * @param pageId
	 * @return
	 */
	public static PageDao findPageById(String pageId){
		PageDao page = null;
		for (PageDao tmpPage : pageList) {
			String tmpPageId = tmpPage.getPageId();
			if(pageId.equals(tmpPageId)){
				page = tmpPage;
				break;
			}
		}
		return page;
	}
	/**
	 * 通过path查询page
	 * @param pagePath
	 * @return
	 */
	public static PageDao findPageByPath(String pagePath){
		System.out.println("--pagePath>"+pagePath);
		PageDao page = null;
		for (PageDao tmpPage : pageList) {
			String tmpPagePath = tmpPage.getPagePath();
			System.out.println("--tmpPagePath>"+tmpPagePath);
			if(pagePath.equals(tmpPagePath)){
				page = tmpPage;
				break;
			}
		}
		return page;
	}
	
	public static void main(String[] args) {

	}

}
