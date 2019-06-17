function blurBackground() {
    document.getElementsByClassName("blur-container")[0].classList.add("blur");
}

function unblurBackground() {
    document.getElementsByClassName("blur-container")[0].classList.remove("blur");
}

function showBookingPopUp() {
    document.getElementById("make-booking-overlay").classList.remove("hidden");
}

function hideBookingPopUp() {
    document.getElementById("make-booking-overlay").classList.add("hidden");
}