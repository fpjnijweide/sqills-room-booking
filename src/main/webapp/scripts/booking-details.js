function hideDetails() {
    let element = document.getElementById("booking-details");
    element.classList.add("hide");
}

function showDetails(bookingID) {
    let element = document.getElementById("booking-details");
    element.classList.remove("hide");
}