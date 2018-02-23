<%--
  Created by IntelliJ IDEA.
  User: hq
  Date: 17-10-17
  Time: 下午4:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
     String path = request.getContextPath();
     String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        body
        {
            text-align: center;
            font-size: 12px;
        }
        *
        {
            margin: 0 auto;
        }
        .login
        {
            background: none repeat scroll 0 0 #FFFFFF;
            border: 1px solid #B0D6F4;
            height: 375px;
            left: 50%;
            margin: -188px 0 0 -335px;
            position: absolute;
            top: 50%;
            width: 670px;
        }
        
        .login .title
        {
            background: none repeat scroll 0 0 #4DA9EB;
            color: #FFFFFF;
            font: 20px/48px Microsoft YaHei;
            margin-bottom: 70px;
        }
        table
        {
            border-collapse: collapse;
            border-spacing: 0;
            text-align: left;
        }
        .login .input
        {
            border: 1px solid #95C6F2;
            height: 30px;
            margin-bottom: 5px;
            padding: 4px;
            width: 240px;
        }
        .login input
        {
            vertical-align: middle;
        }
        
        .login .btn
        {
            background-color: #4DA9EB;
            border: 0 none;
            border-radius: 3px;
            color: #FFFFFF;
            cursor: pointer;
            font: 16px Microsoft YaHei;
            height: 35px;
            margin-bottom: 5px;
            width: 240px;
        }
    </style>
</head>
<body>
	<div class="page">
      <div class="login">
          <div class="title">
              软件注册成功
          </div>
          <table>
              <tr>
                  <td>
                      申请码：
                  </td>
                  <td>
                      <input type="text" class="input" value="${applyId}" readonly="readonly" style="color:#999" />
                  </td>
              </tr>
              <tr>
                  <td>
                      授权开始日期：
                  </td>
                  <td>
                      <input type="text" class="input" value="${start}" name="registerCode" readonly="readonly"/>
                  </td>
              </tr>
              <tr>
                  <td>
                      授权结束日期：
                  </td>
                  <td>
                      <input type="text" class="input" value="${end}" name="grantCode" readonly="readonly"/>
                  </td>
              </tr>
              <tr>
                  <th>
                  </th>
                  <td>
                      <input type="submit" value="开始登录" class="btn" onclick="javascript: window.location.href='<%=basePath%>'"/>
                  </td>
              </tr>
          </table>
      </div>
  </div>
</body>
</html>
