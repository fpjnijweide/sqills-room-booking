/*
There will be multiple points in the tablet front-end that need the availability information of rooms.
For instance the availability display, but also the alternatives if the room is booked.
To avoid multiple API calls for the same, there is one periodical API call which sets a global variable available_rooms
This variable is set inside setAvailableRoomsAndUpdatePage.
The updating of the page is handled by separate functions, using this global variable.
Each of these functions is called in the finally clause of setAvailableRoomsAndUpdatePage.
 */

function updateAvailabilityInterval() {
    setAvailableRoomsAndUpdatePage();

    setTimeout(updateAvailabilityInterval, 30000);
}

function setAvailableRoomsAndUpdatePage() {
    axios.get("/api/room/available")
        .then(response => {
            available_rooms = response.data
        })
        .finally( () => {
                updateAvailabilityDisplay();
            }
        );
}

function updateAvailabilityDisplay() {
    let element = document.getElementById("availability");

    let isAvailable = available_rooms.includes(ROOM_NAME);

    // Disable or enable the book button depending on availability
    if (isAvailable) {
        document.getElementById("book-button").classList.remove("disable");
    } else {
        document.getElementById("book-button").classList.add("disable");
    }

    // Only update the contents of the page, if the availability changed.
    if ((isAvailable && element.innerText === "AVAILABLE")
        || (!isAvailable && element.innerText === "UNAVAILABLE")) {
        return;
    } else if (isAvailable) {
        element.innerText = "AVAILABLE";
    } else {
        element.innerText = "UNAVAILABLE";
    }
}