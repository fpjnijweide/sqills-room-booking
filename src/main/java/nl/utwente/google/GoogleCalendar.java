package nl.utwente.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Channel;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//Test calendar ID: student.utwente.nl_cq0u4f1tg89j5j8701qpj8bpec@group.calendar.google.com
//https://developers.google.com/calendar/quickstart/java
public class GoogleCalendar {
    private static final String APPLICATION_NAME = "sqills-room-booking";
    private static final String gcred = "{\n"+
            "  \"type\": \"service_account\",\n"+
            "  \"project_id\": \"sqills-room-booking\",\n"+
            "  \"private_key_id\": \"bc92a227d50dfa4a932a9a3f6a8ff54bb8fefb8e\",\n"+
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/huALl375dEft\\ncpT4gyJxZ/qfuVhodBV6CZWBBKQVELUcmN5kCpoXqU7V1BLCTmi1WWzhR6tlWem4\\nfY9FaHrSzKE/2GvumW8sYY+D+mFhxjcy3fLFrjmbRZsdrH7nVeJ2stItZoeeNqWL\\nNs6uV4Vl48c3jlOIXkWeaG6NyqcAfIuFwChf5+tAA3U11kkfb9u3aLGQueRdvntP\\nxIt9E/HChjEtlahZoOZltBZdiByimUtGH4DD5oNbE0D72+jEU1cKix3YmGK0P3m9\\nmjA/PalBn17fTB13mzVNSh+S09stYqlZ/mKB5CZ47vDWNrtKiHAwSztMnOeQvyi8\\nToyB0WFBAgMBAAECggEAGDoVaSH2u9NLreuKkIz19GZ0u3OUjVHxzYgc0z8lCk0c\\nu8tShlEZANk30bCL1uxYLmhrb2vDcISZHe80ClGXB8c+tnbH4FykRXbp0oX/f4C1\\nRsfwh7TsMInzfyNswhKJHP6tu3R2vzqmYh/CsSb9BtBzSMHGKz/RiqInKeRyj4r5\\nE8RjflyMUo1CnLiYtTe7qgwFeIYkzqJfbAACXIOA5lsLcFrJ3vKVSe14+pHIB5c9\\nzKO4PCBQtHl2Z0kW3Zq+ebW8UBtjv8nZznTrk35Y6bfwcO6sSgRAU3M8gNYzxjiy\\nZ4X89tmaZtRSTBkRrsZG8hhJu5l0VGWBxS+8DD33YQKBgQD8T0oWQaJVjKhlGa75\\nQGQPUPeEBSG8BJvXKuQJoweZdJ8Wd17cMJDYq4kxOQPumLSEmmNBiCQYbY5tCHG/\\n7bgLDF8X5cZzIVNXnd8vnkWz6hDZ9iTFxCZQSLEkLX69nNQXvfUAn3OxOyPRV7B+\\nSY9aiMgSTFlW+UC7kKiv8hMIXQKBgQDCU//h+68s26MKXeRRmzf+AEyBtXr3Gbtf\\n8r6W1ZsIBOccVgt+95P32RYdh375MMORQEkosgPb3VUPN+xovVu9/Wyf7IeUcJh1\\nnZPIeaEMQN0yrgOLjx/WwqZyh+WeI2kPeNIXUBwNt2D7iUpWNQn0OuXr7Dd2OW/I\\nrdsPwyDeNQKBgQDQECKsT9uqaJ/FH3TnHm5lIwO53vF+tt48haYfPE3cAgve3vle\\n4r3Pr0UnTjkr3MmgQCBxve7mJhI5X6lN+J64na8daeJItsafjhSqk/4u62qSEyUn\\n4SUnN4q8prfJbTaJjaAor1UcZgSXcZSdz9NjeUBjtopF6q2y/wvca+e+EQKBgQCi\\nY+ua/uW+SLSQfzHC+EDsI3fLlMpzH6+zWHjoNwxcwSwYQ9dLfLzMCz8t4gK7XoKv\\nKNd6CHitDOJ92y/VvxehRtu1MMWDyCu/bbUHfIyWXsX6CeywfSQQJY1OfD3nzpOG\\n+GIoVSt8UtfamznhnGT7ERT99+HJC+pb+tzq6RbYWQKBgFwmjASuz9N3/F3Zsqye\\nOigbJIXhEMsaD8/XhOW7E5NuIJTuRQZV5Q30zSlaub3CS1nCZTwGJgJAzlt/aa5d\\nT+dZ43NqIcfcdGOm0macqTYyBcLRr8r/FkBURAddmvymDyqp0BD0XLeqAp6Nsbg2\\nl+ljKG5fzUSVj20XQtTLyh44\\n-----END PRIVATE KEY-----\\n\",\n"+
            "  \"client_email\": \"robo-695@sqills-room-booking.iam.gserviceaccount.com\",\n"+
            "  \"client_id\": \"107174298481524852842\",\n"+
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n"+
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n"+
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n"+
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/robo-695%40sqills-room-booking.iam.gserviceaccount.com\"\n"+
            "}\n" ;
    private String roomID;
    private Calendar calendar;

    public GoogleCalendar(){
        this.calendar = initCalendar();
    }

    public Calendar initCalendar(){
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
    public boolean calendarExist(String calendarName){
        try {
            Calendar.CalendarList.Get result = this.calendar.calendarList().get(calendarName);
            if(result.getLastStatusCode() != 200){
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

//    public void createCalendar(String roomID){
//        this.calendar.calendars().insert(new com.google.api.services.calendar.model.Calendar(roomID));
//    }

//    public void addEvent(String calendarID, String title, Date date, Time startTime, Time endTime, String location) throws IOException {
//        if(!calendarExist(calendarID){
//            createCalendar();
//        }
//        this.calendar.events().insert(calendarID, this.getEvent(title, date, startTime, endTime, location));
//    }

    public static DateTime dateAndTimeToDateTime(Date date, Time time) {
        String myDate = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date utilDate = new java.util.Date();
        try {
            utilDate = sdf.parse(myDate);
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        DateTime dateTime = new DateTime(utilDate);

        return dateTime;
    }

    public Event getEvent(String title, Date date, Time startTime, Time endTime, String location){
        Event event = new Event();
        event.setStart(new EventDateTime().setDateTime(dateAndTimeToDateTime(date, startTime)));
        event.setEnd(new EventDateTime().setDateTime(dateAndTimeToDateTime(date, endTime)));
        event.setSummary(title);
        event.setLocation(location);

        return event;
    }

    private void setupWatchChannel(String calendarID) throws IOException {
        Channel channel = new Channel();
        channel.setId(UUID.randomUUID().toString());
        channel.setType("web_hook");
        channel.setAddress("https://booking.webrelay.io/api/booking/gc");
//        channel.setParams((Map<String, String>) new HashMap<>().put("ttl","30000"));
        Channel response =  this.calendar.events().watch(calendarID, channel).execute();
        System.out.println("Set up for id: "+calendarID);
        System.out.println(response);
    }

    public void setUpWatchers() throws IOException {

        List<CalendarListEntry> allCalendars = this.calendar.calendarList().list().execute().getItems();
        System.out.println("Cal list watcher");
        Channel channel = new Channel();
        channel.setId(UUID.randomUUID().toString());
        channel.setType("web_hook");
        channel.setAddress("https://booking.webrelay.io/api/booking/gc");
        Channel response  = this.calendar.calendarList().watch(channel).execute();
        System.out.println("Set up for calendars");
        System.out.println(response);

        for (int i = 0; i < allCalendars.size(); i++) {
            System.out.println(allCalendars.get(i).getSummary());
            String id = allCalendars.get(i).getId();
            System.out.println("Current Event: ");
            getCalendarEvent(id);
            System.out.println(id);
            setupWatchChannel(id);
        }
    }

    public void getCalendarEvent(String calendarID) throws IOException {
        List<Event> events = this.calendar.events().list(calendarID).execute().getItems();
        for (int i = 0; i < events.size(); i++) {
            System.out.println(events.get(i).getSummary());
        }
    }





}
