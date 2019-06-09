<html>
<head>
    <title>jTemp</title>
    <link rel="stylesheet" href="/css/tablet-design.css">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script>
        // noinspection JSAnnotator
        const ROOM_NAME = "<%= request.getAttribute("roomName") %>";

        let available_rooms = [];


        function openBookingPage() {
            console.log("Open booking page was called.")
        }
    </script>

    <script src="/scripts/tablet/time.js"></script>
    <script src="/scripts/tablet/availability.js"></script>
</head>
<body onload="updateTime(); updateAvailabilityInterval();">
    <div class="left-container">
        <div id="bookings-container">
            <h1>Todays Bookings</h1>

            <div class="booking">
                <div class="time">
                    <span class="start-time">13:15</span>
                    <span class="dash"> - </span>
                    <span class="end-time">14:00</span>
                </div>
                <div class="title">Code Review</div>
            </div>

            <div class="booking">
                <div class="time">
                    <span class="start-time">13:15</span>
                    <span class="seperator"> - </span>
                    <span class="end-time">14:00</span>
                </div>
                <div class="title">Code Review</div>
            </div>

            <div class="booking">
                <div class="time">
                    <span class="start-time">13:15</span>
                    <span class="seperator"> - </span>
                    <span class="end-time">14:00</span>
                </div>
                <div class="title">Code Review</div>
            </div>
        </div>
    </div>

    <div class="right-container">
        <div class="centered-container">
            <div class="centered-text" id="room-name"><%= request.getAttribute("roomName") %></div>
            <div class="centered-text" id="time">13:31</div>
            <div class="centered-text" id="availability">UNAVAILABLE</div>
            <div class="centered-button" onclick="openBookingPage()">Book Now</div>
        </div>
    </div>
</body>
</html>
