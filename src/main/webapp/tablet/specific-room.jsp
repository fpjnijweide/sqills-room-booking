<html>
<head>
    <title>jTemp</title>
    <link rel="stylesheet" href="/css/tablet-design.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
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
                <div id="book-button" class="centered-button" onclick="blurBackground(); showCreationPopUp();">Advanced Booking</div>
                <div id="15book-button" class="centered-button" onclick="book15()">Book 15 Minutes</div>
            </div>
        </div>
    </div>

    <div id="make-booking-overlay" class="hidden">
        <div class="close" onclick="hideCreationPopUp(); unblurBackground();"><i class="fa fa-close" aria-hidden="true"></i></div>
        <div class="centered-title">Book a Room</div>
        <div class="instructions">Leave the title or email field empty to use default values</div>
        <div class="vertical-group">
            <label>Title: </label><input onchange="validateTitle(this.value);" class="text-input" type="text" id="booking-title" value="Booking Title (Optional)">
        </div>

        <div class="vertical-group">
            <label>Email: </label><input onchange="validateEmail(this.value);" class="text-input" type="text" id="booking-email" value="Email (Optional)">
        </div>

        <div class="vertical-group">
            <label>Duration: </label>
            <div class="duration-display" id="booking-duration">15 min</div>
            <input onchange="validateDuration(this.value); updateDuration(this.value)" type="range" min="15" max="120" step="5" value="15" class="custom-range form-control-range" id="slider">
        </div>

        <div class="vertical-group"><label>Private Meeting: </label><input class="" type="checkbox" id="booking-checkbox"></div>

        <div id="warning" class="warning-information hidden">
            The title can only contain alphanumerical characters and +,. -_
        </div>

        <div class="vertical-group">
            <button onclick="bookRoom()" class="centered-button">Book!</button>
        </div>
    </div>

    <div id="booking-details-overlay" class="hidden">
        <div class="close" onclick="closeDetailsPopUp();"><i class="fa fa-close" aria-hidden="true"></i></div>
        <div class="title" id="pop-up-title">Book a Room</div>
        <div class="time" id="pop-up-time">13:00 - 13:30</div>
        <div class="owner" id="pop-up-owner"><span class="booked-by">Booked by </span>Marten Voorberg</div>
        <div class="participants" id="participants"></div>
    </div>

    <div class="modal fade" id="success-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header alert alert-success">
                    <h5 class="modal-title" id="success-title">Successfully Created Booking</h5>
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
                    <h5 class="modal-title" id="fail-title">Could Not Create Booking</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" id="booking-error-message">
                    Something went wrong! Sadly your booking could not be created.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
