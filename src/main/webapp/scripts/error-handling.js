function showError(message){
    console.log(message)
    $("#error-text").text(message)
    $("#errorModal").modal('show');

    if ($("#exampleModalLong").length > 0) {
        $("#exampleModalLong").modal('hide');

    }
}