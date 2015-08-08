package miner.spider.utils;


import miner.spider.pojo.MailSenderInfo;
import miner.spider.pojo.MyAuthenticator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * Created by cutoutsy on 7/25/15.
 */
public class SimpleMailSender {

    //send mail by text
    public boolean sendTextMail(MailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()){
            authenticator = new MyAuthenticator(mailInfo.getUsername(),mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            String mailcontent = mailInfo.getContent();
            mailMessage.setText(mailcontent);

            Transport.send(mailMessage);
            return true;
        }catch(MessagingException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //send mail by html
    public boolean sendHtmlMail(MailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()){
            authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);

        try {
            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();

            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            mailMessage.setContent(mainPart);

            Transport.send(mailMessage);
            return true;
        }catch (MessagingException ex){
            ex.printStackTrace();
        }
        return false;
    }


    public static void SendMail(String toAddress, String subject, String content){

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);

        mailInfo.setUsername("250618577@qq.com");
        mailInfo.setPassword("f123456");
        mailInfo.setFromAddress("250618577@qq.com");

        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);

        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);
    }

}
