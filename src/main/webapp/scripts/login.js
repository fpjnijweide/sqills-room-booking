function login(){
    console.log("here")
    let password = document.getElementById("password").value;
    let username = document.getElementById("username").value;
    if (validInput(password) && validInput(username)){
        let jsonBody = {"username":  username,  "password": password};
        axios.post(`/api/user/login`, jsonBody).then((response) => {
            console.log(response);
            console.log(document.cookie);
            axios.get(`/api/user/test`, jsonBody).then((response) => {

            })
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