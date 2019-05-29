<%@ page import="nl.utwente.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="nl.utwente.model.SpecifiedBooking" %>
<html>
<head>
    <title>Room <%= request.getAttribute("id") %></title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/create-booking.js"></script>
    <script src="/scripts/booking-details.js"></script>
    <script>
        const ROOM_ID = "<%= request.getAttribute("id") %>";
    </script>
</head>
<body>
    <jsp:include page="nav.jsp"/>

    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h2>Bookings for room <%= request.getAttribute("id") %></h2>
                <table class="table">
                    <tr>
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                    </tr>
                    <% List<SpecifiedBooking> bookings = (List< SpecifiedBooking>) request.getAttribute("bookings"); %>
                    <% for (int i = 0; i < bookings.size(); i++) { %>
                    <tr onclick="showDetails(1)">
                        <td><%= bookings.get(i).getDate() %></td>
                        <td><%= bookings.get(i).getStartTime() %></td>
                        <td><%= bookings.get(i).getEndTime() %></td>
                    </tr>
                    <% } %>
                </table>
            </div>

            <div class="col-md-6">
                <h2 style="text-align: right;">Create a booking</h2>
                <form class="align-right">
                    <div class="form-group">
                        <label>Date: </label>
                        <input type="date" id="date" name="date" value="" required>
                    </div>
                    <div class="form-group">
                        <label >Start Time:</label>
                        <input type="time" id="start-time" name="start-time" min="7:00" max="23:00" required>
                    </div>
                    <div class="form-group">
                        <label >End Time:</label>
                        <input type="time" id="end-time" name="end-time" min="7:00" max="23:00" required>
                    </div>
                    <div class="form-group">
                        <label >Email:</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="checkbox">
                        <label><input type="checkbox" id="isPrivate"> Private Meeting</label>
                    </div>


                    <button type="button" onclick="createBooking();" class="btn btn-default">Submit</button>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="success-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="success-title">Sucessfully Created Booking</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Enjoy your meeting!
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">Save changes</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="fail-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header alert alert-danger">
                    <h5 class="modal-title" id="fail-title">Could not create booking</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Something went wrong! Sadly your booking could not be created.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div id="booking-details" class="booking-details hide">
        <button class="close-button" onclick="hideDetails()"><i class="fas fa-times"></i></button>
        <div class="booking-title">Code Review</div>
        <div class="booking-date">Date: 27-08-2000</div>
        <div class="booking-time">Time: 14:00 - 15:00</div>
        <div class="booking-owner">Owner: platon@enschede.com</div>
    </div>
</body>
</html>