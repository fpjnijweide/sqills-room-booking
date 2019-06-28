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
                <div class="room-title">Room <%= id %>
                </div>
                <% if (availableRoomIDs.contains(id)) { %>
                <div class="room-state free"></div>

                <% } else { %>
                <div class="room-state taken"></div>
                <% } %>
                <a href="/desktop/room/<%= id %>" class="room-card-link">
                    <div class="book-room free">View this room</div>
                </a>
                <%-- Todo: Implement --%>
                <%--<div class="time-info">Free for the entire day</div>--%>
            </div>
        </div>
        <% } %>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>
