<%--
  Created by IntelliJ IDEA.
  User: kp
  Date: 15-10-4
  Time: 下午3:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="com.mysql.jdbc.Driver"%>
<html>
<head>
    <title></title>
</head>
<body>
    <%!
        public String insertsql(Map<String,String> map,String table){
            String keysql = "";
            String valuesql = "";
            String sql = null;
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                String key = entry.getKey().substring(1);
                String value = entry.getValue();
                keysql = keysql+","+key;
                valuesql = valuesql+","+value;
            }
            keysql = keysql.substring(1);
            valuesql = valuesql.substring(1);
            sql = "insert into "+table+"("+keysql+") VALUE ("+valuesql+")";
            return sql;
        }

        public String insertsql1(Map<String,String> map){
            int sum = 0;
            String keysql = "";
            boolean is ;
            String valuesql = "";
            String sql = null;
            String property = "";
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                String key = entry.getKey().substring(1);
                String value = entry.getValue();
                if(!key.contains("name")){
                keysql = keysql+","+key;
                valuesql = valuesql+","+value;
                }else {
                    if(!value.equals("")){
                    property = property+"+"+value;
                    }
                }
            }
            property = property.substring(1);
            keysql = keysql.substring(1);
            valuesql = valuesql.substring(1);
            sql = "insert into data("+keysql+",property) VALUE ("+valuesql+",'"+property+"')";
            System.out.print(sql);
            return sql;
        }
    %>
    <%!
        public static Statement statement = null;
        String SQL= null;
        String property = null;
        public static final Properties info = new Properties();
        public static final String url ="jdbc:mysql://127.0.0.1/storm?useUnicode=true&characterEncoding=utf8";
    %>
    <%@include file="getParam.jsp"%>
    <%
    try{
        %>
    <%
            info.put("user","root");
            info.put("password", "root");
            //String string = request.getParameter("wid");
            Driver driver = new Driver();
            Connection con = driver.connect(url, info);
            statement=con.createStatement();
            SQL = insertsql(wmap,"workspace");
            statement.execute(SQL);
            SQL = insertsql(pmap,"project");
            statement.execute(SQL);
            SQL = insertsql(tmap,"task");
            statement.execute(SQL);
            SQL = insertsql(rmap,"regex");
            statement.execute(SQL);
            SQL = insertsql1(dmap);
            statement.execute(SQL);
            %>
    <h2>你已经成功提交</h2>
    <%
        }catch (Exception e){
    %>
    <h2>提交失败</h2>
    <%
                e.printStackTrace();
            }
    %>
</body>
</html>
