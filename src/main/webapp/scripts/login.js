function login(){
    console.log("here")
    let password = document.getElementById("password").value;
    let username = document.getElementById("username").value;
    if (validInput(password) && validInput(username)){
        let jsonBody = {"username":  username,  "password": password};
        axios.post(`/api/user/login`, jsonBody).then((response) => {
            console.log(response);
            console.log(document.cookie);
            window.location="/desktop";


        }).catch((response) => {
            textfield = document.getElementById("response");
            textfield.innerHTML="<p class='text-danger'>Invalid login data</p>"
        })
    }
}

function validInput(string){
    if (string.length > 0){
        return true;
    } else {
        return false;
    }
}