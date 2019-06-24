function createBooking() {
    let date = document.getElementById("date").value, startTime = document.getElementById("start-time").value + ":00",
       endTime =  document.getElementById("end-time").value + ":00", email = document.getElementById("email").value,
        isPrivate =  document.getElementById("isPrivate").checked, title = document.getElementById("title").value;
    const requestBody = {
        "roomName": ROOM_ID,
        "date": date,
        "startTime": startTime,
        "endTime": endTime,
        "email": email,
        "isPrivate": isPrivate,
        "title": title
    };
    axios.post("/api/booking/create", requestBody)
        .then(response => {
            $("#success-modal").modal();
            insertGCalendarEvent(createGCalendarEvent(ROOM_ID,date, startTime, endTime, title, null, isPrivate, null));
        }).catch((response) => {
            $("#fail-modal").modal();
            failModalText = document.getElementById("fail-modal-text")
            failModalText.innerHTML = response.response.data;
        }).finally((response) => {
            updateBookingOverview()
    });
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


