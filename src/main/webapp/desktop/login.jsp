<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <script src="/scripts/login.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>

<div class="loginform">
    <table class="logintable">
        <tr>
            <td>Username</td>
            <td><input type="username" id="username"></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" id="password"></td>
        </tr>
        </tr>
        <tr>
            <td><button onclick="login()">Login</button> </td>
        </tr>
    </table>
</div>
</body>
</html>