document.onload = getData()

function getData() {
    let roomNumber = 1;
    axios.get(`/api/room/${roomNumber}`).then((response) => {
        generateTable(response.data);
    })
}

function generateTable(tableData) {
    let table = document.getElementById("room-bookings")
    let txt = ""
    for (let x in tableData) {
        txt += `<tr> 
            <td> ${tableData[x].roomNumber} </td> 
            <td> ${tableData[x].startTime} </td> 
            <td> ${tableData[x].endTime} </td> 
        </tr>`
    }
    table.innerHTML = txt;
    setInterval(() => {
        getData()
    }, 1)
}

