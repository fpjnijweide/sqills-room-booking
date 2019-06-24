<%@ page import="nl.utwente.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="nl.utwente.model.OutputBooking" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/specific-room.css">
    <title>Room <%= request.getAttribute("id") %></title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/create-booking.js"></script>
    <script src="/scripts/booking-details.js"></script>
    <script src="/scripts/specific-room.js"></script>
    <script>
        const ROOM_ID = "<%= request.getAttribute("id") %>";
    </script>
</head>
<body onload="positionTimeBars()">
    <jsp:include page="nav.jsp"/>
    <div id="footer">
        <div class="bar">
            <div class="timestamps">
                <div class="timestamp time-0" value="0"></div>
                <div class="timestamp time-3" value="3"></div>
                <div class="timestamp time-6" value="6"></div>
                <div class="timestamp time-9" value="9"></div>
                <div class="timestamp time-12" value="12"></div>
                <div class="timestamp time-15" value="15"></div>
                <div class="timestamp time-18" value="18"></div>
                <div class="timestamp time-21" value="21"></div>
                <div class="timestamp time-24" value="24"></div>
            </div>
        </div>

    </div>
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h2>Bookings for room <%= request.getAttribute("id") %></h2>
                <table class="table" id="bookings-table">
                    <tr>
                        <th>Title</th>
                        <th>Name</th>
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                    </tr>
                    <% List<OutputBooking> bookings = (List<OutputBooking>) request.getAttribute("bookings"); %>
                    <% for (int i = 0; i < bookings.size(); i++) { %>
                    <script>
                        if (checkIfBookingToday("<%= bookings.get(i).getDate() %>")){
                            addBooking("<%= bookings.get(i).getStartTime() %>", "<%= bookings.get(i).getEndTime() %>");
                        }
                    </script>
                    <tr class="tablerow" onclick="window.location.href='/desktop/booking/<%= bookings.get(i).getBookingid()%>'">
                        <td><%= bookings.get(i).getTitle() %></td>
                        <td><%= bookings.get(i).getUserName() %></td>
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
                        <label>Title: </label>
                        <input type="text" id="title" name="title" value="" required>
                    </div>
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
                <div class="modal-body" id="fail-modal-text">
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
        <div class="booking-title" id="selected-booking-title">Code Review</div>
        <div class="booking-date" id="selected-booking-date">Date: 27-08-2000</div>
        <div class="booking-time" id="selected-booking-time">Time: 14:00 - 15:00</div>
        <div class="booking-owner" id="selected-booking-owner">Owner: platon@enschede.com</div>
    </div>

</body>
</html>