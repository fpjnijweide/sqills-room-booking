<html>
<head>
    <title>Make a Booking</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/make-booking-page.js"></script>
    <link rel="stylesheet" href="../css/book.css">
</head>
<body>
<jsp:include page="nav.jsp"/>
<%--Todo: Input validation--%>
<div class="container">
    <div class="row">
        <div class="col-md-2"></div>

        <div class="col-md-8 outer">
            <div class="inner">
                <h1 class="create-booking-title">Create a booking</h1>

                <form class="create-booking-form">
                    <label>Booking Title</label>
                    <input id="booking-title" class="booking-title full-width" type="text" placeholder="Booking Title">

                    <label>Date</label>
                    <input type="date" id="booking-date" class="booking-date full-width">

                    <label>Room ID</label>
                    <input type="text" id="booking-roomid" class="full-width" placeholder="Room-ID">

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
        <div class="col-md-2"></div>
        <!-- Button trigger modal -->


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
</div>
</body>
</html>

