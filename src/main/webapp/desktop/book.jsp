<html>
<head>
    <title>Make a Booking</title>
    <jsp:include page="head.jsp"/>
    <script src="/scripts/make-booking-page.js"></script>
    <link rel="stylesheet" href="../css/book.css">
</head>
<body onload="adaptTimeText()">
<jsp:include page="nav.jsp"/>
<%--Todo: Input validation--%>
<div class="container">
    <div class="row">
        <div class="col-md-2"></div>

        <div class="col-md-8 outer">
            <div class="inner">
                <h1 class="create-booking-title">Create a booking</h1>

                <jsp:include page="bookingForm.jsp"/>
            </div>
        </div>
        <div class="col-md-2"></div>
        <!-- Button trigger modal -->


    </div>
</div>
<script type="text/javascript">
    $('.clockpicker').clockpicker();
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>

</html>

