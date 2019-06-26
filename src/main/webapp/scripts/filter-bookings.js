function filterBookings() {
    let params = {
        "email": document.getElementById("filter-email").value,
        "title": document.getElementById("filter-title").value,
    };

    if (document.getElementById("filter-startdate").value === '') {
        params.startDate = "1970-01-01";
    } else {
        params.startDate = document.getElementById("filter-startdate").value;
    }

    if (document.getElementById("filter-enddate").value === '') {
        params.endDate = "2050-01-01";
    } else {
        params.endDate = document.getElementById("filter-enddate").value;
    }

    axios.get('/api/booking/filter', {"params": params})
        .then(response => {
            let bookings = response.data;
            let tableBodyContainer = document.getElementById("bookings-table-body");
            tableBodyContainer.innerHTML = "";
            for (let i = 0; i < bookings.length; i++) {
                let booking = bookings[i];
                tableBodyContainer.innerHTML += `
                <tr onclick="redirectToBookingPage(${booking.bookingid})" class="booking-row-clickable">
                    <td>${booking.title}</td>
                    <td>${booking.userName}</td>
                    <td>${booking.date}</td>
                    <td>${booking.startTime}</td>
                    <td>${booking.endTime}</td>
                </tr>
                `
            }
        })
}

function redirectToBookingPage(bookingID) {
    window.location = `/desktop/booking/${bookingID}`;
}