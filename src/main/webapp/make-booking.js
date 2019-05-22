//Shows the duration form used top make a booking
function displayMakeBooking() {
    document.getElementById(`content`).innerHTML = `
        <div class="row">
            <div class="col-sm-5 offset-sm-2">
                <div class="booking-text">
                    <h2 style="text-align: center">Make a booking</h2>
                    <p>Drag the slider to select a duration</p>
                </div>
                <input onchange="selectHowLong(value)" type="range" min="30" max="240" step="30" class="custom-range"
                   id="booking-duration">
                <br>
                <br>
                <input type="email" class="form-control" placeholder="Enter e-mail" id="e-mail">
                <input type="checkbox" name="private" value="private" id="private"> private meeting <br>
            </div>
            <div class="col-sm-1" id="booking-duration-value">

                
            <div class="col-sm-2">
             
            </div>
            </div> 
             <button onclick="makeBooking()" class="btn btn-primary"><i class="fas fa-rocket"></i></button> 
        </div>
        <div id="emailerror"></div>
    `;
}
//Shows the currently selected duration of the meeting to be booked
function selectHowLong(value) {
    document.getElementById("booking-duration-value").innerHTML = `<p>${value.toString()} m</p>`;
}
//Makes booked based upon selection
function makeBooking() {
    let private = document.getElementById(`private`).checked;
    let email = document.getElementById(`e-mail`).value;
    console.log(email)
    let duration = document.getElementById(`booking-duration`).value;
    let endTime = addMinutes(new Date(), duration);
    if (validEmail(email) || email.value == ""){
        let jsonBody = { "roomNumber": currentRoomNumber, "date": new Date().toISOString().split('T')[0], "startTime": `${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}`, "endTime": `${endTime.getHours()}:${endTime.getMinutes()}:${new Date().getSeconds()}`, "email": email, "isPrivate": private};
        axios.post(`/api/booking/create`, jsonBody).then((reponse) => {
            displayBooked();
        });
    } else {
        invalidEmailMessage();
    }

}

function validEmail(emailParam){
    let email = emailParam;
    console.log(email.length != 0 && email.includes("@") && email.includes("."));
    return email.length != 0 && email.includes("@") && email.includes(".");
}
//Add the duration to the current time
function addMinutes(date, minutes) {
    return new Date(date.getTime() + minutes * 60000);
}


//display that the booking is complete
function displayBooked(data) {
    if (data.success){
        document.getElementById(`book-now`).innerHTML = `<h3> Booking complete </h3>`;
    } else {
        document.getElementById(`book-now`).innerHTML = `<h3> Booking failed </h3>`;
    }

    setTimeout(() => {
        updatePage(currentRoomNumber);
    }, 5000);
}

function invalidEmailMessage(){
    let newDiv = document.getElementById("emailerror");
    newDiv.innerHTML = `<b>invalid email</b>`


}