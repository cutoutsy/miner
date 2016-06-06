package action;

import com.opensymphony.xwork2.ActionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据备份action类
 */
public class BackupAction extends SuperAction {

    //列出当前server的所有数据库
    public String listdatabase() {
        List dbList = new ArrayList();
        Connection conn = (Connection) session.getAttribute("severconn");
        try {
            Statement stmt = conn.createStatement();
            String listDatabases = "SELECT Name FROM Master..SysDatabases ORDER BY Name";
            ResultSet rs = stmt.executeQuery(listDatabases);
            while (rs.next()) {
                //System.out.println(rs.getString("Name"));
                dbList.add(rs.getString("Name"));
            }

            session.setAttribute("dblist", dbList);
            return "dblist_success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "dblist_failed";
        }
    }

    //备份数据库
    public String backup(){
        Connection conn = (Connection) session.getAttribute("severconn");
        ActionContext context = ActionContext.getContext();
        try {
            String dbname = request.getParameter("dbname");
            String base = System.getProperty("user.dir");
            //备份路径会在前面添加一个sql server的默认备份路径﻿C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\Backup
            String path = "\\"+dbname + ".bak";
            String bakSql = "backup database " + dbname + " to disk=? with init";
            //System.out.println(bakSql+"==========");
            PreparedStatement bak = conn.prepareStatement(bakSql);
            bak.setString(1, path);
            bak.execute();
            bak.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "backup_success";
    }

    public String diffbackup(){
        Connection conn = (Connection) session.getAttribute("severconn");
        ActionContext context = ActionContext.getContext();
        try {
            String dbname = request.getParameter("dbname");
            String base = System.getProperty("user.dir");
            //备份路径会在前面添加一个sql server的默认备份路径﻿C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\Backup
            String path = "\\"+dbname + ".bak";
            String bakSql = "backup database " + dbname + " to disk=? with DIFFERENTIAL";
            //System.out.println(bakSql+"==========");
            PreparedStatement bak = conn.prepareStatement(bakSql);
            bak.setString(1, path);
            bak.execute();
            bak.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "backup_success";
    }


    //策略备份
    public String cronbackup(){

        int crontime = Integer.parseInt(request.getParameter("crontime"));
        final String dbname = request.getParameter("dbname");
        System.out.println("定时时间:" + crontime);

        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    // task to run goes here
                    Connection conn = (Connection) session.getAttribute("severconn");
                    ActionContext context = ActionContext.getContext();
                    String base = System.getProperty("user.dir");
                    //备份路径会在前面添加一个sql server的默认备份路径﻿C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\Backup
                    String path = "\\"+dbname + ".bak";
                    String bakSql = "backup database " + dbname + " to disk=? with init";

                    System.out.println(bakSql+"==========");
                    try {
                        PreparedStatement bak = conn.prepareStatement(bakSql);
                        bak.setString(1, path);
                        bak.execute();
                        bak.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            };
            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable, crontime, crontime, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "backup_cron_success";
    }

    //查询数据库
    public String search(){
        Connection conn = (Connection) session.getAttribute("severconn");
        String keyword = request.getParameter("keyword");

        List dbList = new ArrayList();
        try {
            Statement stmt = conn.createStatement();
            String listDatabases = "SELECT Name FROM Master..SysDatabases ORDER BY Name";
            ResultSet rs = stmt.executeQuery(listDatabases);
            while (rs.next()) {
                //System.out.println(rs.getString("Name"));
                dbList.add(rs.getString("Name"));
            }

//            session.setAttribute("dblist", dbList);
//            return "dblist_success";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List reList = new ArrayList();

        for (int i = 0; i < dbList.size(); i++){
            if(dbList.get(i).toString().contains(keyword)){
                reList.add(dbList.get(i));
            }
        }

        session.setAttribute("relist", reList);

        return "search_success";
    }

}
