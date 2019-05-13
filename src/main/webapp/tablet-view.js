function displayRoomIsFree(roomStartTime) {
    // Prints the fact that the room is free in the room-status div.
    let roomStatus = document.getElementById("room-status");
    let currentTime = new Date();
    if (currentTime.getHours() <= roomStartTime.getHours() && currentTime.getMinutes() < roomStartTime.getMinutes()) {
        // If the booking time is not now, print until when room is free
        roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is free until " +
            roomStartTime.getHours() + ":" + roomStartTime.getMinutes() + "!</h3>"
    } else {
        // If booking time is now, earliestStartTime wasn't reset in isRoomFree()
        // This means that there are no bookings for today. Print this fact.
        roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is free for the whole day!</h3>"
    }
}

function displayRoomIsBooked() {
    // Prints the fact that room is booked
    let roomStatus = document.getElementById("room-status");
    roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is booked right now</h3>"
}

function displayTable(tableData) {
    // Prints a table of bookings
    let content = document.getElementById("content");
    let tableContent = `<table class="table" id="room-bookings">
                        <tr>
                         <th>Start Time</th>   
                         <th>End Time</th>   
                        </tr>`
    for (let x in tableData) {
        tableContent += `<tr> 
            <td> ${tableData[x].startTime} </td> 
            <td> ${tableData[x].endTime} </td> 
        </tr>`
    }
    tableContent += `</table>`;
    content.innerHTML = tableContent;

}

function displayOtherFreeRooms(roomStartTime, roomNumberInput) {
    // Called from getData() when checkingForCurrentRoom is false

    if (roomStartTime) { // If the room is not currently booked
        // Basically do the same as in displayRoomIsFree() except for that it doesn't reset the room-status div contents
        // Instead, add new lines
        let roomStatus = document.getElementById("room-status");
        let currentTime = new Date();
        if (currentTime.getHours() <= roomStartTime.getHours() && currentTime.getMinutes() < roomStartTime.getMinutes()) {
            roomStatus.innerHTML += `<h4>But, room   ${roomNumberInput}  is free until 
                ${roomStartTime.getHours()}: ${roomStartTime.getMinutes()}!</h4>`;
        } else {
            roomStatus.innerHTML += `<h4>But, room ${roomNumberInput}  is free for the whole day!</h4>`;
        }

    }
}