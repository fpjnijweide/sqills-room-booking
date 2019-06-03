    window.onload = () => {
    axios.get('/api/room/available')
        .then(response => {
            displayAvailableRooms(response.data);
        });
};

function displayAvailableRooms(IDs) {
    let table = document.getElementById("available-ids");

    for (let i = 0; i < IDs.length; i++) {
        table.innerHTML += "<tr class='clickable-row' data-href='desktop/room/" + IDs[i] + "'>" +
            "<td class='room-name'><a href='/desktop/room/" + IDs[i] + "'>Room " + IDs[i] + "</a></td>" +
            "<td class='available' id='time-" + IDs[i] + "'>" + "</td>" +
            "<tr>";
    }

    setAvailableTimes(IDs);
}

function setAvailableTimes(IDs) {
    for (let i = 0; i < IDs.length; i++) {
        axios.get('/api/room/' + IDs[i] + '/availableUntil')
            .then(response => {
               console.log(response.data);
               let elem = document.getElementById('time-' + IDs[i]);
               if (!response.data) {
                   elem.innerHTML = "Entire Day"
               } else {
                   elem.innerHTML = response.data;
               }
            });
    }
}