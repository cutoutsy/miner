package miner.spider.pojo;

import java.util.Properties;

/**
 * Some base info about send mail
 *
 * Created by cutoutsy on 7/25/15.
 */
public class MailSenderInfo {
    /**
     * if the mail Server is QQ,need to open the smtp server
     */
    private String mailServerHost;
    private String mailServerPort = "25";
    private String fromAddress;
    private String toAddress;
    private String username;
    private String password;
    //require authentication,default is not
    private boolean validate = false;
    private String subject;
    private String content;
    private String[] attachFileNames;

    public Properties getProperties(){
        Properties p = new Properties();
        p.put("mail.smtp.host",this.mailServerHost);
        p.put("mail.smtp.port",this.mailServerPort);
        p.put("mail.smtp.auth",validate ? "true":"false");
        return p;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
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

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] attachFileNames) {
        this.attachFileNames = attachFileNames;
    }


}
