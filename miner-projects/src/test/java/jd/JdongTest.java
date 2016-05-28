package jd;

import junit.framework.TestCase;
import miner.spider.utils.MysqlUtil;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 关于京东的测试类
 */
public class JdongTest extends TestCase{

    public void testMysqlConfig(){
        try {
            Connection conn = MysqlUtil.getConnection();
            Statement st = (Statement) conn.createStatement();

            String workspace_delsql = "delete from workspace where wid=5";
            st.executeUpdate(workspace_delsql);
            System.out.println("jd delete from workspace table.");
            String workspace_insertsql = "insert into workspace(wid, name, description) values(5, 'jd', 'jd comment collect')";
            st.executeUpdate(workspace_insertsql);
            System.out.println("jd insert into workspace table.");

            String project_delsql = "delete from project where wid=5";
            st.executeUpdate(project_delsql);
            System.out.println("jd delete from project table.");
            String project_insertsql = "insert into project(wid, pid, name, description, datasource, schedule, precondition) values(5, 1,'jd', 'get jd comment', 'jd', '0 0 4 * * ?', 'none')";
            st.executeUpdate(project_insertsql );
            System.out.println("jd insert into project table.");

            String task_delsql = "delete from task where wid=5";
            st.executeUpdate(task_delsql);
            System.out.println("jd delete from task table.");
            String task_insertsql = "insert into task(wid, pid, tid, name, description, urlpattern, urlgenerate, isloop) values(5, 1, 1,'jd task', 'get jd comment', 'none', 'false', 'false')";
            st.executeUpdate(task_insertsql );
            System.out.println("jd insert into task table.");

            String data_delsql = "delete from data where wid=5";
            st.executeUpdate(data_delsql);
            System.out.println("jd delete from data table.");
            String data_insertsql = "insert into data(wid, pid, tid, dataid, description, property, rowKey, foreignKey, foreignValue, link, processWay, lcondition) values(5, 1, 1, 1, 'jd comment data', 'comments', 'none', 'none', 'none', 'none', 's', 'none')";
            st.executeUpdate(data_insertsql );
            System.out.println("jd insert into data table.");

            String regex_delsql = "delete from regex where wid=5";
            st.executeUpdate(regex_delsql);
            System.out.println("jd delete from regex table.");
            String regex_insertsql = "insert into regex(wid, pid, tid, tagname, path) values(5, 1, 1,'comments', 'comments0')";
            st.executeUpdate(regex_insertsql );
            System.out.println("jd insert into regex table.");

            conn.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
