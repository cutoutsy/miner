package service.impl;

import entity.Servers;
import service.DatabaseDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDAOImple implements DatabaseDAO{

	public boolean connSqlServer(Servers s){
        Connection conn = null;
        String url = "jdbc:jtds:sqlserver://"+s.getHostip()+":"+s.getHostport()+"/";
        String userid = s.getServername();
        String password = s.getServerpasswd();
        try{
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(url, userid, password);
//            System.out.println("connected");
            conn.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


	public boolean backUpSingleDatabase(String databaseName) {
		return false;
	}

	public boolean backUpSingleTable(String tableNmae) {
		return false;
	}


	public boolean dataReduction(String name) {
		return false;
	}


    public Connection getSqlServerConnection(Servers s){
        Connection conn = null;
        String url = "jdbc:jtds:sqlserver://"+s.getHostip()+":"+s.getHostport()+"/";
        String userid = s.getServername();
        String password = s.getServerpasswd();
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(url, userid, password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return conn;
    }

    public List listServerAllDatabase(Servers s){

        List reList = new ArrayList();

        try {
            Connection conn = getSqlServerConnection(s);
            //创建SQL命令对象
            Statement stmt = conn.createStatement();
            String listDatabases = "SELECT Name FROM Master..SysDatabases ORDER BY Name";
            ResultSet rs = stmt.executeQuery(listDatabases);
            while (rs.next()) {
                //System.out.println(rs.getString("Name"));

                reList.add(rs.getString("Name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return reList;
    }

}
