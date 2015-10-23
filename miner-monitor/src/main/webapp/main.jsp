<%--
  Created by IntelliJ IDEA.
  User: kp
  Date: 15-10-2
  Time: 下午4:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<html>
  <head>
    <title>仇富者联盟</title>
  </head>
      <body>
          <h3>文件配置</h3>
      <form action="index.jsp" method="post">

          <font size="6" color="black">workspace:</font>
          <br>
                <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
                <input type="text"  name="wwid">
                <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name:</font>
                <input type="text"  name="wname">
                <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;description:</font>
                <input type="text"  name="wdescription">
          <br>
          <br>
              <font size="6" color="black">project:</font>
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
              <input type="text" name="pwid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
              <input type="text" name="ppid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name:</font>
              <input type="text" name="pname">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;description:</font>
              <input type="text" name="pdescription">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;datasource:</font>
              <input type="text" name="pdatasource">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;schedule:</font>
              <input type="text" name="pschedule">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;precondition:</font>
              <input type="text" name="pprecondition">
          <br>
          <br>
              <font size="6" color="black">task:</font>
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
              <input type="text" name="twid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
              <input type="text" name="tpid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;taskid:</font>
              <input type="text" name="ttid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;description:</font>
              <input type="text" name="tdescription">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;urlpattern:</font>
              <input type="text" name="turlpattern">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;urlgenerate:</font>
              <input type="text" name="turlgenerate">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;isloop:</font>
              <input type="text" name="tisloop">
          <br><br>
              <font size="6" color="black">data:</font>
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
              <input type="text" name="dwid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
              <input type="text" name="dpid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
              <input type="text" name="dtid">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;dataid:</font>
              <input type="text" name="ddataid">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;description:</font>
              <input type="text" name="ddescription">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;property:</font>
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name1:</font>
              <input type="text" name="dname1">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name2:</font>
              <input type="text" name="dname2">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name3:</font>
              <input type="text" name="dname3">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name4:</font>
              <input type="text" name="dname4">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name5:</font>
              <input type="text" name="dname5">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name6:</font>
              <input type="text" name="dname6">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name7:</font>
              <input type="text" name="dname7">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name8:</font>
              <input type="text" name="dname8">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name9:</font>
              <input type="text" name="dname9">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;name10:</font>
              <input type="text" name="dname10">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;rowkey:</font>
              <input type="text" name="drowkey">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;foreignkey:</font>
              <input type="text" name="dforeignkey">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;foreignvalue:</font>
              <input type="text" name="dforeignvalue">
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;link:</font>
              <input type="text" name="dlink">
          <br>
              <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;processway:</font>
              <input type="text" name="dprocessway">
          <br>
          <br>
          <font size="6" color="black">regex:</font>
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
          <input type="text" name="rwid5">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
          <input type="text" name="rpid5">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
          <input type="text" name="rtid5">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tagname:</font>
          <input type="text" name="rtagname5">
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;path:</font>
          <input type="text" name="rpath5">
          <br>
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
          <input type="text" name="rwid1">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
          <input type="text" name="rpid1">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
          <input type="text" name="rtid1">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tagname:</font>
          <input type="text" name="rtagname1">
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;path:</font>
          <input type="text" name="rpath1">
          <br>
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
          <input type="text" name="rwid2">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
          <input type="text" name="rpid2">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
          <input type="text" name="rtid2">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tagname:</font>
          <input type="text" name="rtagname2">
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;path:</font>
          <input type="text" name="rpath2">
          <br>
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
          <input type="text" name="rwid3">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
          <input type="text" name="rpid3">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
          <input type="text" name="rtid3">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tagname:</font>
          <input type="text" name="rtagname3">
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;path:</font>
          <input type="text" name="rpath3">
          <br>
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;wid:</font>
          <input type="text" name="rwid4">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;pid:</font>
          <input type="text" name="rpid4">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tid:</font>
          <input type="text" name="rtid4">
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;tagname:</font>
          <input type="text" name="rtagname4">
          <br>
          <font size="5" color="black">&nbsp; &nbsp; &nbsp;&nbsp;path:</font>
          <input type="text" name="rpath4">
          <br>
              <input type="submit",value="提交">
        </form>
      </body>
</html>
