<html>
<head>
    <title>Desktop Interface</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/display-available-rooms.js"></script>
</head>
<body>
    <jsp:include page="nav.jsp"/>
    <div class="container">
        <div class="row">
            <div class="col-md-3"></div>

            <div class="col-md-6">
                <h1>Available Rooms:</h1>
                <table id="available-ids" class="table">
                    <tr>
                        <th class="room-name">Room Name</th>
                        <th class="available">Available Until</th>
                    </tr>
                </table>
            </div>

            <div class="col-md-3"></div>
        </div>
    </div>
</body>
</html>