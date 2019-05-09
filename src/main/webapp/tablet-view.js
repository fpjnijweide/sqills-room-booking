document.onload = getData();

function getData() {
    let roomNumber = 1;
    axios.get(`/api/room/${roomNumber}`).then((response) => {
        generateTable(response.data);
    })
}

function roomIsFree(roomNumber){
    console.log("room is free")
}

function roomIsBooked(roomNumber){
    alert("room is booked")
}

function generateTable(tableData){
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

