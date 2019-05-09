// import axios from 'axios'
function selectRoom() {
    getData(document.getElementById("room-input").value);
}

function getData(roomNumber) {
    axios.get(`/api/room/${roomNumber}`).then((response) => {
        if (!isRoomFree(response.data)) {
            generateTable(response.data, roomNumber);
            displayRoomIsBooked(roomNumber);
        } else {
            displayRoomIsFree(roomNumber);
            showMakeBooking();
        }
    });
}

function displayRoomIsFree(roomNumber) {
    let roomStatus = document.getElementById("room-status");
    roomStatus.innerHTML = "<h3>Room " + roomNumber + " is free!</h3>"
}

function displayRoomIsBooked(roomNumber) {
    let roomStatus = document.getElementById("room-status")
    roomStatus.innerHTML = "<h3>Room " + roomNumber + " is booked right now</h3>"
}

function isRoomFree(tableData) {
    for (let x in tableData) {
        let startTimeSplit = tableData[x].startTime.split(":")
        let endTimeSplit = tableData[x].endTime.split(":")
        let currentDate = new Date()
        let startDateTime = new Date()
        let endDateTime = new Date()
        startDateTime.setHours(startTimeSplit[0], startTimeSplit[1])
        endDateTime.setHours(endTimeSplit[0], endTimeSplit[1])
        if (startDateTime < currentDate && currentDate < endDateTime) {
            return false;
        }
    }
    return true;

}

function generateTable(tableData, roomNumber) {
        let content = document.getElementById("content");
        let tableContent = `<table class="table" id="room-bookings">
                        <tr>
                         <th>Start Time</th>   
                         <th>End Time</th>   
                        </tr>`;
        for (let x in tableData) {
            tableContent += `<tr> 
            <td> ${tableData[x].startTime} </td> 
            <td> ${tableData[x].endTime} </td> 
        </tr>`
        }
        tableContent += `</table>`;
        content.innerHTML = tableContent;
        setInterval(() => {
            getData(roomNumber);
        }, 30000)
    }

