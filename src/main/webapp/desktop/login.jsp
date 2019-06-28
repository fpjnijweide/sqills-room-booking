<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/login.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/desktop.css">
</head>
<body>
<%--<jsp:include page="nav.jsp"/>--%>
<div class="loginform">
    <table class="logintable">
        <tr>
            <td>
                <div class="g-signin2" data-width="400" data-height="75" data-onsuccess="onSignIn"></div>
            </td>
        </tr>
        <img class="login-sqills-logo" src="/assets/sqills-logo.svg">
        <%--<tr>--%>
        <%--<td>Username</td>--%>
        <%--<td><input type="username" id="username"></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td>Password</td>--%>
        <%--<td><input type="password" id="password"></td>--%>
        <%--</tr>--%>
        </tr>
        <%--<tr>--%>
        <%--<td><button onclick="login()">Login</button> </td>--%>
        <%--</tr>--%>
    </table>
</div>
<div id="response"></div>
</body>
</html>
