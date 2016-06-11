<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<meta http-equiv="content-type" content="text/html;charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="../css/default.css" />
<style type="text/css">
* {
    background: none repeat scroll 0 0 transparent;
    border: 1 none;
    margin: 0;
    padding: 0;
    vertical-align: baseline;
	font-family:微软雅黑;
	overflow:hidden;
}
#navi{
	width:100%;
	position:relative;
	word-wrap:break-word;
	border-bottom:1px solid #065FB9;
	margin:0;
	padding:0;
	height:40px;
	line-height:40px;
	vertical-align:middle;
    background-image: -moz-linear-gradient(top,#EBEBEB, #BFBFBF);
    background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #EBEBEB),color-stop(1, 
#BFBFBF));
}
#naviDiv{
	font-size:14px;
	color:#333;
	padding-left:10px;
}
#tips{
	margin-top:10px;
	width:100%;
	height:40px;
}
#buttonGroup{
	padding-left:10px;
	float:left;
	height:35px;
}
.button{
	margin-top:20px;
	padding-left:10px;
	padding-right:10px;
	font-size:14px;
	width:70px;
	height:30px;
	line-height:30px;
	vertical-align:middle;
	text-align:center;
	cursor:pointer;
    border-color: #77D1F6;
    border-width: 1px;
    border-style: solid;
    border-radius: 6px 6px;
    -moz-box-shadow: 2px 2px 4px #282828;
    -webkit-box-shadow: 2px 2px 4px #282828;
    background-image: -moz-linear-gradient(top,#EBEBEB, #BFBFBF);
    background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #EBEBEB),color-stop(1, #BFBFBF));
}
#mainContainer{
	padding-left:10px;
	padding-right:10px;
	text-align:left;
	width:98%;
	font-size:16px;
}
</style>
<body>
<script type="text/javascript" src="../js/Calendar3.js"></script>

<div id="navi">
	<div id='naviDiv'>
		<span><img src="../images/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;任务列表<span>&nbsp;
		<span><img src="../images/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;<a href="#">修改任务</a><span>&nbsp;
	</div>
</div>
<div id="tips">
</div>
<div id="mainContainer">
<strong>修改任务</strong>
<br>
<br>

<form name="modifyForm" action="<%=path%>/task/Task_save.action" method="post">
<table width="400" >
  <tr>
    <td width="30%">id：</td>
    <td><input type="text" name="id" value='<s:property value="#session.modify_task.id"/>'  readonly="readonly"/></td>
  </tr>
  <tr>
    <td width="30%">wid：</td>
    <td><input type="text" name="wid" value='<s:property value="#session.modify_task.wid"/>'/></td>
  </tr>
	<tr>
		<td>pid：</td>
		<td><input type="text" name="pid" value='<s:property value="#session.modify_task.pid"/>'/></td>
	</tr>
	<tr>
		<td>tid：</td>
		<td><input type="text" name="tid" value='<s:property value="#session.modify_task.tid"/>'/></td>
	</tr>
  <tr>
    <td>name：</td>
      <td><input type="text" name="tname" value='<s:property value="#session.modify_task.name"/>'/></td>
  </tr>
    <td>description：</td>
    <td><input type="text" name="description" value='<s:property value="#session.modify_task.description"/>'/></td>
  </tr>
    </tr>
	    <td>urlpattern：</td>
	    <td><input type="text" name="urlpattern" value='<s:property value="#session.modify_task.urlpattern"/>'/></td>
	</tr>
	</tr>
	    <td>urlgenerate：</td>
	    <td><input type="text" name="urlgenerate" value='<s:property value="#session.modify_task.urlgenerate"/>'/></td>
	</tr>
	</tr>
	    <td>isloop：</td>
	    <td><input type="text" name="isloop" value='<s:property value="#session.modify_task.isloop"/>'/></td>
	</tr>
    </tr>
    <td>proxy_open：</td>
    <td><input type="text" name="proxy_open" value='<s:property value="#session.modify_task.proxy_open"/>'/></td>
    </tr>

  <tr>
    <td colspan="2" align="center"><input class="button" type="submit" value="修改"></td>
  </tr>
</table>
</form>


</div>
</body>
</html>