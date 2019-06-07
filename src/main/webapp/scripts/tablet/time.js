function updateTime() {
    let element = document.getElementById("time");

    let date = new Date();
    let hours = date.getHours();
    let minutes = date.getMinutes();

    element.innerHTML = hours + ":" + minutes;
    let timeout = setTimeout(updateTime, 30000);
}