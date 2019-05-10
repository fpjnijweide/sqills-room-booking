// TODO remove this hardcoded variable later
var totalNumberOfRooms = 5;
var refreshSet = false;
var currentRoomNumber;
function selectRoom() {
    currentRoomNumber = document.getElementById("room-input").value
    getData(currentRoomNumber);
    if(!refreshSet){
        setInterval(() => {
            getData(currentRoomNumber);
        }, 30000);
        refreshSet = true;
    }
}

function getData(roomNumberInput, checkingForCurrentRoom) {
    if (typeof checkingForCurrentRoom === 'undefined') checkingForCurrentRoom = true;
    axios.get(`/api/room/${roomNumberInput}`).then((response) => {
        let roomStartTime = isRoomFree(response.data);
        if (checkingForCurrentRoom) {
            if (!roomStartTime) { // room is booked
                generateTable(response.data);
                displayRoomIsBooked();
                checkIfOtherRoomsAreBooked()
            } else {
                displayRoomIsFree(roomStartTime);

                showMakeBooking();
            }
        } else {
            displayOtherFreeRooms(roomStartTime, roomNumberInput);
        }
    });
}

function displayOtherFreeRooms(roomStartTime, roomNumberInput) {
    if (roomStartTime) {
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

function checkIfOtherRoomsAreBooked() {
    for (let i = 0; i < totalNumberOfRooms; i++) { // TODO change these numbers to loop through actual rooms yes.
        if (i !== currentRoomNumber) {
            getData(i, false);
        }
    }
}

function displayRoomIsFree(roomStartTime) {
    let roomStatus = document.getElementById("room-status");
    let currentTime = new Date();
    if (currentTime.getHours() <= roomStartTime.getHours() && currentTime.getMinutes() < roomStartTime.getMinutes()) {
        roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is free until " +
            roomStartTime.getHours() + ":" + roomStartTime.getMinutes() + "!</h3>"
    } else {
        roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is free for the whole day!</h3>"
    }
}

function displayRoomIsBooked() {
    let roomStatus = document.getElementById("room-status");
    roomStatus.innerHTML = "<h3>Room " + currentRoomNumber + " is booked right now</h3>"
}

function isRoomFree(tableData) {
    let earliestStartTime = new Date();
    earliestStartTime.setDate(earliestStartTime.getDate() + 1);
    for (let x in tableData) {
        let startTimeSplit = tableData[x].startTime.split(":");
        let endTimeSplit = tableData[x].endTime.split(":");
        let currentDate = new Date(), startDateTime = new Date(), endDateTime = new Date();

        startDateTime.setHours(startTimeSplit[0], startTimeSplit[1]);
        endDateTime.setHours(endTimeSplit[0], endTimeSplit[1]);

        if (startDateTime < currentDate && currentDate < endDateTime) {
            return false
        } else {
            if (startDateTime < earliestStartTime && startDateTime > currentDate) {
                earliestStartTime = startDateTime
            }
        }
    }
    return earliestStartTime

}

function generateTable(tableData) {
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

