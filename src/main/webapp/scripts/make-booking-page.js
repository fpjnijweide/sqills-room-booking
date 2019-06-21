function checkAllEmails(){
    let emails = getAllEmails();
    let requestBody = {
        "emails": emails
    }
    axios.post("/api/user/email/check", requestBody)
        .then(response => {
            let data= response.data;
            if (data.emails.length == 0){
                bookRoom()
            } else {
                //todo throw error that some emails are not correct
                console.log("one of the emails is invalid");
            }
        });
}

function bookRoom() {
    if (document.getElementById("no").checked){
        makeBooking()
    } else if (checkRecurringFields()){
        makeRecurringBooking()
    } else {
        //todo show proper error that recurring fields have to be filled in
        console.log("fill in all recurring booking fields")
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
    let requestBody = {
        "title": document.getElementById("booking-title").value,
        "email": document.getElementById("booking-email").value,
        "date": document.getElementById("booking-date").value,
        "startTime": document.getElementById("booking-starttime").value + ":00",
        "endTime": document.getElementById("booking-endtime").value + ":00",
        "roomName": document.getElementById("booking-roomid").value,
        "isPrivate": document.getElementById("booking-isPrivate").checked
    };

    axios.post("/api/booking/create", requestBody)
        .then(response => {
            let id = response.data;
            addParticipantsToBooking(id);
            document.location.replace(`/api/booking/${id}`);
        });
}
function makeRecurringBooking(){
    let elem = document.getElementById("choose-time-unit");
    let requestBody = {
        "title": document.getElementById("booking-title").value,
        "email": document.getElementById("booking-email").value,
        "date": document.getElementById("booking-date").value,
        "startTime": document.getElementById("booking-starttime").value + ":00",
        "endTime": document.getElementById("booking-endtime").value + ":00",
        "roomName": document.getElementById("booking-roomid").value,
        "isPrivate": document.getElementById("booking-isPrivate").checked,
        "repeatEveryType": elem.options[elem.selectedIndex].value,
        "repeatEvery": document.getElementById("time").value,
        "endingAt": document.getElementById("recurring-end-date").value
    };
    console.log(requestBody)
    axios.post("/api/booking/create/recurring", requestBody)
        .then(response => {
            let id = response.data;
            addParticipantsToBooking(id);
            document.location.replace(`/api/booking/${id}`);
        });
}

function addParticipantsToBooking(bookingID) {
    let participantElements = document.getElementsByClassName("participant-in-list")
    for (let i = 0; i < participantElements.length; i++) {
        let email = participantElements[i].textContent.slice(0,-6);
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
    let input = document.getElementById("participant").value;
    if(input != "") {
        let element = document.getElementById("participants-list");
        element.innerHTML += "<div class='participant-in-list'>" +
            input + "<div class='delete' onclick='removeParticipant(this.parentElement)'>delete</div>"
            + "</div>"
        document.getElementById("participant").value = "";
        console.log()
    }
}

function setParticipantsVisible(){
    let elem = document.getElementById("participants-container")
    elem.style.display = "block";
}

function setRecurringVisible(invisible){
    let elem = document.getElementById("recurring-info")
    if (invisible){
        elem.style.display = "block";
    } else {
        elem.style.display = "none";
    }
}

function getAllEmails(){
    let emails = [];
    let creatorEmail = document.getElementById("booking-email").value;
    let participantElements = document.getElementsByClassName("participant-in-list");
    emails.push(creatorEmail)
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
    if (number > 7 && value == "days"){
        document.getElementById("time").value = Math.floor(number / 7);
        document.getElementById("choose-time-unit").value = "weeks";
    } else if (number > 4 && value == "weeks") {
        document.getElementById("time").value = Math.floor(number / 4);
        document.getElementById("choose-time-unit").value = "months";
    } else if (number > 12 && value == "months") {
        document.getElementById("time").value = Math.floor(number / 12);
        document.getElementById("choose-time-unit").value = "years";
    } else if (number <= 0){

    }
}

function removeParticipant(element){
    element.parentNode.removeChild(element);
}
//todo
//todo throw nicer errors instead of console.logs