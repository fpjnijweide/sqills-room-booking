<%@ page import="nl.utwente.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="nl.utwente.model.OutputBooking" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/specific-room.css">
    <link rel="stylesheet" type="text/css" href="/css/book.css">
    <title>Room <%= request.getAttribute("id") %></title>
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
                    <tr>
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
                <!-- Button trigger modal -->
                <button type="button" class="make-booking-room-button" data-toggle="modal" data-target="#exampleModalLong">
                    Launch demo modal
                </button>
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


    <!-- Modal -->
    <div class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
        <div style="z-index: 5" class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="inner">
                    <h1 class="create-booking-title">Create a booking</h1>

                    <form class="create-booking-form">
                        <label>Booking Title</label>
                        <input id="booking-title" class="booking-title full-width" type="text" placeholder="Booking Title">

                        <label>Your Email</label>
                        <input id="booking-email" class="booking-email full-width" type="text" placeholder="Your Email">

                        <label>Date</label>
                        <input type="date" id="booking-date" class="booking-date full-width">

                        <label>Room ID</label>
                        <input type="text" id="booking-roomid" class="full-width" value="<%= request.getAttribute("id") %>">

                        <label>Time</label>
                        <div class="time-container">
                            <input type="time" class="start-time" id="booking-starttime">
                            <input type="time" class="end-time" id="booking-endtime">
                        </div>

                        <label>Private meeting</label>
                        <input type="checkbox" id="booking-isPrivate">
                        <div class="participants-view">
                            <table>
                                <tr>
                                    <td><label class="participants-title">Add participants</label></td>
                                    <td><div class="add-participants" onclick="setParticipantsVisible()">+</div></td>
                                </tr>

                            </table>

                        </div>
                        <div id="participants-container">

                            <div class="input-and-button">
                                <input type="text" id="participant" placeholder="Participant name">
                                <button type="button" onclick="addParticipantField()">+</button>
                            </div>
                            <div id="participants-list">
                                Participants list
                            </div>
                        </div>
                        <div class="recurring">
                            <label>Recurring booking</label>
                            <input type="radio" name="recurring" id="yes" value="yes" onclick="setRecurringVisible(true)"> Yes
                            <input type="radio" name="recurring" id="no" value="no" checked="checked" onclick="setRecurringVisible(false)"> No
                            <div id="recurring-info">I want a booking every
                                <input id="time" type="number" oninput="checkRecurringDays()">
                                <select id="choose-time-unit" class="choose-time-unit" onchange="checkRecurringDays()">
                                    <option value="day">Days</option>
                                    <option value="week">Weeks</option>
                                    <option value="month">Months</option>
                                    <option value="year">Years</option>
                                </select>
                                <br>
                                ending at
                                <input id="recurring-end-date" type="date">
                                from now
                            </div>
                        </div>

                        <button class="submit-button" type="button" onclick="checkFieldsAndBook()">Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>