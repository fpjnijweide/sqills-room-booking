function onLoadGAPI() {
    gapi.load('client');
}

async function initGCalendar() {
    let CLIENT_ID = '347026751328-idgjfem8sg70oq9kdf3ivarntlhbkrvk.apps.googleusercontent.com'
        , API_KEY = 'AIzaSyBEYUE-ZZR0vDAfWaYhB6KRiNY1zNcLWSY'
        , DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"]
        , SCOPES = "https://www.googleapis.com/auth/calendar";
    await gapi.client.init({
        apiKey: API_KEY,
        clientId: CLIENT_ID,
        discoveryDocs: DISCOVERY_DOCS,
        scope: SCOPES
    });
}

function createGCalendarEvent(roomName, date, startTime, endTime, title, participants, isPrivate, recurrence) {
    events = {
        'summary': title,
        'location': roomName,
        'description': null,
        'start': {
            'dateTime': `${date}T${startTime}`,
            'timeZone': 'Europe/Amsterdam'
        },
        'end': {
            'dateTime': `${date}T${endTime}`,
            'timeZone': 'Europe/Amsterdam'
        },
        'recurrence': recurrence,
        'attendees': participants,
    };
    console.log(events);
    return events;

}


function createRecurrence(repeatEveryType, repeatEvery, endAt) {
    return [`RRULE:FREQ=${changeRepeatTypeToGoogleApi(repeatEveryType.toUpperCase())};INTERVAL=${repeatEvery};UNTIL=${new Date(endAt).toISOString()}`];
}

function changeRepeatTypeToGoogleApi(repeatEveryType) {
    switch (repeatEveryType.toUpperCase()) {
        case "DAY":
            return "DAILY";
        case "MONTH":
            return "MONTHLY";
        default:
            return "YEARLY";
    }
}

async function insertGCalendarEvent(event) {
    await initGCalendar();
    gapi.client.calendar.events.insert({
        'calendarId': 'primary',
        'resource': event
    }).execute();

}

module.exports = {
    createGCalendarEvent: createGCalendarEvent
};