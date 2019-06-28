<%@ page import="nl.utwente.model.OutputBooking" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/specific-room.css">
    <link rel="stylesheet" type="text/css" href="/css/book.css">
    <title>Room <%= request.getAttribute("id") %>
    </title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/create-booking.js"></script>
    <script src="/scripts/booking-details.js"></script>
    <script src="/scripts/specific-room.js"></script>
    <script src="/scripts/make-booking-page.js"></script>

    <script>
        const ROOM_ID = "<%= request.getAttribute("id") %>";
    </script>
</head>
<body onload="positionTimeBars()">
<jsp:include page="nav.jsp"/>
<div id="footer">
    <div class="footer-title">Bookings overview for today</div>
    <div class="bar">
        <div class="timestamps">
            <div class="timestamp time-0" value="0"><p>00:00</p></div>
            <div class="timestamp time-3" value="3"><p>03:00</p></div>
            <div class="timestamp time-6" value="6"><p>06:00</p></div>
            <div class="timestamp time-9" value="9"><p>09:00</p></div>
            <div class="timestamp time-12" value="12"><p>12:00</p></div>
            <div class="timestamp time-15" value="15"><p>15:00</p></div>
            <div class="timestamp time-18" value="18"><p>18:00</p></div>
            <div class="timestamp time-21" value="21"><p>21:00</p></div>
            <div class="timestamp time-24" value="24"><p>23:59</p></div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <h2>Bookings for room <%= request.getAttribute("id") %>
            </h2>
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
                    if (checkIfBookingToday("<%= bookings.get(i).getDate() %>")) {
                        addBooking("<%= bookings.get(i).getStartTime() %>", "<%= bookings.get(i).getEndTime() %>");
                    }
                </script>
                <% if (!bookings.get(i).getUserName().equals("PRIVATE")) { %>
                <tr class="tablerow"
                    onclick="window.location.href='/desktop/booking/<%= bookings.get(i).getBookingid()%>'">
                        <% } else { %>
                <tr>
                    <% } %>
                    <td><%= bookings.get(i).getTitle() %>
                    </td>
                    <td><%= bookings.get(i).getUserName() %>
                    </td>
                    <td><%= bookings.get(i).getDate() %>
                    </td>
                    <td><%= bookings.get(i).getStartTime() %>
                    </td>
                    <td><%= bookings.get(i).getEndTime() %>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>

        <div class="col-md-6">
            <!-- Button trigger modal -->
            <button type="button" onclick="setRoom(<%= request.getAttribute("id") %>)" class="make-booking-room-button"
                    data-toggle="modal" data-target="#exampleModalLong">
                Book this room
            </button>
        </div>
    </div>
</div>

<div class="modal fade" id="success-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
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

<div class="modal fade" id="fail-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
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


<!-- Modal -->
<div class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
     aria-hidden="true">
    <div style="z-index: 5" class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLongTitle">Create a booking</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="inner">

                <jsp:include page="bookingForm.jsp"/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('.clockpicker').clockpicker();
</script>
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