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
            <div class="col-md-3"></div>

            <div class="col-md-6">
                <h1 class="create-booking-title">Create a booking</h1>

                <form class="form-group row">
                    <label for="booking-title" class="col-md-2 col-form-label">Booking Title</label>
                    <div class="col-md-10">
                      <input id="booking-title" class="form-control" type="text" placeholder="Booking Title">
                    </div>

                    <label for="booking-email" class="col-md-2 col-form-label">Your Email</label>
                    <div class="col-md-10">
                    <input id="booking-email" class="form-control" type="text" placeholder="Your Email">
                    </div>
                    
                    <label for="booking-date" class="col-md-2 col-form-label">Date</label>
                    <div class="col-md-10">
                    <input type="date" id="booking-date" class="form-control">
                    </div>

<%--                 TODO this should be roomname instead of roomid --%>
                    <label for="booking-roomid" class="col-md-2 col-form-label">Room ID</label>
                    <div class="col-md-10">
                    <input type="text" id="booking-roomid" class="form-control">
                    </div>

                    <label>Time</label>
                    <div class="form-row time-container col-md-12">
                        <div class="col-md-4">
                        <input type="time" class="start-time" id="booking-starttime">
                        </div>
                        <div class="col-md-4">
                        <input type="time" class="end-time" id="booking-endtime">
                        </div>
                        <div class="col-md-2">

<%--                            <div class="center">--%>
                                <input type="checkbox" id="booking-isPrivate" style="display:none"/>
                        </div>
                                <label for="booking-isPrivate" class="toggle col-md-2"><span>Private meeting</span></label>
<%--                            </div>--%>

<%--                        <label for="booking-isPrivate">Is Private</label>--%>
<%--                            <input type="checkbox" id="booking-isPrivate" style="display:none"/>--%>

                    </div>



                    <div class="form-row" id="participants-container">

                        <button type="button" class="btn btn-secondary" onclick="addParticipantField()">Add participant</button>
                        <button type="button" class="btn btn-primary "onclick="bookRoom()">Submit</button>

                    </div>


                </form>
            </div>

            <div class="col-md-3"></div>     
        </div>
    </div>
</body>
</html>
