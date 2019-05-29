function bookRoom() {
    let requestBody = {
        // "title": document.getElementById("booking-title").value,
        "email": document.getElementById("booking-email").value,
        "date": document.getElementById("booking-date").value,
        "startTime": document.getElementById("booking-starttime").value + ":00",
        "endTime": document.getElementById("booking-endtime").value + ":00",
        "roomID": document.getElementById("booking-roomid").value,
        "isPrivate": document.getElementById("booking-isPrivate").checked
    };

    axios.post("/api/booking/create", requestBody)
        .then(response => {
            let id = response.data.bookingid;
            alert(id);
            addParticipantsToBooking(id);
        });
}

function addParticipantsToBooking(bookingID) {
    let participantElements = document.getElementsByClassName("participant");
    for (let i = 0; i < participantElements.length; i++) {
        let email = participantElements[i].value;
        let requestObject = {
            "email": email,
            "bookingid": bookingID
        };

        axios.post("/api/participant/add", requestObject)
            .then(response => {
                console.log(response);
            })
    }
}

function addParticipantField() {
    let element = document.getElementById("participants-container");
    element.innerHTML += "<input type='text' class='participant full-width'>"
}