package service.impl;

import entity.Users;
import org.apache.struts2.ServletActionContext;
import service.UsersDAO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UsersDAOImpl implements UsersDAO {
	
//	private HttpServletRequest request;
	
	
	public boolean usersLogin(Users u){
		try{
			
			ServletRequest request = ServletActionContext.getRequest();
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			if(u.getUsername().equals("admin") && u.getPassword().equals("admin") && u.getGcode().equals(session.getAttribute("validateCode"))){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
}
