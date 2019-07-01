<%@ page import="nl.utwente.authentication.AuthenticationFilter" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/sqillsRoomBooking/desktop/"><img class="menu-logo" src="/sqillsRoomBooking/assets/sqills-logo.svg"></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/sqillsRoomBooking/desktop/rooms">Rooms</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/sqillsRoomBooking/desktop/book">Book a Room</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/sqillsRoomBooking/desktop/bookings">Bookings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/sqillsRoomBooking/desktop/admin">Admin</a>
            </li>
            <li class="nav-item">

                <% if (session.getAttribute(AuthenticationFilter.principalName)==null){ %>
                <a class="nav-link" href="desktop/login">Log in</a>
                <% } else { %>
                <script src="/sqillsRoomBooking/scripts/logout.js"></script>
                <a class="nav-link" onclick="logout()" href="javascript:void(0);">Log out</a>
                <% } %>
            </li>
        </ul>
    </div>
</nav>

<jsp:include page="errorModal.jsp"/>