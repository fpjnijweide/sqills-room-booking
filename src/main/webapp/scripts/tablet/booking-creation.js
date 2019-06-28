function updateDuration(value) {
    document.getElementById("booking-duration").innerHTML = value + " min";
}

function book15() {
    let requestBody = {
        "title": "Quick Booking",
        "email": "sqills_tablet@gmail.com",
        "roomName": ROOM_NAME,
        "isPrivate": false
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
            document.getElementById("booking-error-message").innerText = error.response.data;
            $("#fail-modal").modal();
        })
        .finally(() => {
            setAvailableRoomsAndUpdatePage();
            hideCreationPopUp();
            unblurBackground();
            getBookingsAndUpdatePage();
        });
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
            document.getElementById("booking-error-message").innerText = error.response.data;
            $("#fail-modal").modal();
        })
        .finally(() => {
            setAvailableRoomsAndUpdatePage();
            hideCreationPopUp();
            unblurBackground();
            getBookingsAndUpdatePage();
        });
}

function validateEmail(email) {
    if (email !== "Email (Optional)") {
        axios.get(`/api/user/validateEmail/${email}`)
            .then(response => {
                let emailInputElement = document.getElementById("booking-email");
                if (response.data.valid) {
                    emailInputElement.classList.remove("invalid");
                } else {
                    emailInputElement.classList.add("invalid");
                }
            });
    }
}

function validateTitle(title) {
    let titleInputElement = document.getElementById("booking-title");
    let warningElement = document.getElementById("warning");

    if (isValidTitle(title)) {
        titleInputElement.classList.remove("invalid");
        warningElement.classList.add("hidden");

    } else {
        titleInputElement.classList.add("invalid");
        warningElement.classList.remove("hidden");
        warningElement.innerText = "The title can only contain alphanumerical characters and +,. -_";
    }
}

function isValidTitle(title) {
    const validCharacters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,. -_";

    for (let i = 0; i < title.length; i++) {
        let char = title[i];
        if (!validCharacters.includes(char)) {
            return false;
        }
    }

    return true;
}

function validateDuration(duration) {
    axios.get(`/api/room/${ROOM_NAME}/availableUntil`)
        .then(response => {
            if (response.data !== "") {
                let availableUntilTime = response.data;
                let splitAvailable = availableUntilTime.split(":");
                let availableHours = parseInt(splitAvailable[0]);
                let availableMinutes = parseInt(splitAvailable[1]);
                let availableValue = availableHours * 60 + availableMinutes;
                // console.log("Available");
                // console.log(availableHours);
                // console.log(availableMinutes);
                // console.log(availableValue);

                let today = new Date();
                let currentHour = today.getHours();
                let currentMinute = today.getMinutes();
                let currentValue = (currentHour * 60) + currentMinute + parseInt(duration);
                // console.log(currentHour);
                // console.log(currentMinute);
                // console.log(duration);
                // console.log(currentValue);

                let warningElement = document.getElementById("warning");
                if (availableValue < currentValue) {
                    document.getElementById("booking-duration").classList.add("invalid");
                    warningElement.classList.remove("hidden");
                    warningElement.innerText = "Booking overlaps with future booking";
                } else {
                    warningElement.classList.add("hidden");
                    document.getElementById("booking-duration").classList.remove("invalid");
                }
            }
        });
}
