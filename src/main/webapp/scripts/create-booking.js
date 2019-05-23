function createBooking() {
    const requestBody = {
        "roomNumber": ROOM_ID,
        "date": document.getElementById("date").value,
        "startTime": document.getElementById("start-time").value + ":00",
        "endTime": document.getElementById("end-time").value + ":00",
        // "email": document.getElementById("email").value,
        // "isPrivate": document.getElementById("isPrivate").value
    };

    axios.post("/api/booking/create", requestBody)
        .then(response => {
            if (response.data.success) {
                $("#success-modal").modal();
            } else {
                $("#fail-modal").modal();
            }
        });
}