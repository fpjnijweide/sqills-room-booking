// import axios from 'axios'

var myObj

function selectRoom(){
    let inputfield = document.getElementById("room-input")
    let roomnr = inputfield.value
    let roomNumber = roomnr
    let inputform = document.getElementById("input-form")
    // inputform.innerHTML=""
    getData(roomNumber)
}

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

//
//
//
// function loadBooks(event) {
//
//
//     if (event.key == "Enter") {
//         let search = document.getElementById("search")
//
//         let items = document.getElementById("items")
//         var xhr = new XMLHttpRequest()
//         xhr.onreadystatechange = function () {
//             if (this.readyState == 4) {
//                 if (this.status == 200) {
//                     myObj = JSON.parse(this.responseText)
//                     let txt = "<table border='1'>"
//                     for (x in myObj) {
//                         txt += "<tr>" +
//                             "<td>" + myObj[x].shortDescription + "</td>" +
//                             "<td>" + myObj[x].cost + "</td>" +
//                             "<td><button id=\"" + myObj[x].itemID + "\" onclick=\"addToCart(myObj[" + x +
//                             "])\">Add to cart</button></td>"
//                         "</tr>"
//                     }
//                     txt += "</table>"
//                     items.innerHTML = txt
//                 } else {
//                     items.innerHTML = ""
//                 }
//             }
//
//         }
//
//
//         // xhr.open("POST",) 2isgyhfiewiueiew abbb ab ab ab
//         xhr.open("GET", "./rest/" + search.value, true)
//         xhr.send()
//         search.value = ""
//
//     }
//
//
// }
//
//
// function showCart() {
//
//     let costs = document.getElementById("costs")
//
//     let cartcontents = document.getElementById("cartcontents")
//     let xhr = new XMLHttpRequest()
//     xhr.onreadystatechange = function () {
//         if (this.readyState == 4) {
//             if (this.status == 200) {
//                 let myObj = JSON.parse(this.responseText)
//                 let txt = "<table border='1'>"
//                 let totcosts = 0
//                 for (x in myObj) {
//                     totcosts += myObj[x].item.cost * myObj[x].numItems
//                     txt += "<tr>" +
//                         "<td>" + myObj[x].item.shortDescription + "</td>" +
//                         "<td>" + myObj[x].item.cost + "</td>" +
//                         "<td>" + myObj[x].numItems + "</td>" +
//                         "</tr>"
//                 }
//                 txt += "</table>"
//                 cartcontents.innerHTML = txt
//                 costs.innerHTML = "Total costs = " + totcosts + " Euros"
//             } else {
//                 cartcontents.innerHTML = ""
//                 costs.innerHTML = "Total costs = 0.0 Euros"
//             }
//         }
//
//     }
//     xhr.open("GET", "./rest/cart", true)
//     xhr.send()
//
// }
//
// function addToCart(object) {
//     let new_object = [{
//         item: object,
//         numItems: 1
//     }]
//
//
//     let object_string = JSON.stringify(new_object)
//     let xhr = new XMLHttpRequest()
//     xhr.onreadystatechange = function () {
//         if (this.readyState == 4) {
//             if (this.status == 204) {
//                 showCart()
//             }
//         }
//     }
//     xhr.open("POST", "./rest/cart/", true)
//     xhr.setRequestHeader("Content-Type", "application/json")
//     xhr.send(object_string)
//
//
// }
