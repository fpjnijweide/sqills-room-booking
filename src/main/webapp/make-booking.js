function showMakeBooking() {
    document.getElementById(`content`).innerHTML = `
            <input onchange="selectHowLong(value)" type="range" min="30" max="240" step="30" class="custom-range"
               id="booking-duration">
        <button onclick="makeBooking()" class="btn btn-primary"><i
                                    class="fas fa-rocket"></i></button> <div id="booking-duration-value"></div>
    `;
}

function selectHowLong(value) {
    document.getElementById("booking-duration-value").innerHTML = ` for <p>${value.toString()} minutes</p>`;
}

function makeBooking(response) {
    let duration = document.getElementById(`booking-duration`).value;
    let endTime = addMinutes(new Date(), duration);
    axios.post(`/api/room/${currentRoomNumber}/?startTime=${new Date().getHours()}:${new Date().getMinutes()}&endTime=${endTime.getHours()}:${endTime.getMinutes()}`).then((reponse) => {
        displayBooked();
    });
}

function addMinutes(date, minutes) {
    return new Date(date.getTime() + minutes * 60000);
}

function displayBooked() {
    document.getElementById(`book-now`).innerHTML = `<h3> Booking complete </h3>`;
    setTimeout(() => {
        getData(currentRoomNumber);
    }, 5000);
}