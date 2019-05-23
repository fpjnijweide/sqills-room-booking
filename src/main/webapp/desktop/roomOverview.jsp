<%@ page import="java.util.List" %>
<html>
<head>
    <title>Room Overview</title>
    <jsp:include page="head.jsp"/>
</head>
<body>
    <jsp:include page="nav.jsp"/>
    <div class="container">
        <div class="row">
            <div class="col-md-3"></div>

            <div class="col-md-6">
                <ul>
                <% List<String> roomIDs = (List<String>) request.getAttribute("roomIDs"); %>
                <% for (String id : roomIDs) { %>
                    <li><a href="/desktop/room/<%= id %>">Room <%= id %></a></li>
                <% } %>
                </ul>
            </div>

            <div class="col-md-3"></div>
        </div>
    </div>
</body>
</html>
