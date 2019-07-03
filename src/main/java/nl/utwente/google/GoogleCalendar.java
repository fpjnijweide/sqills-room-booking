package nl.utwente.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.RecurringBooking;
import nl.utwente.model.SpecifiedBooking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Test calendar ID: student.utwente.nl_cq0u4f1tg89j5j8701qpj8bpec@group.calendar.google.com
//https://developers.google.com/calendar/quickstart/java
public class GoogleCalendar {


    private static final String PUSH_NOTIFICATION_END_POINT_DEV = "https://booking.webrelay.io/sqillsRoomBooking/api/booking/google-calendar/push-notification-events";
    private static final String PUSH_NOTIFICATION_END_POINT_PROD = "https://bookroom.nl/sqillsRoomBooking/";

    private static final String APPLICATION_NAME = "sqills-room-booking";
    private static final String gcred = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"sqills-room-booking\",\n" +
            "  \"private_key_id\": \"bc92a227d50dfa4a932a9a3f6a8ff54bb8fefb8e\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/huALl375dEft\\ncpT4gyJxZ/qfuVhodBV6CZWBBKQVELUcmN5kCpoXqU7V1BLCTmi1WWzhR6tlWem4\\nfY9FaHrSzKE/2GvumW8sYY+D+mFhxjcy3fLFrjmbRZsdrH7nVeJ2stItZoeeNqWL\\nNs6uV4Vl48c3jlOIXkWeaG6NyqcAfIuFwChf5+tAA3U11kkfb9u3aLGQueRdvntP\\nxIt9E/HChjEtlahZoOZltBZdiByimUtGH4DD5oNbE0D72+jEU1cKix3YmGK0P3m9\\nmjA/PalBn17fTB13mzVNSh+S09stYqlZ/mKB5CZ47vDWNrtKiHAwSztMnOeQvyi8\\nToyB0WFBAgMBAAECggEAGDoVaSH2u9NLreuKkIz19GZ0u3OUjVHxzYgc0z8lCk0c\\nu8tShlEZANk30bCL1uxYLmhrb2vDcISZHe80ClGXB8c+tnbH4FykRXbp0oX/f4C1\\nRsfwh7TsMInzfyNswhKJHP6tu3R2vzqmYh/CsSb9BtBzSMHGKz/RiqInKeRyj4r5\\nE8RjflyMUo1CnLiYtTe7qgwFeIYkzqJfbAACXIOA5lsLcFrJ3vKVSe14+pHIB5c9\\nzKO4PCBQtHl2Z0kW3Zq+ebW8UBtjv8nZznTrk35Y6bfwcO6sSgRAU3M8gNYzxjiy\\nZ4X89tmaZtRSTBkRrsZG8hhJu5l0VGWBxS+8DD33YQKBgQD8T0oWQaJVjKhlGa75\\nQGQPUPeEBSG8BJvXKuQJoweZdJ8Wd17cMJDYq4kxOQPumLSEmmNBiCQYbY5tCHG/\\n7bgLDF8X5cZzIVNXnd8vnkWz6hDZ9iTFxCZQSLEkLX69nNQXvfUAn3OxOyPRV7B+\\nSY9aiMgSTFlW+UC7kKiv8hMIXQKBgQDCU//h+68s26MKXeRRmzf+AEyBtXr3Gbtf\\n8r6W1ZsIBOccVgt+95P32RYdh375MMORQEkosgPb3VUPN+xovVu9/Wyf7IeUcJh1\\nnZPIeaEMQN0yrgOLjx/WwqZyh+WeI2kPeNIXUBwNt2D7iUpWNQn0OuXr7Dd2OW/I\\nrdsPwyDeNQKBgQDQECKsT9uqaJ/FH3TnHm5lIwO53vF+tt48haYfPE3cAgve3vle\\n4r3Pr0UnTjkr3MmgQCBxve7mJhI5X6lN+J64na8daeJItsafjhSqk/4u62qSEyUn\\n4SUnN4q8prfJbTaJjaAor1UcZgSXcZSdz9NjeUBjtopF6q2y/wvca+e+EQKBgQCi\\nY+ua/uW+SLSQfzHC+EDsI3fLlMpzH6+zWHjoNwxcwSwYQ9dLfLzMCz8t4gK7XoKv\\nKNd6CHitDOJ92y/VvxehRtu1MMWDyCu/bbUHfIyWXsX6CeywfSQQJY1OfD3nzpOG\\n+GIoVSt8UtfamznhnGT7ERT99+HJC+pb+tzq6RbYWQKBgFwmjASuz9N3/F3Zsqye\\nOigbJIXhEMsaD8/XhOW7E5NuIJTuRQZV5Q30zSlaub3CS1nCZTwGJgJAzlt/aa5d\\nT+dZ43NqIcfcdGOm0macqTYyBcLRr8r/FkBURAddmvymDyqp0BD0XLeqAp6Nsbg2\\nl+ljKG5fzUSVj20XQtTLyh44\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"robo-695@sqills-room-booking.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"107174298481524852842\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/robo-695%40sqills-room-booking.iam.gserviceaccount.com\"\n" +
            "}\n";
    private String roomID;
    private Calendar calendar;

    public GoogleCalendar() {
        this.calendar = initCalendar();
    }

    public static DateTime dateAndTimeToDateTime(Date date, Time time) {
        String myDate = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date utilDate = new java.util.Date();
        try {
            utilDate = sdf.parse(myDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        DateTime dateTime = new DateTime(utilDate);

        return dateTime;
    }

    private static Time removeDateFromTime(EventDateTime eventDateTime) {
        try {
            Time time = new Time(eventDateTime.getDateTime().getValue());
            time.toString();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.YEAR, 1970);
            cal.set(java.util.Calendar.MONTH, 0);
            cal.set(java.util.Calendar.HOUR_OF_DAY, time.getHours());
            cal.set(java.util.Calendar.MINUTE, time.getMinutes());
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            Date d = cal.getTime();
            return new Time(d.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Calendar initCalendar() {
        HttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleCredential credential = GoogleCredential.fromStream(new ByteArrayInputStream(gcred.getBytes(StandardCharsets.UTF_8)))
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            return new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

//    public void createCalendar(String roomID){
//        this.calendar.calendars().insert(new com.google.api.services.calendar.model.Calendar(roomID));
//    }

    public void addEvent(String calendarName, String title, Date date, Time startTime, Time endTime, String location)  {
        String calendarId = getCalendarIDFromSummary(calendarName);
        if(calendarId != null){
            try {
                this.calendar.events().insert(calendarId,formatEvent(title,date,startTime,endTime,location)).execute();
            } catch (IOException e) {
                System.out.println("Failed to create google event: "+e.getMessage());
            }

        }
    }

    public void getResource(String resourceID) {
    }

    private String getCalendarIDFromSummary(String calendarSummary){
        try {
            for (CalendarListEntry calendar : this.calendar.calendarList().list().execute().getItems()) {
                if(calendar.getSummary().equals(calendarSummary)){
                    return calendar.getId();
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public boolean calendarExist(String calendarName) {
        try {
            Calendar.CalendarList.Get result = this.calendar.calendarList().get(calendarName);
            if (result.getLastStatusCode() != 200) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Event formatEvent(String title, Date date, Time startTime, Time endTime, String location) {
        Event event = new Event();
        event.setStart(new EventDateTime().setDateTime(dateAndTimeToDateTime(date, startTime)));
        event.setEnd(new EventDateTime().setDateTime(dateAndTimeToDateTime(date, endTime)));
        event.setSummary(title);
        event.setLocation(location);

        return event;
    }

    /**
     * TODO refactor !!
     *
     * @param calendarId
     * @return
     * @throws IOException
     */
    public Events getLatestEvents(String calendarId) throws IOException {
        String calendarName = this.calendar.calendars().get(calendarId).execute().getSummary();
        Calendar.Events.List request = this.calendar.events().list(calendarId);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.add(java.util.Calendar.YEAR, 1);
        DateTime max = new DateTime(c.getTime());
        String pageToken = null;
        Events events = null;
        String syncToken = BookingDao.getLatestGoogleCalendarSyncToken();
        if (syncToken != null) {
            request.setSyncToken(syncToken);
        }
        do {
            request.setPageToken(pageToken);

            try {
                events = request.execute();
            } catch (GoogleJsonResponseException e) {
                e.printStackTrace();
            }

            List<Event> items = events.getItems();
            System.out.println(items);
            if (items.size() == 0) {
                System.out.println("No new events to sync.");
            } else {
                for (Event event : items) {
                    try {
                        if (!event.getStatus().equals("cancelled")) {
                            SpecifiedBooking specifiedBooking = eventToBooking(calendarName, event);
                            try {
                                newBooking(event, specifiedBooking);
                            } catch (BookingException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Invalid booking removed");
                                removeEvent(calendarId, event, e);
                            } catch (DAOException | InvalidBookingIDException | InvalidEmailException | ParseException e){
                                System.out.println(e.getMessage());
                            }
                        } else{
                            cancelledBooking(calendarId, calendarName, event);
                        }
                    }catch(Exception e ){
                        System.out.println(e.getMessage());
                    }
                }
            }

            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        BookingDao.setGoogleCalendarSyncToken(events.getNextSyncToken());
        return events;
    }

    private void removeEvent(String calendarId, Event event, BookingException e) throws IOException {
        try {
            this.calendar.events().delete(calendarId, event.getId()).execute();
        } catch (GoogleJsonResponseException ex) {
            System.out.println(e.getMessage());
            System.out.println("Event Already Removed");
        }
    }

    private void cancelledBooking(String calendarId, String calendarName, Event event) throws IOException {
        Event cancelledEvent = this.getEvent(calendarId, event.getId());
        SpecifiedBooking specifiedBooking = eventToBooking(calendarName, cancelledEvent);
        try {
            System.out.println("Removing Participants");
            ParticipantDao.removeAllParticipants(BookingDao.getBookingId(specifiedBooking));
            System.out.println("Deleting a booking");
            BookingDao.deleteBooking(specifiedBooking);
        } catch (DAOException | InvalidBookingIDException e) {
            System.out.println(e.getMessage());
        }
    }

    private void newBooking(Event event, SpecifiedBooking specifiedBooking) throws ParseException, BookingException, DAOException, InvalidBookingIDException, InvalidEmailException {
        int createdBookingId = -1;
        if (event.getRecurrence() != null) {
            System.out.println("Recurring booking found");
            RecurringBooking recurringBooking = recPatternToRecurringBooking(event.getRecurrence().get(0));
            recurringBooking.setRoomName(specifiedBooking.getRoomName());
            recurringBooking.setDate(specifiedBooking.getDate());
            recurringBooking.setTitle(specifiedBooking.getTitle());
            recurringBooking.setEmail(specifiedBooking.getEmail());
            recurringBooking.setIsPrivate(specifiedBooking.getIsPrivate());
            recurringBooking.setStartTime(specifiedBooking.getStartTime());
            recurringBooking.setEndTime(specifiedBooking.getEndTime());
            createdBookingId = BookingDao.createRecurringBooking(recurringBooking);
            System.out.println("Recurring booking create with ID: "+createdBookingId);

        } else {
            createdBookingId = BookingDao.createBooking(specifiedBooking);
            System.out.println("Event added to DB");
        }
        if( event.getAttendees() != null){
            for (int i = 0; i < event.getAttendees().size(); i++) {
                ParticipantDao.addParticipantEmailToBooking(createdBookingId, event.getAttendees().get(i).getEmail());
            }
        }
    }



    private RecurringBooking recPatternToRecurringBooking(String recurrenceString) throws ParseException {
        RecurringBooking recurringBooking = new RecurringBooking();
        System.out.println(recurrenceString);
        String repeatType = matchRegex(recurrenceString, "(?<=FREQ=)(.*?)(?:;|$)");
        int repeatEvery = 1;
        try{
             repeatEvery = Integer.parseInt(matchRegex(recurrenceString, "(?<=INTERVAL=)(.*?)(?:;|$)"));
            recurringBooking.setRepeatEvery(repeatEvery);

        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
        try{
            String byDay = matchRegex(recurrenceString, "(?<=BYDAY=)(.*?)(?:;)?$");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.YEAR, 1);
            recurringBooking.setEndingAt(new java.sql.Date(cal.getTime().getTime()));
        }catch(Exception e){
            try {
                String endAt = matchRegex(recurrenceString, "(?<=UNTIL=)(.*?)(?:;|$)");
                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                recurringBooking.setEndingAt((java.sql.Date) df2.parse(endAt));
            }catch(Exception ex) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.YEAR, 1);
                recurringBooking.setEndingAt(new java.sql.Date(cal.getTime().getTime()));
            }
        }
        recurringBooking.setRepeatEveryType(convertRepeatTypeToEnum(repeatType));
        recurringBooking.setRepeatEvery(repeatEvery);
        return recurringBooking;
    }
    public static String convertRepeatTypeToEnum(String repeatType){
        switch (repeatType){
            case "MONTHLY":
                return  "month";
            case "WEEKLY":
                return "week";
            case "YEARLY":
                return "year";
                default:
                //TODO handle correctly
                return "day";
        }
    }
    /**
     * Returns first match of regex on a target string
     * @param target
     * @param regex
     * @return
     */
    private String matchRegex(String target, String regex){
        Pattern frePattern = Pattern.compile(regex);
        Matcher matchFreq = frePattern.matcher(target);
        matchFreq.find();
        if(matchFreq.groupCount() < 1){
            return "";
        }
        return matchFreq.group(1);
    }



    /*
    Change this to actually match on the name
     */
    private SpecifiedBooking eventToBooking(String calendarName, Event event) {
        SpecifiedBooking specifiedBooking = new SpecifiedBooking();
        specifiedBooking.setEmail(event.getCreator().getEmail());
        specifiedBooking.setDate(new java.sql.Date(event.getStart().getDateTime().getValue()));
        specifiedBooking.setEndTime(removeDateFromTime(event.getEnd()));
        specifiedBooking.setStartTime(removeDateFromTime(event.getStart()));
        specifiedBooking.setRoomName(calendarName);
        specifiedBooking.setTitle(event.getSummary());
        specifiedBooking.setIsPrivate(false);
        return specifiedBooking;
    }


    public Event getEvent(String calendarId, String eventResourceId) throws IOException {
        this.calendar.calendarList().get(calendarId).execute();
        return this.calendar.events().get(calendarId, eventResourceId).execute();
    }


    /*
    Watcher set up not called by proper program
     */
    private void setupWatchChannel(String calendarID) throws IOException {
        Channel channel = new Channel();
        channel.setId(UUID.randomUUID().toString());
        channel.setType("web_hook");
        channel.setToken(calendarID);
        channel.setAddress(PUSH_NOTIFICATION_END_POINT_PROD);
        Channel response = this.calendar.events().watch(calendarID, channel).execute();
        System.out.println("Set up for id: " + calendarID);
        System.out.println(response);
    }

    public void removeWatchChannel(String channelID, String resourceID) throws IOException {
        Channel channel = new Channel();
        channel.setId(channelID);
        channel.setResourceId(resourceID);
        Object response = this.calendar.channels().stop(channel).execute();
        System.out.println(response);
    }

    public void setUpWatchers() throws IOException {

        List<CalendarListEntry> allCalendars = this.calendar.calendarList().list().execute().getItems();
        for (int i = 0; i < allCalendars.size(); i++) {
            System.out.println(allCalendars.get(i).getSummary());
            String id = allCalendars.get(i).getId();
            setupWatchChannel(id);
        }
        System.out.println("Google Calendar Resource watchers set up");
    }


    public void getCalendarEvent(String calendarID) throws IOException {
        List<Event> events = this.calendar.events().list(calendarID).execute().getItems();
        for (int i = 0; i < events.size(); i++) {
            System.out.println(events.get(i).getSummary());
        }
    }

    public static void main(String[] args) {
        GoogleCalendar gc = new GoogleCalendar();
        try {
            gc.setUpWatchers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
