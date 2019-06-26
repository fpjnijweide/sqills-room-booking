function disableButtonsBasedOnPermissions() {
    axios.get(`/api/booking/${bookingID}/access`)
        .then(response => {
            if(response.data) {
                let elements = document.getElementsByClassName("booking-information-button");
                for (let i = 0; i < elements.length; i++) {
                    elements[i].classList.remove("disabled");
                    elements[i].removeAttribute("disabled")
                }
            }
        })
}

function redirectToAllBookings() {
    window.location = '/desktop/bookings';
}

function deleteBooking() {
    axios.delete(`/api/booking/${bookingID}`)
        .then(response => {
            $('#deletion-success-modal').modal();
        })
        .catch(error => {
            document.getElementById("deletion-error-container").innerText = error.response.data;
            $('#deletion-failure-modal').modal();
        });
}

function openEditPopUp() {
    $("#edit-modal").modal();
}

function editBooking() {
    let requestBody = {
        "title": document.getElementById("edit-booking-title").value,
        "roomName": document.getElementById("edit-booking-room-name").value,
        "date": document.getElementById("edit-booking-date").value,
        "startTime": document.getElementById("edit-booking-start-time").value,
        "endTime": document.getElementById("edit-booking-end-time").value,
        "isPrivate": document.getElementById("edit-booking-is-private").checked,
        "email": EMAIL
    }

    axios.put(`/api/booking/${bookingID}`, requestBody)
        .then(response => {
            console.log(response)
        })
        .catch(error => {
            console.log(error);
            console.log(error.response.data);
        })
}