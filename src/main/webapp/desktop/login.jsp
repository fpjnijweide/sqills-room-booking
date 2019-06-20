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

        <div class="g-signin2" data-onsuccess="onSignIn"></div>


    </table>
</div>
<div id="response"></div>
</body>
</html>