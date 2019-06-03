let emailIsInDatabase = false;
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
                <input type="email" class="form-control" placeholder="Enter e-mail" id="e-mail" onkeyup="autoComplete()">
                <br>
                <input type="text" class="form-control" placeholder="Booking title (optional)" id="title">
                <br>
                <input type="checkbox" name="private" value="private" id="private"> private meeting 
                
                <br><br>
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
    let title = document.getElementById("title").value;
    if (title === "Booking title (optional)") {
        title = "";
    }
    console.log(email)
    let duration = document.getElementById(`booking-duration`).value;
    let endTime = addMinutes(new Date(), duration);
    if (email == ""){
        email ="sqills_tablet@gmail.com";
    }
    if (validEmail(email)){
        let jsonBody = {"startTime": `${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}`, "endTime": `${endTime.getHours()}:${endTime.getMinutes()}:${new Date().getSeconds()}`, "email": email, "isPrivate": private, "title" : title};
        axios.post(`/api/room/` + currentRoomName + `/book`, jsonBody).then((response) => {
            displayBooked(response.data);
        }).catch((error) => {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.log(error.response.data);
                alert(error.response.status);
                console.log(error.response.headers);
            } else if (error.request) {
                // The request was made but no response was received
                // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                // http.ClientRequest in node.js
                alert(error.request);
            } else {
                // Something happened in setting up the request that triggered an Error
                alert('Error', error.message);
            }
            console.log(error.config);
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
    console.log("here")
    if (data.success){
        document.getElementById(`book-now`).innerHTML = `<h3> Booking complete </h3>`;
    } else {
        document.getElementById(`book-now`).innerHTML = `<h3> Booking failed </h3>`;
    }

    setTimeout(() => {
        updatePage(currentRoomName, true);
        document.getElementById(`book-now`).innerHTML = ``;
    }, 5000);
}

function invalidEmailMessage(){
    let newDiv = document.getElementById("emailerror");
    newDiv.innerHTML = `<b>invalid email</b>`
}

function autoComplete(){
    let email = document.getElementById("e-mail").value;
    if (email.value != "") {
        axios.get(`/api/user/` + email).then((response) => { // GET request
            let data = response.data;
            if (data.email != "null") {
                document.getElementById("e-mail").value = data.email;
                emailIsInDatabase = true;
            } else {
                emailIsInDatabase = false;
            }
        });
    }
}