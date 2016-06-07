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
		border: 0 none;
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
		float:left;
		margin-right:10px;
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
		text-align:center;
		width:98%;
		font-size:12px;
	}
</style>
<body>
<div id="navi">
	<div id='naviDiv'>
		<span><img src="../img/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;数据库管理<span>&nbsp;</span>
		<span><img src="../img/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;<a href="<%=path%>/database/database_connect_success.jsp">连接中数据库</a><span>&nbsp;</span>
	</div>
</div>
<div id="tips">

    <%
        if(session.getAttribute("serverconncted") == null){ %>

        <h3>&nbsp;&nbsp;&nbsp;&nbsp;没有已连接数据库,请先去连接.</h3>
    <%}
    %>

	<!--
	<div id="buttonGroup">
		<div class="button" onmouseout="this.style.backgroundColor='';this.style.fontWeight='normal'" onmouseover="this.style.backgroundColor='#77D1F6';this.style.fontWeight='bold'">
			<a href="<%=path%>/students/Students_add.jsp">添加学生</a>
		</div>
		<div class="button" onmouseout="this.style.backgroundColor='';this.style.fontWeight='normal'" onmouseover="this.style.backgroundColor='#77D1F6';this.style.fontWeight='bold'">
			<a>查找学生</a>
		</div>
	</div>
	-->
</div>
<div id="mainContainer">
	<!-- 从session中获取学生集合 -->

	<table class="default" width="100%">
		<col width="25%">
		<col width="25%">
		<col width="25%">
		<col width="25%">
		<tr class="title">
			<td>id</td>
			<td>wid</td>
			<td>name</td>
			<td>desc</td>
		</tr>

		<!-- 遍历开始 -->

		<s:iterator value="#session.workspace_list" var="workspace_list">
			<tr class="list">
				<td><s:property value="#workspace_list.id"/></td>
				<td><s:property value="#workspace_list.wid"/></td>
				<td><s:property value="#workspace_list.name"/></td>
				<td><s:property value="#workspace_list.description"/></td>
			</tr>
		</s:iterator>
		<!-- 遍历结束 -->
	</table>
</div>
</body>
</html>