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

<div id="navi">
	<div id='naviDiv'>
		<span><img src="../img/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;数据备份<span>&nbsp;</span>
		<span><img src="../img/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;<a href="<%=path%>/backup/database_search.jsp">查询备份</a><span>&nbsp;</span>
	</div>
</div>
<div id="tips">
</div>
<div id="mainContainer">

<strong>查询备份</strong>
<br>
<br>

<form name="connectForm" action="" method="post">
<table width="400" >
  <tr>
    <td width="30%">输入关键字：</td>
    <td><input type="text" name="keyword"/></td>
  </tr>
	<tr>
		<td><input class="button" onclick="search();" value="查询"></td>
	</tr>
</table>
</form>

	<table class="default" width="100%">
		<col width="20%">
		<col width="30%">
		<col width="30%">
		<tr class="title">
			<td>数据库名称</td>

			<td>直接备份</td>
			<td>策略备份</td>
		</tr>

		<!-- 遍历开始 -->

		<s:iterator value="#session.relist" var="relist">
			<tr class="list">
				<td><s:property value="#relist"/></td>
				<td><a href="<%=path%>/backup/Backup_backup.action?dbname=<s:property value='#dblist'/>" onclick="javascript: return confirm('确定备份吗？');">备份</a></td>
				<td><a href="<%=path%>/backup/backup_cron.jsp?dbname=<s:property value='#relist'/>">策略备份</a></td>
			</tr>
		</s:iterator>
		<!-- 遍历结束 -->
	</table>

</div>
</body>
<script type="text/javascript">
	function search(){
		document.connectForm.action="<%=path%>/backup/Backup_search.action";
		document.connectForm.submit();
	}

</script>

</html>