function positionTimeBars(){
    let timeBars = document.getElementsByClassName("timestamp");
    console.log(timeBars)
    for (let i = 0; i < timeBars.length; i++){
        let timeBar = timeBars[i];
        let time = timeBar.getAttribute("value");
        let margin = time / 24 * 100;
        timeBar.style.left = "calc((" + margin + "%) - 1px)";
    }
}

function addBooking(startTime, endTime, date){
    let startHours = parseInt(startTime.split(":")[0]);
    let startMinutes = parseInt(startTime.split(":")[1]);
    let endHours = parseInt(endTime.split(":")[0]);
    let endMinutes = parseInt(endTime.split(":")[1]);
    let start = startHours + (startMinutes / 60);
    let end = endHours + (endMinutes / 60);
    let duration = end - start;
    let parent= document.getElementsByClassName("bar")[0];
    let booking = document.createElement("div");
    booking.className = "booking";
    booking.style.position = "absolute";
    booking.style.top = "0";
    booking.style.left = start / 24 * 100 + "%";
    booking.style.width = duration / 24 * 100 + "%";
    booking.style.backgroundColor = "#FF7212";
    booking.style.display = "block";
    booking.style.height = "100%";
    parent.appendChild(booking)
}

function checkIfBookingToday(date) {
    let parts = date.split("-");
    let thedate = new Date(parts[0], parts[1] - 1, parts[2]);
    let today = new Date();
    today.setHours(0,0,0,0);
    if (thedate > today || thedate < today) {
        return false;
    } else {
        return true;
    }
}

function checkIsFromPastDays(date){
    let parts = date.split("-");
    let thedate = new Date(parts[0], parts[1] - 1, parts[2]);
    let today = new Date();
    today.setHours(0,0,0,0);
    if (thedate >= today) {
        return false;
    } else {
        return true;
    }
}

function checkIfFormAndOpen(){
    if ($("#exampleModalLong").length > 0) {
        $("#exampleModalLong").modal('show');
    }
}

module.exports = {
    checkIsFromPastDays: checkIsFromPastDays,
    checkIfBookingToday: checkIfBookingToday
}