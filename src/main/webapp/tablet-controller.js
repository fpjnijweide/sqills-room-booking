var refreshSet = false; // Global boolean used to check whether the updatePage method is being called every X seconds
var currentRoomNumber; // Global variable used for storing room number to be used in methods
var showing = false
var rooms;

function getRooms(){
    axios.get(`/api/room/list`).then((response) => { // GET request
        rooms = response.data;
    });
}

function selectRoom() { // Called when "select room" button is pressed
    currentRoomNumber = document.getElementById("room-input").value;
    if (rooms.includes(parseInt(currentRoomNumber))){
        updatePage(currentRoomNumber, false); // Call the main method
        if(!refreshSet){ // If we do not already have the updatePage method being checked every X seconds
            // Use setInterval to make sure it is called every X seconds, and set refreshSet to true
            setInterval(() => {
                updatePage(currentRoomNumber, true);

            }, 30000);
            refreshSet = true;
        }
    } else {
        invalidRoomNumber(currentRoomNumber);
    }
}

function invalidRoomNumber(number){
    alert(number + " is not a valid room number")
}


function checkIfRoomTaken(data) {
    let roomStartTime = getEarliestStartTime(data); // Check if room is free, and get the time of the next booking
    return roomStartTime;
}

function updatePage(roomNumberInput, update) {
    axios.get(`/api/room/${roomNumberInput}`).then((response) => { // GET request
        let data = response.data;
        if(!checkIfRoomTaken(data)){
            showing = false;
            displayTableOfBookings(data); // remove this one
            displayRoomIsBooked();
            checkIfOtherRoomsAreBooked();
        } else {
            if (!update || !showing && checkIfRoomTaken(data)) {
                showing = true;
                // console.log(data)
                displayRoomIsFree(checkIfRoomTaken(data));
                displayMakeBooking();
            }
        }
    });
}

function checkIfOtherRoomsAreBooked() {
    axios.get(`/api/room/list`).then((response) => { // GET request
        let listOfRoomIDs = response.data
        for(let id of listOfRoomIDs) {
            if (id != currentRoomNumber) { // Don't check for current room, obviously
                axios.get(`/api/room/${id}`).then(response => { // GET request
                    let roomStartTime = getEarliestStartTime(response.data); // Check if room is free, and get the time of the next booking
                    displayOtherFreeRooms(roomStartTime, id);
                });
            }
        }
    });

}

function getEarliestStartTime(bookings) {
    let earliestStartTime = new Date();
    // Set the earliest booking found to tomorrow, so all new bookings we find come before it
    earliestStartTime.setDate(earliestStartTime.getDate() + 1);
    for (let x in bookings) { // Loop through all bookings found
        // Split the start time and end time of this booking into a list of numbers
        let startTimeSplit = bookings[x].startTime.split(":");
        let endTimeSplit = bookings[x].endTime.split(":");


        // Generate date-time objects for the current time, start time of booking, end time of booking
        let currentDate = new Date(), startDateTime = new Date(), endDateTime = new Date();

        // Set the start time, end time objects' time to the numbers that we found in the HTTP response
        startDateTime.setHours(startTimeSplit[0], startTimeSplit[1], 0, 0);
        endDateTime.setHours(endTimeSplit[0], endTimeSplit[1], 0, 0);

        if (startDateTime < currentDate && currentDate < endDateTime) {
            // If the current time is between the times found, the room is booked! Return null
            return null
        } else {
            // Else, check if this booking is the earliest booking found so far while still being after the current time
            if (startDateTime < earliestStartTime && startDateTime > currentDate) {
                // If this is the case, then this is the next booking time!
                earliestStartTime = startDateTime
            }
        }
    }
    return earliestStartTime // If no current booking is found, return the time of the next booking

}