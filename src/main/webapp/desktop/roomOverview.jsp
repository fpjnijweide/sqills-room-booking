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
            <% List<String> roomIDs = (List<String>) request.getAttribute("roomIDs");
               List<String> availableRoomIDs = (List<String>) request.getAttribute("availableRoomIDs"); %>
            <% for (String id : roomIDs) { %>
            <div class="col-md-3">
                <div class="room-card-container">
                    <div class="room-title">Room <%= id %></div>
                    <% if (availableRoomIDs.contains(id)) { %>
                        <div class="room-state free"></div>
                        <a href="/desktop/room/<%= id %>" class="room-card-link">
                            <div class="book-room free">Book this room</div>
                        </a>
                    <% } else { %>
                        <div class="room-state taken"></div>
                        <div class="book-room taken">Book this room</div>
                    <% } %>
                    <%-- Todo: Implement --%>
                    <%--<div class="time-info">Free for the entire day</div>--%>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html>
