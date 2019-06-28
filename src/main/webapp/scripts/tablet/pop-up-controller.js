function blurBackground() {
    document.getElementsByClassName("blur-container")[0].classList.add("blur");
}

function unblurBackground() {
    document.getElementsByClassName("blur-container")[0].classList.remove("blur");
}

function showCreationPopUp() {
    document.getElementById("make-booking-overlay").classList.remove("hidden");
}

function hideCreationPopUp() {
    document.getElementById("make-booking-overlay").classList.add("hidden");
}

function openDetailsPopUp(bookingID) {
    axios.get(`/api/booking/${bookingID}`)
        .then(response => {
            document.getElementById("pop-up-title").innerText = response.data.title;
            document.getElementById("pop-up-owner").innerHTML = `<span class="booked-by">Booked by ${response.data.userName}`;
            document.getElementById("pop-up-time").innerHTML = response.data.startTime + " - " + response.data.endTime;

            let participants = response.data.participants;
            if (participants.length !== 0) {
                let participantElement = document.getElementById("participants");
                participantElement.innerHTML = "<h4>Participants</h4>";

                for (let i = 0; i < participants.length; i++) {
                    participantElement.innerHTML += `<li>${participants[i].name}</li>`;
                }
            }
        })
        .finally(() => {
            document.getElementById("booking-details-overlay").classList.remove("hidden");
            blurBackground();
        });
}

function closeDetailsPopUp() {
    document.getElementById("booking-details-overlay").classList.add("hidden");
    unblurBackground();
}