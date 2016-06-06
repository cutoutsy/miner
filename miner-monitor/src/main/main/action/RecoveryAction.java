package action;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import utils.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据恢复action类
 */
public class RecoveryAction extends SuperAction {
    //列出当前所有的备份
    public String listback() {
        String bakPath = "C:\\Program Files\\Microsoft SQL Server\\MSSQL11.MSSQLSERVER\\MSSQL\\Backup";
        List<String> bakList = IOUtil.getFilesNameStr(bakPath);

        Map<String, String> bakFileInfo = new HashMap<String, String>();
        System.out.println(bakList.size());
        for(int i = 0; i < bakList.size(); i++){
            String fileName = "";
            String baklist = bakList.get(i).toString().split("\\$")[0];
            if(File.separator.equals("\\")){
                fileName = baklist.split("\\\\")[baklist.split("\\\\").length-1];
            }else{
                fileName = baklist.split("/")[baklist.split("/").length-1];
            }

            bakFileInfo.put(fileName,  bakList.get(i).toString().split("\\$")[1]);
        }

        session.setAttribute("bakfileinfos", bakFileInfo);

        return "baklist_success";
    }

    //数据库还原
    public String bakrecovery(){
        Connection conn = (Connection) session.getAttribute("severconn");
        ActionContext context = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
        String webtruepath = "C:\\Program Files\\Microsoft SQL Server\\MSSQL11.MSSQLSERVER\\MSSQL\\Backup";
        String name = request.getParameter("filename");

        String dbname = name.split("\\.")[0];
        try {

//            String path = webtruepath + "\\" + name; // name文件名
            //还原路径会在前面添加一个sql server的默认备份路径﻿C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\Backup
            String path = "\\"+dbname + ".bak";
            System.out.println(path+"==="+dbname);
            String recoverySql = "ALTER DATABASE " +dbname+ " SET   ONLINE   WITH   ROLLBACK   IMMEDIATE";// 恢复所有连接

            PreparedStatement ps = conn.prepareStatement(recoverySql);
            CallableStatement cs = conn.prepareCall("{call killrestore(?,?)}");
            cs.setString(1, dbname); // 数据库名
            cs.setString(2, path); // 已备份数据库所在路径
            cs.execute(); // 还原数据库
            ps.execute(); // 恢复数据库连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "recovery_success";
    }

    //删除备份文件
    public String bakdelete(){
        Connection conn = (Connection) session.getAttribute("severconn");
        ActionContext context = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
        String webtruepath = "C:\\Program Files\\Microsoft SQL Server\\MSSQL11.MSSQLSERVER\\MSSQL\\Backup";
        String name = request.getParameter("filename");

        String dbname = name.split("\\.")[0];
        try {
//            String path = webtruepath + "\\" + name; // name文件名
            //还原路径会在前面添加一个sql server的默认备份路径﻿C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\Backup
            String path = webtruepath+"/"+dbname + ".bak";

            //删除文件
            File file = new File(path);
            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "delete_success";
    }

}
