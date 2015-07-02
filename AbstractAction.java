
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com..ap.model.commons.MenuModel;
import com..ap.model.sysmeterial.TSysStatus;
import com..ap.model.sysuser.TSysUser;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 定义每个action的抽象类，里面定义了模组的功能类型和常用方法
 * 
 *  add Request and response methods here.
 * 
 *
 */
public class AbstractAction extends ActionSupport {
	//TODO 模块的ID信息要从文件中读取，建议从web.xml加载
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6655372714902221271L;
	/**session存放当前登录系统的用户bean*/
	public static final String AP_SYS_SESSION_LOGON_USER = "AP_SYS_SESSION_LOGON_USER";
	/**
	 * AP 系统中所有变量在ServletContext Mapping中的key值 value是一个新的MAP
	 */
	public static final String AP_SYS_CONTEXT_CONSTANT = "AP_SYS_CONTEXT_CONSTANT";
	
	
	/**加载操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_LOAD ="AP-SYS-LOAD";
	
	/**保存操作:  (平台的操作别名，用于各个action对用户的权限的判断). */	
	public static final String AP_SYS_FUNCTION_SAVE ="AP-SYS-SAVE";
	/**列表操作:  (平台的操作别名，用于各个action对用户的权限的判断). */	
	public static final String AP_SYS_FUNCTION_lIST ="AP-SYS-LIST";
	/**查看单一记录操作:相当于listone。 (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_VIEW ="AP-SYS-VIEW";
	/** 查询操作:  (平台的操作别名，用于各个action对用户的权限的判断)). */
	public static final String AP_SYS_FUNCTION_QUERY ="AP-SYS-QUERY";
	/** 新增操作:  (平台的操作别名，用于各个action对用户的权限的判断. */
	public static final String AP_SYS_FUNCTION_ADD ="AP-SYS-ADD";
	/** 修改操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_MODIFY ="AP-SYS-MODIFY";
	/** 删除操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_DELETE ="AP-SYS-DROP";
	/** 配置设定操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_CONFIG ="AP-SYS-CONFIG";
	/** 上传操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_UPLOAD ="AP-SYS-UPLOAD";
	/** 下载操作:  (平台的操作别名，用于各个action对用户的权限的判断). */
	public static final String AP_SYS_FUNCTION_DOWNLOAD ="AP-SYS-DOWNLOAD";
	
	
	/**
	 * 表示SoftPhone的IO类型
	 */
	public static final String AP_SOFTPHONE_IO_TYPE ="279c9a5a-197e-40fa-b308-ad64236e535a";
	/**
	 * 环境变量中的类型数据
	 */
	public static final String AP_VARABLE_ROOT_TYPE ="539897a9-806d-46e4-8665-05085a3bba4c";

	/**
	 * 环境变量中的环境变量
	 */
	public static final String AP_VARABLE_COM_VARIABLE ="397ba113-c1e8-4623-8458-03d3d60240e6";

	/**
	 * 用户登录后的权限对象
	 */
	public static final String AP_SYS_SESSION_MENUMODEL = "AP_SYS_SESSION_MENUMODEL";
	
	/**
	 * 当前用户的状态
	 */
	private static final String CURRECT_STATUS_SESSION = "userCurrectStatus";
	
	/** Logger available to subclasses */
	protected final  Logger log = (Logger.getLogger(getClass()));
	
	//存放客户对象列表
	protected static final String SESSION_CLIENT_LIST = "SESSION_CLIENT_LIST";
	//存放当前进线客户
	protected static final String SESSION_CURRENT_CLIENT = "SESSION_CURRENT_CLIENT";
	
	/**
	 * session can be get by  request.getSession()
	 * @return
	 */
	protected static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	protected static ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
		
	}


	protected static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 得到用户对功能权限判断的处理实例
	 * @return
	 */
	protected static PermissionUtil  getPermissionUtil(){
		return PermissionUtil.createPermissionUtil(getSessionMenuModel());
				 
	}
	
	/**
	 * 取得系统常量，这个常量是系统级的，在as的runtime内都有效
	 * @param strKey
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public static Object getSysConstant(String strKey){		
		return SysConstant.getSysConstant(strKey);
		
	}	


	/**
	 * 设置系统常量，这个常量是系统级的，在as的runtime内都有效
	 * @param strKey 关键字
	 * @param ojbValue
	 */
	@SuppressWarnings("unchecked")
	public static void setSysConstant(String strKey,Object ojbValue){
		SysConstant.setSysConstant(strKey, ojbValue);
	}
	
	/**
	 * 设置系统常量，把某个变量从系统删除
	 * @param strKey 关键字
	 * @param ojbValue
	 */
	@SuppressWarnings("unchecked")
	public static void removevSysConstant(String strKey){
		SysConstant.removevSysConstant(strKey);
	}
	
	
	protected static void printToResponse(String xmlString) throws IOException{
	
		HttpServletResponse response ;
        //将生成的XML字符串发往客户端     
		response=getResponse();
        response.setContentType("text/xml; charset=UTF-8");      
        response.setHeader("Cache-Control", "no-cache"); 	
		response.getWriter().print(xmlString);	
		response.getWriter().flush();      

	}
	
	

	/**
	 * @param str 
	 * @throws IOException
	 */
	public static void writeToResponse(String str) throws IOException {
		getResponse().setContentType("text/html;charset=UTF-8");
		getResponse().getWriter().write(str);
	}
	

	/**
	 * 取得当前用户的国际化语言类型 
	 * @return
	 * <li>zh_CN 简体中文
	 * <li>zh_TW 繁体中文
	 * <li>en_US 英文
	 */
	public static String getI18Type()  {
		
		String type =(String)getRequest().getSession().getAttribute("AP_SYS_USER_I18_TYPE");
		if(type==null && "".equals(type)){
			return "zh_CN";
		}else{
			return type;
		}
		//.setContentType("text/html;charset=UTF-8");
		//getResponse().getWriter().write(str);
	}
	
	/**
	 * 设置登录系统的用户
	 * @param loginUser
	 */
	public static void setSessionLoginUser(TSysUser loginUser) {		
		getRequest().getSession().setAttribute(AP_SYS_SESSION_LOGON_USER, loginUser);		
	}

	
	/**
	 * 设置登录系统的用户
	 * @param loginUser
	 */
	public static void invalidateLoginUser() {		
		getRequest().getSession().invalidate();		
	}
	

	/**
	 * 取得当前系统用户
	 * @param loginUser
	 */
	public static TSysUser getSessionLoginUser() {
		return (TSysUser) getRequest().getSession().getAttribute(AP_SYS_SESSION_LOGON_USER);
	}
	
	/**
	 * 判断用户是否登录或过期
	 * @return
	 */
	public static boolean isLogon(){
	
		try {
			TSysUser loginUser  =getSessionLoginUser();		
			if(loginUser ==null){			
				return false;			
			}else{
				return true;
			}
		} catch (RuntimeException e) {			
			return false;
		}
		
	}
	
	/**
	 * 设置登录着的权限信息
	 * @param loginUser
	 */
	public static void setSessionMenuModel(MenuModel model) {		
		getRequest().getSession().setAttribute(AP_SYS_SESSION_MENUMODEL, model);		
	}
	/**
	 * 得到登录着的权限信息
	 * @return
	 */
	public static MenuModel getSessionMenuModel(){
		return (MenuModel) getRequest().getSession().getAttribute(AP_SYS_SESSION_MENUMODEL);
	}

	
	/**
	 * @param beanName 在Request中取得Spring配置中的Bean
	 * @param beanIFClass 此bean的接口或实例类
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public static Object getSpringBean(String beanName,Class beanIFClass){		
		ServletContext servletContext = getRequest().getSession().getServletContext();		
		WebApplicationContext wac = WebApplicationContextUtils.
		            getRequiredWebApplicationContext(servletContext);			
		return  wac.getBean(beanName,beanIFClass);		
				
	}	
	public static String getRealPath(String path){
		return ServletActionContext.getServletContext().getRealPath(path);

	}
	
	public static String getRootPath() {
		if (getRequest() == null) {
			throw new RuntimeException("request is null.");
		}
		StringBuffer url = new StringBuffer();
		url.append(getRequest().getScheme() + "://");
		url.append(getRequest().getServerName());
		if (getRequest().getServerPort() != 80)
			url.append(":" + getRequest().getServerPort());
		url.append(getRequest().getContextPath());
		return url.toString();
	}
	public static String getRootPath(HttpServletRequest request ) {
		if (request == null) {
			throw new RuntimeException("request is null.");
		}
		StringBuffer url = new StringBuffer();
		url.append(request.getScheme() + "://");
		url.append(request.getServerName());
		if (request.getServerPort() != 80)
			url.append(":" + request.getServerPort());
		url.append(request.getContextPath());
		return url.toString();
	}
	
	/**
	 * 输入相对路径如： pages/index.jsp
	 * 返回绝对URL如： http://localhost:8080/applatform/pages/index.jsp
	 * @param relativePath
	 * @return
	 */
	public static String getAbsolutePath(HttpServletRequest request,String relativePath) {
			if (relativePath.startsWith("/"))
				return getRootPath(request) + relativePath;
			else
				return getRootPath(request) + "/" + relativePath;
		}
	
	/**
	 * 获取当前session中的状态
	 * @return
	 */
	public static TSysStatus getSessionStatus(){
		HttpSession session = getRequest().getSession();
		// 查询当前是否已经有状态设置
		TSysStatus curStatus = (TSysStatus) session
				.getAttribute(CURRECT_STATUS_SESSION);
		return curStatus;
	}
	
	/**
	 * 获取当前session中的进线客户
	 * @return
	 */
	public static Map getSessionInlineClient(){
		HttpSession session = getRequest().getSession();
		// 当前session中的进线客户
		Map curClient = (Map) session
				.getAttribute(SESSION_CURRENT_CLIENT);
		return curClient;
	}
	
}
