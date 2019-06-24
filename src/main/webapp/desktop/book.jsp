<html>
<head>
    <title>Make a Booking</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/make-booking-page.js"></script>
</head>
<body>
    <jsp:include page="nav.jsp"/>
<%--Todo: Input validation--%>
    <div class="container">
        <div class="row">
            <div class="col-md-2"></div>

            <div class="col-md-8">
                <div class="inner">
                    <h1 class="create-booking-title">Create a booking</h1>

                    <form class="create-booking-form">
                        <label>Booking Title</label>
                        <input id="booking-title" class="booking-title full-width" type="text" value="Booking Title">

                        <label>Your Email</label>
                        <input id="booking-email" class="booking-email full-width" type="text" value="Your Email">

                        <label>Date</label>
                        <input type="date" id="booking-date" class="full-width">

                        <label>Room ID</label>
                        <input type="text" id="booking-roomid" class="full-width">

                        <label>Time</label>
                        <div class="time-container">
                            <input type="time" class="start-time" id="booking-starttime">
                            <input type="time" class="end-time" id="booking-endtime">
                        </div>

                        <label>Is Private</label>
                        <input type="checkbox" id="booking-isPrivate">

                        <div id="participants-container">

                        <button type="button" onclick="addParticipantField()">Add participant</button>

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
                    <button class="submit-button" type="button" onclick="checkAllEmails()">Submit</button>
                </form>
            </div>

            <div class="col-md-2"></div>
        </div>
        <div class="col-md-2"></div>
        <!-- Button trigger modal -->

        <!-- Modal -->
        <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="error-text">

                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
