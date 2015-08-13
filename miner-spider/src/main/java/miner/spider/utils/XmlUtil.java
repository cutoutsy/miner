package miner.spider.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

/**
 * XML Read util
 *
 * Created by cutoutsy on 8/4/15.
 */

public class XmlUtil {

    private static MyLogger logger = new MyLogger(XmlUtil.class);

    public static Jedis redis;

    //This method is for read XML
    public static void readDocument(String file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            //  if(!element.getName().equalsIgnoreCase("url")){
            //      System.out.println("StudentInfo is:");
            //      continue;
            //  }
            System.out.println("attribute value "+ element.getText());
        }
    }

    //This method is for read XML
    public static String readDocumentByTag(String file, String tag) throws DocumentException{
        String reValue = "";
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            if(!element.getName().equalsIgnoreCase(tag)){
                //System.out.println("StudentInfo is:");
                continue;
            }
            //System.out.println("attribute value "+ element.getText());
            reValue = element.getText();
        }
        return reValue;
    }


    public static void readProjectToRedis(String file, String redisKey) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
            //System.out.println("attribute value "+ element.getText());
        }
        String[] datainfo = oneProject.split("\\$");
        String projectKey = datainfo[0]+"-"+datainfo[1];
        String projectValue = "";

        for(int i = 2 ; i < datainfo.length; i++){
            projectValue += datainfo[i]+"$";
        }
        projectValue = projectValue.substring(0,projectValue.length()-1);
        redis = RedisUtil.GetRedis();
        redis.hset(redisKey, projectKey, projectValue);
        System.out.println("sucess!!");
    }

    public static void readTaskToRedis(String file, String redisKey) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
            //System.out.println("attribute value "+ element.getText());
        }
        String[] datainfo = oneProject.split("\\$");
        String projectKey = datainfo[0]+"-"+datainfo[1]+"-"+datainfo[2];
        String projectValue = "";

        for(int i = 3 ; i < datainfo.length; i++){
            projectValue += datainfo[i]+"$";
        }
        projectValue = projectValue.substring(0,projectValue.length()-1);
        redis = RedisUtil.GetRedis();
        redis.hset(redisKey, projectKey, projectValue);
        System.out.println("sucess!!");
    }

    public static void readWorkspaceToMysql(String file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
        }
        String[] datainfo = oneProject.split("\\$");
        int wid = Integer.valueOf(datainfo[0]);

        Connection con = MysqlUtil.getConnection();
        Statement stmt = con.createStatement();
        // 插入最新测量的数据
        boolean rs = stmt
                .execute("insert into workspace (wid, name, description) values ('"
                        + wid + "','" + datainfo[1] + "','" + datainfo[2] + "')");
        System.out.println("sucess!!");
    }

    public static void readProjectToMysql(String file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
        }
        String[] datainfo = oneProject.split("\\$");
        int wid = Integer.valueOf(datainfo[0]);
        int pid = Integer.valueOf(datainfo[1]);
        String name = datainfo[2];
        String description = datainfo[3];
        String datasource = datainfo[4];
        String schedule = datainfo[5];
        String state = datainfo[6];
        String precondition = datainfo[7];
        String projectExecuteNUm = datainfo[8];

        Connection con = MysqlUtil.getConnection();
        Statement stmt = con.createStatement();
        // 插入最新测量的数据
        boolean rs = stmt
                .execute("insert into project (wid, pid, name, description, datasource, schedule, precondition) values ('"
                        + wid + "','" + pid + "','"+ name + "','"+ description + "','"+ datasource + "','"+ schedule + "','"+ precondition + "')");

        redis = RedisUtil.GetRedis();
        redis.hset("project_state", datainfo[0]+"-"+datainfo[1], state);
        redis.hset("project_executenum", datainfo[0]+"-"+datainfo[1], projectExecuteNUm);
        redis.hset("project_cronstate", datainfo[0]+"-"+datainfo[1], "3");
        System.out.println("sucess!!");
    }


    public static void readTaskToMysql(String file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
        }
        String[] datainfo = oneProject.split("\\$");
        int wid = Integer.valueOf(datainfo[0]);
        int pid = Integer.valueOf(datainfo[1]);
        int tid = Integer.valueOf(datainfo[2]);
        String name = datainfo[3];
        String description = datainfo[4];
        String urlpattern = datainfo[5];
        String urlgenerate = datainfo[6];
        String isloop = datainfo[7];

        Connection con = MysqlUtil.getConnection();
        Statement stmt = con.createStatement();
        // 插入最新测量的数据
        boolean rs = stmt
                .execute("insert into task (wid, pid, tid, name, description, urlpattern, urlgenerate, isloop) values ('"
                        + wid + "','" + pid + "','" + tid + "','"+ name + "','"+ description + "','"+ urlpattern + "','"+ urlgenerate + "','"+ isloop + "')");

        System.out.println("sucess!!");
    }


    public static void main(String[] args){
        try {

            //String state = readDocumentByTag("./conf/project.xml", "state");
            //System.out.println(state);
            //project
//            readProjectToRedis("./conf/project.xml", "projectInfo");
//            readTaskToRedis("./conf/task.xml", "taskInfo");
//            //project b
//            readProjectToRedis("./conf/projectb.xml", "projectInfo");
//            readTaskToRedis("./conf/taskb.xml", "taskInfo");
//            //project c
//            readProjectToRedis("./conf/projectc.xml", "projectInfo");
//            readTaskToRedis("./conf/taskc.xml", "taskInfo");
//            readWorkspaceToMysql("./conf/workspace.xml");

            readProjectToMysql("./conf/project.xml");
//            readTaskToMysql("./conf/task.xml");

//            readProjectToMysql("./conf/projectb.xml");
//            readTaskToMysql("./conf/taskb.xml");

//            readProjectToMysql("./conf/projectc.xml");
//            readTaskToMysql("./conf/taskc.xml");

//            readProjectToMysql("./conf/projectd.xml");
//            readTaskToMysql("./conf/taskd.xml");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
