document.onload = getData();

function getData() {
    let roomNumber = 1;
    axios.get(`/api/room/${roomNumber}`).then((response) => {
        generateTable(response.data);
    })
}

function generateTable(tableData) {
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
        getData();
    }, 30000)
}

