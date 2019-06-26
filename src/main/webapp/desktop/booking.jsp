<%@ page import="nl.utwente.model.OutputBooking" %>
<%@ page import="nl.utwente.model.OutputBookingWithParticipants" %>
<%@ page import="nl.utwente.model.User" %>
<% OutputBookingWithParticipants booking = (OutputBookingWithParticipants) request.getAttribute("booking"); %>

<!DOCTYPE html>
<html>
<head>
    <title>Booking <%= booking.getTitle() %></title>
    <jsp:include page="head.jsp"/>
    <script>const bookingID = <%= booking.getBookingid() %></script>
    <script src="/scripts/booking-page.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
</head>

<body onload="disableButtonsBasedOnPermissions()">
    <jsp:include page="nav.jsp"/>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <h1 class="booking-information-header">Booking <%= booking.getTitle() %></h1>
            <div class="booking-information-field">
                <span class="highlight"><%= booking.getStartTime()%></span>
                -
                <span class="highlight"><%= booking.getEndTime()%></span>
                on
                <span class="highlight"><%= booking.getDate()%></span>
            </div>
            <div class="booking-information-field">
                Booked by
                <span class="highlight"><%= booking.getUserName() %></span>
            </div>
            <div class="booking-information-field">
                Room
                <span class="highlight"><%= booking.getRoomName() %></span>
            </div>
            <div class="booking-information-field">
                <% if (booking.getParticipants().size() != 0) {%>
                Participants:
                <% } else { %>
                    No Participants
                <% } %>
                <ul>
                <% for (User user : booking.getParticipants()) {%>
                    <li><%= user.getName() %></li>
                <% } %>
                </ul>
            </div>

            <div class="button-container">
                <button class="booking-information-button disabled" disabled>Edit</button>
                <button onclick='deleteBooking()' class="booking-information-button disabled" disabled>Delete</button>
            </div>
        </div>
        <div class="col-md-4"></div>

        <div id="deletion-success-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content alert alert-success">
                    <div class="modal-header">
                        <h5 class="modal-title">Succesfully Deleted Booking</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-footer">
                        <button onclick="redirectToAllBookings()" type="button" class="btn btn-primary">Go To Bookings</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="deletion-failure-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert alert-danger">
                        <h5 class="modal-title">Could Not Delete Booking</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="deletion-error-container">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>