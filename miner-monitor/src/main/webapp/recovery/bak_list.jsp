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
		<span><img src="../img/arror.gif" width="7" height="11" border="0" alt=""></span>&nbsp;<a href="<%=path%>/database/database_save.jsp">选择数据库连接</a><span>&nbsp;</span>
	</div>
</div>
<div id="tips">

	<%
        //session中保存的时map, 直接==null不行
		if( ((Map)session.getAttribute("bakfileinfos")).size() == 0 ) {
    %>
	<h3>&nbsp;&nbsp;&nbsp;&nbsp;没有保存的备份文件，请先去备份数据库.</h3>
	<%}
	%>

</div>
<div id="mainContainer">

	<table class="default" width="100%">
		<col width="20%">
		<col width="30%">
		<col width="30%">
		<tr class="title">
			<td>备份文件</td>
			<td>备份时间</td>
			<td>还原</td>
			<td>删除</td>
		</tr>

		<!-- 遍历开始 -->

		<s:iterator value="#session.bakfileinfos"  id="bakfileinfo" status="st">
			<tr class="list">
				<td><s:property value="key"/></td>
                <td><s:property value="value"/></td>
                <td><a href="<%=path%>/recovery/Recovery_bakrecovery.action?filename=<s:property value='key'/>" onclick="javascript: return confirm('确定还原吗？');">还原</a></td>
                <td><a href="<%=path%>/recovery/Recovery_bakdelete.action?filename=<s:property value='key'/>" onclick="javascript: return confirm('确定删除吗？');">删除</a></td>
			</tr>
		</s:iterator>
		<!-- 遍历结束 -->
	</table>
</div>
</body>
</html>