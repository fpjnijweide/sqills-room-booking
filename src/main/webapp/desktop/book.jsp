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

                        <button type="button" onclick="bookRoom()">Submit</button>
                    </form>
                </div>
            </div>

            <div class="col-md-2"></div>
        </div>
    </div>
</body>
</html>
