function logout(){
    axios.post(`/api/user/logout`).then((response) => {
        location.reload();

    })
}