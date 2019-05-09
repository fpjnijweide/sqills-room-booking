// import axios from 'axios'

var myObj

// document.onload = getData()

function getData(roomNumber){

    axios.get(`/api/room/${roomNumber}`).then((response)=>{
        generateTable(response.data,roomNumber);
    })
}

function roomIsFree(roomNumber){
    let roomStatus = document.getElementById("room-status")
    roomStatus.innerHTML="<h3>Room " + roomNumber + " is free!</h3>"
    // roomStatus.style.color="green"
}

function roomIsBooked(roomNumber) {
    let roomStatus = document.getElementById("room-status")
    roomStatus.innerHTML="<h3>Room " + roomNumber + " is booked right now</h3>"
    // roomStatus.style.color="red"
}

function generateTable(tableData,roomNumber){
    let foundCurrentBooking = false
    let table = document.getElementById("room-bookings")
    let currentDate = new Date()
    let txt = ""
    for (let x in tableData) {
        let startTimeSplit = tableData[x].startTime.split(":")
        let endTimeSplit = tableData[x].endTime.split(":")

        let startDateTime = new Date()
        let endDateTime = new Date()
        startDateTime.setHours(startTimeSplit[0],startTimeSplit[1])
        endDateTime.setHours(endTimeSplit[0],endTimeSplit[1])

        // TODO include check if it is booked in the next half hour
        if (startDateTime < currentDate && currentDate < endDateTime) {

            foundCurrentBooking = true
        }



        txt += "<tr>" +
            "<td>" + tableData[x].roomNumber + "</td>" +
            "<td>" + tableData[x].startTime + "</td>" +
            "<td>" + tableData[x].endTime + "</td>" +
        "</tr>"
    }

    table.innerHTML = txt
    if (!foundCurrentBooking){
        roomIsFree(roomNumber)
    } else {
        roomIsBooked(roomNumber)
    }
}

