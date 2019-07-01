function logout(){
    let auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
            axios.post(`/sqillsRoomBooking/api/user/logout`).then((response) => {
                location.reload();
            });
    })
}
// Required to load auth2 lib correctly
