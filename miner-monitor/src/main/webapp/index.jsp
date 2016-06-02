<%--
  Created by IntelliJ IDEA.
  User: kp
  Date: 15-10-4
  Time: 下午3:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Map"%>
<%@ page import="com.mysql.jdbc.Driver"%>
<%@ page import="redis.clients.jedis.Jedis" %>
<%@ page import="miner.utils.StaticValue" %>
<%@ page import="miner.utils.RedisUtil" %>
<%@ page import="miner.utils.PlatformParas" %>
<html>
<head>
    <title>task summit</title>
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
                valuesql = valuesql+","+"'"+value+"'";
            }
            keysql = keysql.substring(1);
            valuesql = valuesql.substring(1);
            sql = "insert into "+table+"("+keysql+") VALUE ("+valuesql+")";
            return sql;
        }

        public String insertsqlreg(Map<String,String> map,String table){
            String keysql = "";
            String valuesql = "";
            String sql = null;
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                String key = entry.getKey();
                key = key.substring(1,key.length()-1);
                String value = entry.getValue();
                if(value.equals("")){
                    return  null;
                }
                keysql = keysql+","+key;
                valuesql = valuesql+","+"'"+value+"'";
            }
            keysql = keysql.substring(1);
            valuesql = valuesql.substring(1);
            sql = "insert into "+table+"("+keysql+") VALUE ("+valuesql+")";
            return sql;
        }

        public String insertsqldata(Map<String,String> map){
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
                valuesql = valuesql+","+"'"+value+"'";
                }else {
                    if(!value.equals("")){
                    property = property+"$"+value;
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

        public void insertRedis(String wid,String pid){
            RedisUtil ru = new RedisUtil();
            Jedis redis = ru.getJedisInstance();

            String state = "die";
            String projectExecuteNum = "0";
            redis.hset("project_state",wid+"-"+pid,state);
            redis.hset("project_executenum",wid+"-"+pid,projectExecuteNum);
            redis.hset("project_cronstate",wid+"-"+pid,"3");
        }
    %>
    <%!
        public static Statement statement = null;
        String SQL= null;
        String property = null;
        public static final Properties info = new Properties();
        public static final String url ="jdbc:mysql://"+ PlatformParas.mysql_host+":"+PlatformParas.mysql_port+"/"+PlatformParas.mysql_database+"?useUnicode=true&characterEncoding=utf8";
    %>
    <%@include file="getParam.jsp"%>
    <%
    try{
        %>
    <%
            info.put("user",PlatformParas.mysql_user);
            info.put("password", PlatformParas.mysql_password);
            String wid = new String(request.getParameter("pwid").getBytes("ISO-8859-1"),"UTF-8");
            String pid = new String(request.getParameter("ppid").getBytes("ISO-8859-1"),"UTF-8");
            insertRedis(wid,pid);
            Driver driver = new Driver();
            Connection con = driver.connect(url, info);
            statement=con.createStatement();
            SQL = insertsql(wmap, "workspace");
            statement.execute(SQL);
            SQL = insertsql(pmap,"project");
            statement.execute(SQL);
            SQL = insertsql(tmap,"task");
            statement.execute(SQL);
        for(Map<String,String> map : list){
            SQL = insertsqlreg(map, "regex");
            if(SQL != null){
            statement.execute(SQL);
            }
        }
            SQL = insertsqldata(dmap);
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
