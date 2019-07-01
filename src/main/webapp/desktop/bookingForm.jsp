<script src="/sqillsRoomBooking/scripts/tablet/make-tablet-booking.js"></script>
<form class="create-booking-form">
    <label>Booking Title</label>
    <input id="booking-title" class="booking-title full-width" type="text" placeholder="Booking Title">

    <label>Date</label>
    <input type="date" id="booking-date" class="booking-date full-width">

    <label>Room ID</label>
    <div class="choose-room-for-booking">
        <select id="room-select" class="choose-time-unit">
        </select>
    </div>

    <label>Start Time</label>
    <div>
        <input type="time" id="booking-start-time">
    </div>

    <label>End Time</label>
    <div>
        <input type="time" id="booking-end-time">
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
            <input type="text" id="participant" onkeyup="autoComplete(this)" placeholder="Participant name">
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
        </div>
    </div>

    <button class="submit-button" type="button" onclick="checkFieldsAndBook()">Submit</button>
</form>
<script>
    $(document).ready(function(){

//init data
        axios.get(`/sqillsRoomBooking/api/room/list`).then((response) => { // GET request
            rooms = response.data;
            for (let i = 0; i <= rooms.length; i++) {
                if (rooms[i] != undefined) {
                    $('#room-select').append('<option value="' + rooms[i] + '">' + rooms[i] + '</option>');
                }
            }
        })
    });
</script>