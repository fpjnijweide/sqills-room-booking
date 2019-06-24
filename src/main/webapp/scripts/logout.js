function logout(){
    let auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
            axios.post(`/api/user/logout`).then((response) => {
                location.reload();
            });
    })
}
// Required to load auth2 lib correctly
function onLoad() {
    gapi.load('auth2', function () {
        gapi.auth2.init();
    });
}