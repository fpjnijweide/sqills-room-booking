function login() {
    let password = document.getElementById("password").value;
    let username = document.getElementById("username").value;
    if (validInput(password) && validInput(username)) {
        let jsonBody = {"username": username, "password": password};
        axios.post(`/api/user/login`, jsonBody).then((response) => {
            window.location = "/desktop";


        }).catch((response) => {
            textfield = document.getElementById("response");
            textfield.innerHTML = "<p class='text-danger'>Invalid login data</p>";
        });
    }

}

function onSignIn(googleUser) {
    var options = new gapi.auth2.SigninOptionsBuilder(
        {'scope': 'https://www.googleapis.com/auth/calendar.events'});
    googleUser.grant(options).then(
        function (success) {
            console.log(JSON.stringify({message: "success", value: success}));
        },
        function (fail) {
            alert(JSON.stringify({message: "fail", value: fail}));
        });
    axios.post('/api/user/googleauth', googleUser.getAuthResponse().id_token).then((response
    ) => {
        window.location = "/desktop";
    });

}


function validInput(string) {
    if (string.length > 0) {
        return true;
    } else {
        return false;
    }
}

function onLoad() {
    gapi.load('auth2', function () {
        gapi.auth2.init();
    });
}