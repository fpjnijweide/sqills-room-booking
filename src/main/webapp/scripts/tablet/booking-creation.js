function updateDuration(value) {
    document.getElementById("booking-duration").innerHTML = value + " min";
}

function bookRoom() {
    let title = document.getElementById("booking-title").value;
    if (title === "Booking Title (Optional)") {
        title = "Titleless Meeting";
    }

    let email = document.getElementById("booking-email").value;
    if (email === "Email (Optional)") {
        email = "sqills_tablet@gmail.com";
    }

    let requestBody = {
        "title": title,
        "email": email,
        "roomName": ROOM_NAME,
        "isPrivate": document.getElementById("booking-checkbox").checked
    };

    let durationInputMinutes = parseInt(document.getElementById("slider").value);

    let today = new Date();
    let dateString = "";
    dateString += today.getFullYear();
    dateString += "-" + (today.getMonth() + 1);
    dateString += "-" + today.getDate();

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
            $("#success-modal").modal();
        })
        .catch(error => {
            $("#fail-modal").modal();
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
        })
        .finally(() => {
            setAvailableRoomsAndUpdatePage();
            hideCreationPopUp();
            unblurBackground();
            getBookingsAndUpdatePage();
        });
}