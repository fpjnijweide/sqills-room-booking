function updateTime() {
    let element = document.getElementById("time");

    let date = new Date();
    let hours = date.getHours();
    let hourString = hours.toString();
    let minutes = date.getMinutes();
    let minuteString = minutes.toString();

    if (hours < 10) {
        hourString = "0" + hourString;
    }

    if (minutes < 10) {
        minuteString = "0" + minuteString
    }
    element.innerHTML = hourString + ":" + minuteString;

    console.log(available_rooms);

    let timeout = setTimeout(updateTime, 30000);
}