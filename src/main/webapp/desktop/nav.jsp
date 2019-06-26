<%@ page import="nl.utwente.authentication.AuthenticationFilter" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/desktop/"><img class="menu-logo" src="/assets/sqills-logo.svg"></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/desktop/">Home<span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/desktop/rooms">Rooms</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/desktop/book">Book a Room</a>
            </li>
            <li class="nav-item">

                <% if (session.getAttribute(AuthenticationFilter.principalName)==null){ %>
                <a class="nav-link" href="/desktop/login">Log in</a>
                <% } else { %>
                <script src="/scripts/logout.js"></script>
                <a class="nav-link" onclick="logout()" href="javascript:void(0);">Log out</a>
                <% } %>
            </li>
        </ul>
    </div>
</nav>

<jsp:include page="errorModal.jsp"/>