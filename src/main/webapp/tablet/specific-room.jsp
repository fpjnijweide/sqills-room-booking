<html>
<head>
    <title>jTemp</title>
    <link rel="stylesheet" href="/css/tablet-design.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script>
        // noinspection JSAnnotator
        const ROOM_NAME = "<%= request.getAttribute("roomName") %>";

        let available_rooms = [];


        function openBookingPage() {
            console.log("Open booking page was called.")
        }
    </script>

    <script src="/scripts/tablet/booking-creation.js"></script>
    <script src="/scripts/tablet/time.js"></script>
    <script src="/scripts/tablet/availability.js"></script>
    <script src="/scripts/tablet/pop-up-controller.js"></script>
    <script src="/scripts/tablet/bookings-overview.js"></script>
</head>
<body onload="updateTime(); updateAvailabilityInterval(); updateBookingsOverviewInterval();">
    <div class="blur-container">
        <div class="left-container">
            <div id="bookings-container">
                <h1 class="booking-header">Today's Bookings</h1>
            </div>
        </div>

        <div class="right-container">
            <div class="centered-container">
                <div class="centered-text" id="room-name"><%= request.getAttribute("roomName") %></div>
                <div class="centered-text" id="time">13:31</div>
                <div class="centered-text" id="availability">UNAVAILABLE</div>
                <div class="centered-button" onclick="blurBackground(); showCreationPopUp();">Book Now</div>
            </div>
        </div>
    </div>

    <div id="make-booking-overlay" class="hidden">
        <div class="centered-title">Book a Room</div>
        <div class="vertical-group">
            <label>Title: </label><input class="text-input" type="text" id="booking-title" value="Booking Title (Optional)">
        </div>

        <div class="vertical-group">
            <label>Email: </label><input class="text-input" type="text" id="booking-email" value="Email (Optional)">
        </div>

        <div class="vertical-group">
            <label>Duration: </label>
            <div class="duration-display" id="booking-duration">15 min</div>
            <input onchange="updateDuration(this.value)" type="range" min="15" max="120" step="5" value="15" class="custom-range form-control-range" id="slider">
        </div>

        <div class="vertical-group"><label>Private Meeting: </label><input class="custom-control-input" type="checkbox" id="booking-checkbox"></div>

        <div class="vertical-group">
            <button onclick="bookRoom()" class="centered-button">Book!</button>
        </div>
    </div>

    <div id="booking-details-overlay" class="hidden">
        <div class="close" onclick="closeDetailsPopUp();"><i class="fa fa-close" aria-hidden="true"></i></div>
        <div class="title" id="pop-up-title">Book a Room</div>
        <div class="time" id="pop-up-time">13:00 - 13:30</div>
        <div class="owner" id="pop-up-owner"><span class="booked-by">Booked by </span>Marten Voorberg</div>
        <%--<div class="participants">--%>
            <%--<h4>Participants</h4>--%>
            <%--<ul>--%>
                <%--<li>Platon Frolov</li>--%>
                <%--<li>Sara Lute</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
    </div>
</body>
</html>
