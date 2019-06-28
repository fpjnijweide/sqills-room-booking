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
    <script>const EMAIL = "<%= request.getAttribute("email") %>"</script>
    <script src="/scripts/booking-page.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

</head>

<body onload="disableButtonsBasedOnPermissions()">
    <jsp:include page="nav.jsp"/>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <h1 class="booking-information-header"><%= booking.getTitle() %></h1>
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
                    <li>
                        <%= user.getName() %>
                        <span onclick="removeParticipant(<%=booking.getBookingid()%>, <%=user.getUserid()%>)" class="fa-span remove-participant">
                            <i class="fa fa-close"></i>
                        </span>
                    </li>
                <% } %>
                </ul>
            </div>

            <div class="button-container">
                <button onclick="openEditPopUp()" class="booking-information-button disabled" disabled>Edit</button>
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

        <div id="edit-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Booking</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div>
                            <label>Title: </label>
                            <input type="text" id="edit-booking-title" value="<%= booking.getTitle() %>">
                        </div>

                        <div>
                            <label>Room Name: </label>
                            <input type="text" id="edit-booking-room-name" value="<%= booking.getRoomName() %>">
                        </div>

                        <div>
                            <label>Date: </label>
                            <input type="date" id="edit-booking-date" value="<%= booking.getDate() %>">
                        </div>

                        <div>
                            <label>Start Time: </label>
                            <input type="time" id="edit-booking-start-time" value="<%= booking.getStartTime() %>">
                        </div>

                        <div>
                            <label>End Time: </label>
                            <input type="time" id="edit-booking-end-time" value="<%= booking.getEndTime() %>">
                        </div>

                        <div>
                            <label>Private Meeting: </label>
                            <input type="checkbox" id="edit-booking-is-private">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button onclick="editBooking()" type="button" class="btn btn-primary">Edit Booking</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="edit-failure-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert alert-danger">
                        <h5 class="modal-title">Could Not Edit Booking Booking</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="edit-error-container">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="edit-success-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content alert alert-success">
                    <div class="modal-header">
                        <h5 class="modal-title">Successfully Edited Booking</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-footer">
                        <button onclick="location.reload()" type="button" class="btn btn-primary">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="participant-success-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content alert alert-success">
                    <div class="modal-header">
                        <h5 class="modal-title">Successfully Removed Participant</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-footer">
                        <button onclick="location.reload()" type="button" class="btn btn-primary">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="participant-failure-modal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content alert alert-danger">
                    <div class="modal-header">
                        <h5 class="modal-title">Could Not Remove Participant</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="participant-error-container">

                    </div>
                    <div class="modal-footer">
                        <button onclick="location.reload()" type="button" class="btn btn-primary">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>