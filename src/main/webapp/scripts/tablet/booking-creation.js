function updateDuration(value) {
    document.getElementById("booking-duration").innerHTML = value + " min";
}

function bookRoom() {
    let requestBody = {
        "title": document.getElementById("booking-title").value,
        "email": document.getElementById("booking-email").value,
        "roomName": ROOM_NAME,
        "isPrivate": document.getElementById("booking-checkbox").checked
    };

    let durationInputMinutes = parseInt(document.getElementById("slider").value);

    let today = new Date();
    let dateString = "";
    dateString += today.getFullYear();
    dateString += "-" + (today.getMonth() + 1);
    dateString += "-" + today.getDate();
    console.log(dateString);

    let startTimeString = today.getHours() + ":"
        + today.getMinutes() + ":" +
        "00";

    let endTimeHours = today.getHours() + Math.floor((today.getMinutes() + durationInputMinutes) / 60);
    let endTimeMinutes = (today.getMinutes() + durationInputMinutes) % 60;

    let endTimeString = endTimeHours + ":"
        + endTimeMinutes + ":" +
        "00";

    requestBody.date = dateString;
    requestBody.startTime = startTimeString;
    requestBody.endTime = endTimeString;

    axios.post("/api/booking/create", requestBody)
        .then(response => {
            let id = response.data.bookingid;
            alert(id);
        })
        .finally(() => {
            setAvailableRoomsAndUpdatePage();
            hideBookingPopUp();
            unblurBackground();
            getBookingsAndUpdatePage();
        });
}