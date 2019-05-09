function showMakeBooking() {
    document.getElementById(`content`).innerHTML = `
            <input onchange="selectHowLong(value)" type="range" min="30" max="240" step="30" class="custom-range"
               id="booking-duration">
        <button onclick="makeBooking()" class="btn btn-primary">Book</button>
    `;

}

function makeBooking(response) {
    let duration = document.getElementById(`content`).value;
    alert(`Trying to make booking for: ${duration}`);
    axios.post(`/api/room/${1}`).then((reponse) => {
        displayBooked();
    });
}

function displayBooked() {
    document.getElementById(`book-now`).innerHTML = `<h3> Booking complete </h3>`;
    setTimeout(() => {
        generateTable()
    }, 20000);
}