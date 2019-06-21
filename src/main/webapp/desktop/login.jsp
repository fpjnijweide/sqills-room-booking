<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/login.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id" content="47026751328-n650qv0b0v1qjmmnk16vddsae05rqp4v.apps.googleusercontent.com">
</head>
<body>
<jsp:include page="nav.jsp"/>
<div class="loginform">
    <table class="logintable">
<tr>
    <td><div class="g-signin2" data-onsuccess="onSignIn"></div></td>
</tr>
<%--        <tr>--%>
<%--            <td>Username</td>--%>
<%--            <td><input type="username" id="username"></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td>Password</td>--%>
<%--            <td><input type="password" id="password"></td>--%>
<%--        </tr>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td><button onclick="login()">Login</button> </td>--%>
<%--        </tr>--%>
    </table>
</div>
<div id="response"></div>
</body>
</html>
