// TODO remove this hardcoded variable later
var totalNumberOfRooms = 4; // Total number of rooms, a fake temporary variable used in checkIfOtherRoomsAreBooked()
var refreshSet = false; // Global boolean used to check whether the getData method is being called every X seconds
var currentRoomNumber; // Global variable used for storing room number to be used in methods

function selectRoom() { // Called when "select room" button is pressed
    currentRoomNumber = document.getElementById("room-input").value
    getData(currentRoomNumber); // Call the main method
    if(!refreshSet){ // If we do not already have the getData method being checked every X seconds
        // Use setInterval to make sure it is called every X seconds, and set refreshSet to true
        setInterval(() => {
            getData(currentRoomNumber);
        }, 10000);
        refreshSet = true;
    }
}

function getData(roomNumberInput, checkingForCurrentRoom) {
    if (typeof checkingForCurrentRoom === 'undefined') checkingForCurrentRoom = true; // Set this var to true if not defined
    axios.get(`/api/room/${roomNumberInput}`).then((response) => { // GET request
        let roomStartTime = isRoomFree(response.data); // Check if room is free, and get the time of the next booking

        if (checkingForCurrentRoom) { // If we are checking for the room in the textbox, go into this block of code
                                      // else, we are checking from checkIfOtherRoomsAreBooked() and we do not want
                                      // to print stuff and generate tables
            if (!roomStartTime) {
                // If room start time is null then it's not free
                displayTable(response.data);
                displayRoomIsBooked();
                checkIfOtherRoomsAreBooked()
            } else {
                // If room is free, show that it can be booked
                displayRoomIsFree(roomStartTime);
                showMakeBooking();
            }
        } else {
            // We are checking from checkIfOtherRoomsAreBooked(), so we do not generate tables but print a line instead
            displayOtherFreeRooms(roomStartTime, roomNumberInput);
        }
    });
}

function checkIfOtherRoomsAreBooked() {
    for (let i = 0; i < totalNumberOfRooms; i++) { // TODO change these numbers to loop through actual rooms yes.
        if (i !== currentRoomNumber) { // Don't check for current room, obviously
            getData(i, false);
        }
    }
}

function isRoomFree(tableData) {
    let earliestStartTime = new Date();
    // Set the earliest booking found to tomorrow, so all new bookings we find come before it
    earliestStartTime.setDate(earliestStartTime.getDate() + 1);

    for (let x in tableData) { // Loop through all bookings found
        // Split the start time and end time of this booking into a list of numbers
        let startTimeSplit = tableData[x].startTime.split(":");
        let endTimeSplit = tableData[x].endTime.split(":");


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