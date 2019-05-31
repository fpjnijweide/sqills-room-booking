function createBooking() {
    const requestBody = {
        "roomName": ROOM_ID,
        "date": document.getElementById("date").value,
        "startTime": document.getElementById("start-time").value + ":00",
        "endTime": document.getElementById("end-time").value + ":00",
        "email": document.getElementById("email").value,
        "isPrivate": document.getElementById("isPrivate").checked
    };

    axios.post("/api/booking/create", requestBody)
        .then(response => {
            if (response.data.success) {
                $("#success-modal").modal();
            } else {
                $("#fail-modal").modal();
            }
        })
        .finally(
            updateBookingOverview()
        );
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function updateBookingOverview() {
    // The sleep is required because this function is called right after the
    // POST create request is sent. We have to delay this GET request to make sure the
    // database is actually updated.
    await sleep(2000);
    axios.get(`/api/room/${ROOM_ID}/week`)
        .then(response => {
            let element = document.getElementById("bookings-table");
            element.innerHTML = "<tr>" +
                                    "<th>Date</th>" +
                                    "<th>Start Time</th>" +
                                    "<th>End Time</th>" +
                                "</tr>";

            for (let i = 0; i < response.data.length; i++) {
                let booking = response.data[i];
                element.innerHTML += "<tr>" +
                    "<td>" + booking.date + "</td>" +
                    "<td>" + booking.startTime + "</td>" +
                    "<td>" + booking.endTime + "</td>" +
                "</tr>" ;
            }
        });
}