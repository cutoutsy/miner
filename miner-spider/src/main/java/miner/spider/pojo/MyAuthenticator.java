package miner.spider.pojo;

import javax.mail.*;

/**
 * Created by cutoutsy on 7/25/15.
 */
public class MyAuthenticator extends Authenticator{
    String username = null;
    String password = null;

    public MyAuthenticator(){
    }

    public MyAuthenticator(String username,String password){
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(username,password);
    }
}
