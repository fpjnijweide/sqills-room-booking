function filterUsers() {
    let email = document.getElementById("filter-email").value;
    let adminFilter = document.getElementById("filter-admin").value;

    showAllUsers();
    hideUsersBasedOnEmail(email);
    hideUsersBasedOnAdmin(adminFilter);
}

function showAllUsers() {
    let elements = document.getElementsByClassName("user-row");

    for (let i = 0; i < elements.length; i++) {
        elements[i].classList.remove("hide");
    }
}

function hideUsersBasedOnEmail(email) {
    if (email === '') {
        return;
    }

    let elements = document.getElementsByClassName("user-row");

    for (let i = 0; i < elements.length; i++) {
        let element = elements[i];

        if (element.getAttribute("email") !== email) {
            element.classList.add("hide")
        }
    }
}

function hideUsersBasedOnAdmin(adminFilter) {
    console.log(adminFilter);
    let elements = document.getElementsByClassName("user-row");

    if (adminFilter === 'admin-and-non-admin') {
        return
    } else if (adminFilter === 'only-admin') {
        for (let i = 0; i < elements.length; i++) {
            let element = elements[i];
            if (!element.hasAttribute("data-admin")) {
                element.classList.add("hide")
            }
        }
    } else if (adminFilter === 'only-non-admin') {
        for (let i = 0; i < elements.length; i++) {
            let element = elements[i];
            if (element.hasAttribute("data-admin")) {
                element.classList.add("hide")
            }
        }
    }
}

function showUser(userID) {
    axios.get(`/api/user/u/${userID}`)
        .then(response => {
            document.getElementById("user-id").innerText = response.data.userid;
            document.getElementById("user-name").value = response.data.name;
            document.getElementById("user-email").value = response.data.email;
            if (response.data.administrator) {
                document.getElementById("user-admin").setAttribute("checked", "true");
            } else {
                document.getElementById("user-admin").removeAttribute("checked");

            }
        });
}

function deleteUser() {
    let userID = document.getElementById("user-id").innerText;
    console.log(userID);
    axios.delete(`/api/user/${userID}`)
        .then(response => {
            alert("Deleted User");
        })
        .catch(error => {
            alert(error);
        })
}