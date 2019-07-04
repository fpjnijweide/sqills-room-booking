function bookingValidityChecks(){
    if (!checkCorrectEmails()){
        showError("not all emails are valid");
        return false;
    } else if (!checkIfEverythingFilledIn()){
        showError("not all fields are filled in");
        return false;
    } else if (!checkStartAfterEndTime()){
        showError("The end time should be after the start time");
        return false;
    } else {
        return true;
    }

}

function checkStartAfterEndTime(){
    let start = document.getElementById("booking-start-time").value;
    let end  = document.getElementById("booking-end-time").value;
    if (end <= start ){
        return false
    } else {
        return true
    }

}

function checkCorrectEmails(){
    let emails = getAllEmails();
    let requestBody = {
        "emails": emails
    };
    if (emails.length > 0) {
        axios.post("/sqillsRoomBooking/api/user/email/check", requestBody)
            .then(response => {
                let data= response.data;
                if (data.emails.length != 0){
                    return false
                }
            });
    } else {
        return true;
    }
}

function checkIfEverythingFilledIn(){
    let title = document.getElementById("booking-title").value;
    let date = document.getElementById("booking-date").value;
    let end = document.getElementById("booking-start-time").value + ":00";
    let start = document.getElementById("booking-end-time").value + ":00";
    if (title == "" || date == "" || end == ":00" || start == ":00"){
        return false;
    } else {
        return true;
    }
}

function bookRoom() {
    if (bookingValidityChecks()) {
        if (document.getElementById("no").checked) {
            makeBooking()
        } else if (checkRecurringFields()) {
            makeRecurringBooking()
        } else {
            showError("fill in all the fields for recurring bookings")
        }
    }
}

function checkRecurringFields(){
    let number = document.getElementById("time").value;
    let recurringEndDate = document.getElementById("recurring-end-date").value;
    if (number != "" && recurringEndDate != "") {
        return true;
    }
    return false;
}

function makeBooking(){
    let selectRoom = document.getElementById("room-select");
//todo hook up room dropdown to make booking
    let title = document.getElementById("booking-title").value
    , email = "" // document.getElementById("booking-email").value
    , date = document.getElementById("booking-date").value
    , startTime = document.getElementById("booking-start-time").value + ":00"
    , endTime = document.getElementById("booking-end-time").value + ":00"
    , roomName = selectRoom.options[selectRoom.selectedIndex].value
    , isPrivate = document.getElementById("booking-isPrivate").checked
    , participants = extractParticpants(document.getElementsByClassName("booking-participant"))
    , requestBody = {
        "title": title,
        "email": email,
        "date": date,
        "startTime": startTime,
        "endTime": endTime,
        "roomName": roomName,
        "isPrivate": isPrivate
    };

    axios.post("/sqillsRoomBooking/api/booking/create", requestBody)
        .then(response => {
            let id = response.data;
            addParticipantsToBooking(id);
          //  setTimeout(function(){ window.location.href="/sqillsRoomBooking/desktop/booking/" + id; }, 200);
            // initGCalendar(insertGCalendarEvent(createGCalendarEvent(roomName,date, startTime, endTime, title, participantElements, isPrivate, null)));

            initGCalendar(insertGCalendarEvent(createGCalendarEvent(roomName,date, startTime, endTime, title, participants, isPrivate, null)));
            // document.location.replace(`/sqillsRoomBooking/api/booking/${id}`);
        }).catch((response) => {
            showError(response.response.data)
    });

}
function makeRecurringBooking(){
    let selectRoom = document.getElementById("room-select");
    let elem = document.getElementById("choose-time-unit");
    let participants = extractParticpants(document.getElementsByClassName("booking-participant"))

    let requestBody = {
        "title": document.getElementById("booking-title").value,
        "email": "", // document.getElementById("booking-email").value,
        "date": document.getElementById("booking-date").value,
        "startTime": document.getElementById("booking-start-time").value + ":00",
        "endTime": document.getElementById("booking-end-time").value + ":00",
        "roomName": selectRoom.options[selectRoom.selectedIndex].value,
        "isPrivate": document.getElementById("booking-isPrivate").checked,
        "repeatEveryType": elem.options[elem.selectedIndex].value,
        "repeatEvery": document.getElementById("time").value,
        "endingAt": document.getElementById("recurring-end-date").value,
    };
    axios.post("/sqillsRoomBooking/api/booking/create/recurring", requestBody)
        .then(response => {
            let id = response.data;
            addParticipantsToBooking(id);
            initGCalendar(insertGCalendarEvent(createGCalendarEvent(requestBody.roomName, requestBody.date, requestBody.startTime, requestBody.endTime, requestBody.title, participants, requestBody.isPrivate, createRecurrence(requestBody.repeatEveryType, requestBody.repeatEvery, requestBody.endingAt))));

        });
}

function extractParticpants(participantElements){
    let participants = [];
    for (let i = 0; i < participantElements.length; i++) {
        participants[i] = {email: participantElements[i].innerText};
    }
    return participants;
}
function addParticipantsToBooking(bookingID) {
    let participantsElements = document.getElementsByClassName("booking-participant");
    for (let i = 0; i < participantsElements.length; i++) {
        let requestObject = {
            "email": participantsElements[i].innerText,
            "bookingid": bookingID
        };
        axios.post("/sqillsRoomBooking/api/participant/add", requestObject);
    }
}

function addParticipantField() {
    let input = document.getElementById("participant").value;
    if(input !== "") {
        let element = document.getElementById("participant-form-group-container");
        element.innerHTML += `
            <div class="booking-form-group">
                <label>Participant</label>
                <div class='participant-in-list'>
                    <span class='booking-participant'>${input}</span>
                    <span class='delete' onclick='removeParticipant(this.parentElement.parentElement)'><i class="fa fa-trash" aria-hidden="false"></i></span>
                </div>
            </div>`;
        document.getElementById("participant").value = "";
    }
}

function setParticipantsVisible() {
    let elem = document.getElementById("participants-container");
    elem.style.display = "block";
}

function setRecurringVisible(invisible){
    let elem = document.getElementById("recurring-container");
    if (invisible){
        elem.classList.remove('hidden');
    } else {
        elem.classList.add('hidden');
    }
}

function getAllEmails(){
    let emails = [];
    // let creatorEmail = document.getElementById("booking-email").value;
    let participantElements = document.getElementsByClassName("participant-in-list");
    // emails.push(creatorEmail)
    for (let i = 0; i < participantElements.length; i++) {
        let email = document.getElementsByClassName("participant-in-list")[i].textContent.slice(0,-6);
        emails.push(email)
    }
    return emails
}

function checkRecurringDays(){
    let unit = document.getElementById("choose-time-unit");
    let value = unit.options[unit.selectedIndex].value;
    console.log(value);
    let number = document.getElementById("time").value;
    if (number > 7 && value === "days"){
        document.getElementById("time").value = Math.floor(number / 7);
        document.getElementById("choose-time-unit").value = "weeks";
    } else if (number > 4 && value === "weeks") {
        document.getElementById("time").value = Math.floor(number / 4);
        document.getElementById("choose-time-unit").value = "months";
    } else if (number > 12 && value === "months") {
        document.getElementById("time").value = Math.floor(number / 12);
        document.getElementById("choose-time-unit").value = "years";
    } else if (number <= 0){

    }
}

function removeParticipant(element){
    element.parentNode.removeChild(element);
}



// function adaptTimeText(){
//     $('.btn.btn-sm.btn-default.btn-block.clockpicker-button').text("Select")
// }

function setRoom(number){
    document.getElementById("room-select").value = number;
}

