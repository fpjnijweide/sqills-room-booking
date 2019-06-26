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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>

</html>