// TODO remove this hardcoded variable later
var totalNumberOfRooms = 4; // Total number of rooms, a fake temporary variable used in checkIfOtherRoomsAreBooked()
var refreshSet = false; // Global boolean used to check whether the updatePage method is being called every X seconds
var currentRoomNumber; // Global variable used for storing room number to be used in methods

function selectRoom() { // Called when "select room" button is pressed
    currentRoomNumber = document.getElementById("room-input").value
    updatePage(currentRoomNumber); // Call the main method
    if(!refreshSet){ // If we do not already have the updatePage method being checked every X seconds
        // Use setInterval to make sure it is called every X seconds, and set refreshSet to true
        setInterval(() => {
            updatePage(currentRoomNumber);
        }, 30000);
        refreshSet = true;
    }
}

//TODO refactor
function updatePage(roomNumberInput) {
    axios.get(`/api/room/${roomNumberInput}`).then((response) => { // GET request
        let roomStartTime = getEarliestStartTime(response.data); // Check if room is free, and get the time of the next booking
            if (!roomStartTime) {
                // If room start time is null then it's not free
                displayTableOfBookings(response.data);
                displayRoomIsBooked();
                checkIfOtherRoomsAreBooked()
            } else {
                // If room is free, show that it can be booked
                displayRoomIsFree(roomStartTime);
                displayMakeBooking();
            }
    });
}

function checkIfOtherRoomsAreBooked() {
    for (let i = 0; i < totalNumberOfRooms; i++) { // TODO change these numbers to loop through actual rooms yes.
        if (i !== currentRoomNumber) { // Don't check for current room, obviously
            axios.get(`/api/room/${i}`).then(response => { // GET request
                let roomStartTime = getEarliestStartTime(response.data); // Check if room is free, and get the time of the next booking
                displayOtherFreeRooms(roomStartTime, i);
            });
        }

    }
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
        startDateTime.setHours(startTimeSplit[0], startTimeSplit[1]);
        endDateTime.setHours(endTimeSplit[0], endTimeSplit[1]);

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