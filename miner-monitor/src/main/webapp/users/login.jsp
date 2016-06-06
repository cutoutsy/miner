<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%><!-- 使用struts2的标签库 -->

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width" />
<title>miner管理－后台登录</title>
<meta name="description" content="SQL Server数据安全性保护系统后台登录页面" />
<meta name="keywords" content="SQL,SQL Server,后台，登录">
<!--[if lt IE 9]>
		<script src="./js/jslib/html5shiv.js"></script>
		<![endif]-->
<!--  <link rel="stylesheet" href="../css/style.css"> 引用导致css路径不对，直接写在html文件里-->
<script type="text/javascript">
	/* 设置登录块随不同分辨率改变大小并居中 */
	function vMiddle() {
		var middleDiv = document.getElementById("loginform");
		var divWidth = middleDiv.offsetWidth;
		var divHeight = divWidth * 0.54;
		middleDiv.style.height = divHeight + "px";
		var bodyHeight = document.body.offsetHeight;
		//设置居中
		middleDiv.style.marginTop = -divHeight / 2 + "px";
		middleDiv.style.marginLeft = -divWidth / 2 + "px";

		//设置标题位置垂直居中
		var titleDiv = document.getElementById("title");
		var titleHeight = titleDiv.offsetHeight;
		titleDiv.style.lineHeight = titleHeight + "px";
	}
</script>
</head>

<style type="text/css">
/*通用样式*/
* {
	margin: 0px;
	padding: 0px;
}

body {
	font-size: 12px;
	font-family: "微软雅黑", Verdana, Helvetica, Sans-Serif;
}

a, a.plain:hover, a.plain:visited, a.plain:link {
	text-decoration: none;
}

li {
	list-style: none;
}

/*设置全背景图样式*/
body {
	background-image: url(../img/loginbg.png);
	/* 背景图垂直、水平居中*/
	background-position: center center;
	background-repeat: no-repeat;
	/* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
	background-attachment: fixed;
	background-size: cover;
	/* 设置背景颜色， 背景图片加载过程中会显示背景色 */
	background-color: #464646;
	width: 100%;
	height: 100%;
}

.loginform {
	width: 35%;
	margin: 0 auto;
	position: absolute;
	top: 50%;
	left: 50%;
	/*margin-left: -250px;
	margin-top: -135px;*/
	background-color: #FFF;
	z-index: 100;
}

.title {
	margin: 0 auto;
	width: 53.6%;
	height: 18.5%;
	background-color: #5b5b5b;
	border-bottom-right-radius: 16px;
	border-bottom-left-radius: 16px;
	/* 浏览器兼容 */
	-webkit-border-bottom-right-radius: : 16px;
	-moz-border-bottom-right-radius: 16px;
	-webkit-border-bottom-left-radius: : 16px;
	-moz-border-bottom-left-radius: 16px;
	font-size: 16px;
	color: #FFF;
	text-align: center;
	line-height: 100%;
}

.error {
	height: 20px;
	line-height: 20px;
	width: 68%;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
	color: red;
}

/*表单样式*/
#login {
	width: 68%;
	height: 73%;
	margin-left: auto;
	margin-right: auto;
}

.item {
	/*height: 56px;*/
	height: 20%;
	line-height: 30px;
	margin-bottom: 10px;
}

.item .item-label {
	float: left;
	width: 20%;
	text-align: left;
}

.item-text {
	float: left;
	width: 68%;
	padding-left: 10px;
	height: 80%;
	border: 1px solid #ccc;
	overflow: hidden;
	background-color: #f8f8f8;
}

.item img {
	margin-left: 3%;
}

.loginbtn {
	display: inline-block;
	width: 70%;
	margin-left: 14%;
	height: 80%;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	background-color: #ffac1c;
	border: 0px;
	color: #FFF;
	font-size: 14px;
	cursor: pointer;
}
</style>

<body onload="vMiddle();">
	<div class="loginform" id="loginform">
		<div class="title" id="title">miner管理系统登录</div>
		<!-- 登录错误显示 -->
		<div class="error"><s:fielderror/><!-- 显示表单验证的出错信息 -->
		</div>

		<form id="login" action="<%=path%>/users/Users_login.action"
			method="post">
			<div class="item">
				<label for="phone" class="item-label">用户名</label> <input
					class="item-text" type="text" name="username" id="phone">
			</div>
			<div class="item">
				<label for="password" class="item-label">密&nbsp;&nbsp;&nbsp;码</label>
				<input class="item-text" type="password" name="password"
					id="password" maxlength="20">
			</div>
			<div class="item">
				<label class="item-label">验证码</label> <input
					class="item-text" type="text" id="code" name="gcode" maxlength="5"
					style="width: 35%"> <img src="/users/getcodepic.jsp" alt="验证码" width="30%"
					height="80%">
			</div>
			<div class="item">
				<input type="submit" value="登录" class="loginbtn">
			</div>
		</form>
	</div>
</body>
</html>