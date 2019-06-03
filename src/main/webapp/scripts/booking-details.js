function hideDetails() {
    let element = document.getElementById("booking-details");
    element.classList.add("hide");
}

function showDetails(bookingID) {
    let element = document.getElementById("booking-details");
    populateDetails();

    element.classList.remove("hide");
}

function populateDetails(){
    axios.get(`/api/booking/${ROOM_ID}`).then((response)=>
        {
        document.getElementById(`selected-booking-date`).innerText = `Date: ${response.data.date}`;
        document.getElementById(`selected-booking-time`).innerText = `Time: ${response.data.startTime}`;
        document.getElementById(`selected-booking-owner`).innerText = `Email: ${response.data.name}`;
        document.getElementById(`selected-booking-title`).innerText = `Title: ${response.data.title}`
        }
    )
}