// TODO remove this hardcoded variable later
var totalNumberOfRooms = 5

// import axios from 'axios'
function selectRoom() {
    getData(document.getElementById("room-input").value)
}

function getData(roomNumber, checkingForCurrentRoom) {
    if (typeof checkingForCurrentRoom === 'undefined') checkingForCurrentRoom = true
    axios.get(`/api/room/${roomNumber}`).then((response) => {
        let roomStartTime = isRoomFree(response.data)
        if (checkingForCurrentRoom) {
            if (!roomStartTime) { // room is booked
                generateTable(response.data, roomNumber)
                displayRoomIsBooked(roomNumber)
                checkIfOtherRoomsAreBooked(roomNumber)
            } else {
                displayRoomIsFree(roomNumber, roomStartTime)

                showMakeBooking(roomNumber)
            }
        } else {
            if (roomStartTime) {
                let roomStatus = document.getElementById("room-status")
                let currentTime = new Date()
                if (currentTime.getHours() <= roomStartTime.getHours() && currentTime.getMinutes() < roomStartTime.getMinutes()){
                    roomStatus.innerHTML += "\n<h4>But, room " + roomNumber + " is free until " +
                        roomStartTime.getHours() + ":" + roomStartTime.getMinutes() + "!</h4>"
                } else {
                    roomStatus.innerHTML += "\n<h4>But, room " + roomNumber + " is free for the whole day!</h4>"
                }

            }
        }

    })
}

function checkIfOtherRoomsAreBooked(roomNumber) {
    let roomStatus = document.getElementById("room-status")
    for (let i = 0; i < totalNumberOfRooms; i++) { // TODO change these numbers to loop through actual rooms
        if (i !== roomNumber) {
            getData(i, false)
        }
    }
}

function displayRoomIsFree(roomNumber, roomStartTime) {
    let roomStatus = document.getElementById("room-status")
    let currentTime = new Date()
    if (currentTime.getHours() <= roomStartTime.getHours() && currentTime.getMinutes() < roomStartTime.getMinutes()) {
        roomStatus.innerHTML = "<h3>Room " + roomNumber + " is free until " +
            roomStartTime.getHours() + ":" + roomStartTime.getMinutes() + "!</h3>"
    } else {
        roomStatus.innerHTML = "<h3>Room " + roomNumber + " is free for the whole day!</h3>"
    }
}

function displayRoomIsBooked(roomNumber) {
    let roomStatus = document.getElementById("room-status")
    roomStatus.innerHTML = "<h3>Room " + roomNumber + " is booked right now</h3>"
}

function isRoomFree(tableData) {
    let earliestStartTime = new Date()
    earliestStartTime.setDate(earliestStartTime.getDate() + 1)
    for (let x in tableData) {
        let startTimeSplit = tableData[x].startTime.split(":")
        let endTimeSplit = tableData[x].endTime.split(":")
        let currentDate = new Date()
        let startDateTime = new Date()
        let endDateTime = new Date()
        startDateTime.setHours(startTimeSplit[0], startTimeSplit[1])
        endDateTime.setHours(endTimeSplit[0], endTimeSplit[1])
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

function generateTable(tableData, roomNumber) {
    let content = document.getElementById("content")
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
    tableContent += `</table>`
    content.innerHTML = tableContent
    setInterval(() => {
        getData(roomNumber)
    }, 30000)
}

