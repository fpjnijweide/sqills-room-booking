document.onload = getRoomBookings();

function getRoomBookings() {
    axios.get('/api/room/1').then((response) =>{
        setUpTable(response.data);
    });
}

function setUpTable(bookingData){

}


