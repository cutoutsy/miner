package miner.spider.utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

/**
 * XML Read util
 *
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
                continue;
            }
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
        boolean rs = stmt
                .execute("insert into task (wid, pid, tid, name, description, urlpattern, urlgenerate, isloop) values ('"
                        + wid + "','" + pid + "','" + tid + "','" + name + "','" + description + "','" + urlpattern + "','" + urlgenerate + "','" + isloop + "')");

        System.out.println("sucess!!");
    }

    public static void readDataToMysql(String file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
        }
        String property = "";
        Element memberElm=root.element("property");
        List nodes = memberElm.elements("name");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            property += elm.getText()+"$";
        }
        property = property.substring(0, property.length()-1);

        String[] datainfo = oneProject.split("\\$");
        String wid = datainfo[0];
        String pid = datainfo[1];
        String tid = datainfo[2];
        String did = datainfo[3];
        String description = datainfo[4];
        String rowKey = datainfo[6];
        String foreignkey = datainfo[7];
        String foreignvalue = datainfo[8];
        String link = datainfo[9];
        String processWay = datainfo[10];
        String lcondition = datainfo[11];
        Connection con = MysqlUtil.getConnection();
        Statement stmt = con.createStatement();
        boolean rs = stmt
                .execute("insert into data (wid, pid, tid, dataid , description, property, rowKey, foreignKey, foreignValue, link, processWay, lcondition) values ('"
                        + wid + "','" + pid + "','"+ tid + "','"+ did + "','"+ description + "','"+ property + "','"+ rowKey + "','"+ foreignkey + "','"+ foreignvalue + "','"+ link + "','"+ processWay + "','"+ lcondition + "')");
        System.out.println("sucess!!");
    }

    public static void readRegexToMysql(String file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        String oneProject = "";
        for (Iterator<?> i = root.elementIterator(); i.hasNext();){
            Element element = (Element)i.next();
            oneProject += element.getText()+"$";
        }
        String[] datainfo = oneProject.split("\\$");

        String wid = datainfo[0];
        String pid = datainfo[1];
        String tid = datainfo[2];
        String did = datainfo[3];

        Element memberElm=root.element("rules");

        List nodes = memberElm.elements("item");
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            Element elm = (Element) it.next();
            Attribute attribute = elm.attribute("tagname");
            String tagname = attribute.getText();
            String path = elm.getText();

            Connection con = MysqlUtil.getConnection();
            Statement stmt = con.createStatement();
            boolean rs = stmt
                .execute("insert into regex (wid, pid, tid, tagname, path) values ('"
                        + wid + "','" + pid + "','" + tid + "','"+ tagname + "','"+ path + "')");
        }
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

//            readProjectToMysql("./conf/project.xml");
//            readTaskToMysql("./conf/task.xml");

//            readProjectToMysql("./conf/projectb.xml");
//            readTaskToMysql("./conf/taskb.xml");

//            readProjectToMysql("./conf/projectc.xml");
//            readTaskToMysql("./conf/taskc.xml");

//            readProjectToMysql("./conf/projectd.xml");
//            readTaskToMysql("./conf/taskd.xml");

//            readDataToMysql("./conf/data.xml");
//            readRegexToMysql("./conf/dataregex.xml");

//            readWorkspaceToMysql("./conf/workspace.xml");
//            readProjectToMysql("./conf/project.xml");
//            readTaskToMysql("./conf/task.xml");
//            readDataToMysql("./conf/data.xml");
//            readRegexToMysql("./conf/dataregex.xml");

//            readWorkspaceToMysql("./conf/workspace_elong.xml");
//            readProjectToMysql("./conf/project_elong.xml");
//            readTaskToMysql("./conf/task_elong.xml");
//            readDataToMysql("./conf/data_elong.xml");
//            readRegexToMysql("./conf/dataregex_elong.xml");

//            readWorkspaceToMysql("./conf/workspace_4s.xml");
//            readProjectToMysql("./conf/project_4s.xml");
//            readTaskToMysql("./conf/task_4s_1.xml");
//            readTaskToMysql("./conf/task_4s_2.xml");

//            readDataToMysql("./conf/data_4s_1_task1.xml");
//            readDataToMysql("./conf/data_4s_1_task2.xml");
//            readDataToMysql("./conf/data_4s_2_task1.xml");
//            readDataToMysql("./conf/data_4s_2_task2.xml");
//
//            readRegexToMysql("./conf/dataregex_4s.xml");
//            readRegexToMysql("./conf/dataregex_4s_task2.xml");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
