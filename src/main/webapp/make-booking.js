//Shows the duration form used top make a booking
function displayMakeBooking() {
    document.getElementById(`content`).innerHTML = `
        <div class="row">
            <div class="col-sm-5 offset-sm-2">
                <input onchange="selectHowLong(value)" type="range" min="30" max="240" step="30" class="custom-range"
                   id="booking-duration">
            </div>
            <div class="col-sm-1" id="booking-duration-value">
            </div>
            <div class="col-sm-2">
              <button onclick="makeBooking()" class="btn btn-primary"><i class="fas fa-rocket"></i></button> 
            </div>
            </div>
        </div>
    `;
}
//Shows the currently selected duration of the meeting to be booked
function selectHowLong(value) {
    document.getElementById("booking-duration-value").innerHTML = `<p>${value.toString()} m</p>`;
}
//Makes booked based upon selection
function makeBooking() {
    let duration = document.getElementById(`booking-duration`).value;
    let endTime = addMinutes(new Date(), duration);
    let jsonBody = { "roomNumber": currentRoomNumber, "date": new Date().toISOString().split('T')[0], "startTime": `${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}`, "endTime": `${endTime.getHours()}:${endTime.getMinutes()}:${new Date().getSeconds()}` };
    axios.post(`/api/booking/create`, jsonBody).then((reponse) => {
        displayBooked();
    });
}
//Add the duration to the current time
function addMinutes(date, minutes) {
    return new Date(date.getTime() + minutes * 60000);
}


//display that the booking is complete
function displayBooked() {
    document.getElementById(`book-now`).innerHTML = `<h3> Booking complete </h3>`;
    setTimeout(() => {
        updatePage(currentRoomNumber);
    }, 5000);
}