package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//所有action动作的父类
public class SuperAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, ServletContextAware {
	
	protected HttpServletRequest request;	//请求对象
	protected HttpServletResponse response;		//响应对象
	protected HttpSession session;		//会话对象
	protected ServletContext application;	//全局对象


	public void setServletContext(ServletContext application) {
		this.application = application;
	}


	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.session = this.request.getSession();
	}

}
