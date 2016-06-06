package entity;

/**
 * 用户类
 * 登录系统用户
 */

public class Users {
	
	private String username;
	private String password;
	private String gcode;
	
	public Users(String username, String password, String gcode) {
		//super();
		this.username = username;
		this.password = password;
		this.gcode = gcode;
	}

	public String getGcode() {
		return gcode;
	}

	public void setGcode(String gcode) {
		this.gcode = gcode;
	}
	
	public Users(){
		
	}
	

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
