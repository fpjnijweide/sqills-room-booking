<!DOCTYPE html>
<!-- Front end for the webBookQuote Servlet. -->

<html lang="en">
<head>
    <title>Room Booking</title>
    <meta charset="UTF-8">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <!--        TODO make tablet-view script only load in mobile view?-->
    <!--CDN-->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.b7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="/css/tablet.css">
    <script src="/scripts/tablet/tablet-controller.js"></script>
    <script src="/scripts/tablet/tablet-view.js"></script>
    <script src="/scripts/tablet/make-booking.js"></script>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css"
          integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">

</head>

<body onload="getRooms()">
<header>
    <div class="header-logo">
        <img src="/assets/sqills-logo.svg" alt="sqills logo"/>
    </div>
</header>
<div class="container-fluid">
    <div id="emailinput">

    </div>

    <div id="content">
        <div class="row">
            <div class="col-sm">
                <form id="input-form" onsubmit="return enterPressed(event)">
                    <ul class="input-form-select-room">
                        <li>
                            <input type="text" class="form-control" maxlength="10" placeholder="Enter Room Number" id="room-input"
                                   name="room-input" onkeyup="autoCompleteRoomName()">
                        </li>
                        <li>
                            <button id="room-button" onclick="selectRoom()" class="btn btn-primary" type="button">
                                <i class="fas fa-rocket"></i>
                            </button>
                        </li>
                    </ul>
                </form>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-8 offset-sm-2 "id="book-now">
        </div>
        <div class="col-sm-8 offset-sm-2" id="room-status">
        </div>

    </div>
</div>
</body>


</html>