
function initGCalendar(){
    var CLIENT_ID = '347026751328-idgjfem8sg70oq9kdf3ivarntlhbkrvk.apps.googleusercontent.com';
    var API_KEY = 'AIzaSyBEYUE-ZZR0vDAfWaYhB6KRiNY1zNcLWSY';
    var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
    var SCOPES = "https://www.googleapis.com/auth/calendar";
    gapi.load('client', () => {
        gapi.client.init({
            apiKey: API_KEY,
            clientId: CLIENT_ID,
            discoveryDocs: DISCOVERY_DOCS,
            scope: SCOPES
        })
    });
}
function createGCalendarEvent(roomName, date, startTime, endTime, title, participants, isPrivate, recurrence) {
    event =  {
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
    }
    console.log(event);
    return event;

}
function insertGCalendarEvent(event){
    initGCalendar();
    gapi.client.calendar.events.insert({
        'calendarId': 'primary',
        'resource': event
    }).execute();
}

