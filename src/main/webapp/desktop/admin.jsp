<%@ page import="nl.utwente.model.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: marten
  Date: 27-6-19
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/admin.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

</head>
<body>
    <jsp:include page="nav.jsp"/>

    <div class="row">
        <div class="col-md-6">
            <div class="filter">
                <div class="label">Admin or Non-Admin</div>
                <select class="custom-select" id="filter-admin">
                    <option value="admin-and-non-admin">Admin And Non-Admin</option>
                    <option value="only-admin">Admin Only</option>
                    <option value="only-non-admin">Non-Admin Only</option>
                </select>

                <div class="label">Email</div>
                <input class="form-control" type="text" id="filter-email">

                <button onclick="filterUsers()" class="purple-button">Filter Users</button>
                <button onclick="showAllUsers()" class="purple-button">Remove Filter</button>
                <button class="purple-button">Add New User</button>

            </div>

            <table class="table">
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Is Admin</th>
                    </tr>
                </thead>
                <tbody>
                <% for (User user : (List<User>) request.getAttribute("users")) { %>
                    <% if (user.isAdministrator()) { %>
                    <tr data-admin email="<%= user.getEmail() %>" class="user-row" onclick="showUser(<%=user.getUserid()%>)">
                    <% } else { %>
                    <tr data-email="<%= user.getEmail() %>" class="user-row" onclick="showUser(<%=user.getUserid()%>)">
                    <% } %>
                        <td><%= user.getUserid() %></td>
                        <td><%= user.getName() %></td>
                        <td><%= user.getEmail() %></td>
                        <td><%= user.isAdministrator() %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div class="col-md-6">
            <div class="user-data">
                <div class="label">Name</div>
                <div style="display: none" id="user-id"></div>
                <input class="form-control" type="text" id="user-name">

                <div class="label">Email</div>
                <input class="form-control" type="text" id="user-email">

                <div class="label">Administrator <input type="checkbox" id="user-admin"></div>

                <div class="button-container">
                    <button>Edit</button>
                    <button onclick="deleteUser()">Delete</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
