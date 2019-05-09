// import axios from 'axios'

var myObj

// document.onload = getData()
function selectRoom(){
    let inputfield = document.getElementById("room-input")
    let roomnr = inputfield.value
    let roomNumber = roomnr
    let inputform = document.getElementById("input-form")
    // inputform.innerHTML=""
    getData(roomNumber)
}

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
    let content = document.getElementById("content");
    let currentDate = new Date()
    let tableContent = `<table class="table" id="room-bookings">
                        <tr>
                         <th>Start Time</th>   
                         <th>End Time</th>   
                        </tr>`;

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



        tableContent += `<tr> 
            <td> ${tableData[x].startTime} </td> 
            <td> ${tableData[x].endTime} </td> 
        </tr>`
    }

    tableContent += `</table>`;
    content.innerHTML = tableContent;
    if (!foundCurrentBooking){
        roomIsFree(roomNumber)
    } else {
        roomIsBooked(roomNumber)
    }
    setInterval(() => {
        getData(roomNumber);
    }, 30000)
}

