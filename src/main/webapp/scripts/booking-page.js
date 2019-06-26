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