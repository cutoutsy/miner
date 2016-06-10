package action;

import com.opensymphony.xwork2.ModelDriven;
import entity.Users;
import org.apache.struts2.interceptor.validation.SkipValidation;
import service.UsersDAO;
import service.impl.UsersDAOImpl;
import utils.IOUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


//用户action类，使用模型驱动接收表单数据

public class UsersAction extends SuperAction implements ModelDriven<Users> {
	
	
	private Users user = new Users();

	//用户登录操作
	public String login(){
		UsersDAO udao = new UsersDAOImpl();
		if(udao.usersLogin(user)){

			//在session中保存登录成功的用户名
			session.setAttribute("loginUserName", user.getUsername());

			//返回一个视图
			return "login_success";
		}else{
			return "login_failure";
		}
	}

	//用户注销方法,执行注销方法时不执行表单验证，使用@SkipValidation注解
	@SkipValidation
	public String logout(){
		if(session.getAttribute("loginUserName") != null){
			session.removeAttribute("loginUserName");
		}
		
		return "logout_success";
	}
	
	
	//表单验证
	@Override
	public void validate() {
		//检查用户名不能为空
		if("".equals(user.getUsername().trim())){
			this.addFieldError("usernameError", "用户名不能为空");
		}else if("".equals(user.getPassword().trim())){
			this.addFieldError("usernameError", "密码不能为空");
		}else if(user.getPassword().length() < 5){
			this.addFieldError("passwordError", "密码长度不能小于5位");
		}else if(!session.getAttribute("validateCode").equals(user.getGcode())){
			System.out.println(user.getGcode() +"=="+session.getAttribute("validateCode"));
			this.addFieldError("gcodeError", "验证码错误");
		}
	}

	public Users getModel() {
		return this.user;
	}

}
