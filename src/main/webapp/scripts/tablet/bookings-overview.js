function updateBookingsOverviewInterval() {
    getBookingsAndUpdatePage();

    setTimeout(updateBookingsOverviewInterval, 30000);
}

function getBookingsAndUpdatePage() {
    axios.get('/api/room/' + ROOM_NAME)
        .then(response => {
            updatePage(response.data);
        });
}

function updatePage(bookings) {
    let container = document.getElementById("bookings-container");

    container.innerHTML = "<h1 class=\"booking-header\">Today's Bookings</h1>";

    for (let i = 0; i < bookings.length; i++) {
        container.innerHTML +=
            `<div class=\"booking\" onclick=\"openDetailsPopUp(${bookings[i].bookingid})\">\n` +
            `<div class=\"title\">${bookings[i].title}</div>` +
            `<div class=\"time\">${bookings[i].startTime} - ${bookings[i].endTime}</div>` +
            `</div>`;
    }
}