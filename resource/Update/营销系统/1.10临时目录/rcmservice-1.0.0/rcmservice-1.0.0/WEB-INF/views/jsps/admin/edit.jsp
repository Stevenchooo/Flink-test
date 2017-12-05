<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Person</title>
</head>
<body>
	person edit
	<div id="main">
		<div>
			<form name="mainForm" action="<%=request.getContextPath()%>/rule/save" method="post">
				<div>
					<span>姓名</span><input type="text" id="name" name="name">
				</div>
				<div>
					<span>性别</span>
					<select id="sex" name="sex">
						<option value="male">男</option>
						<option value="female">女</option>
					</select>
				</div>
				<div>
					<span>年龄</span><input type="text" id="age" name="age">
				</div>
				<div>
					<span>工作</span><input type="text" id="job" name="job">
				</div>
				<div>
					<span>地址</span><input type="text" id="address" name="address">
				</div>
				<div>
					<input type="submit" id="btnPass" value="提交">
				</div>
			</form>
		</div>
	</div>
</body>
</html>