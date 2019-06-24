package nl.utwente.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

//Test calendar ID: student.utwente.nl_cq0u4f1tg89j5j8701qpj8bpec@group.calendar.google.com
//https://developers.google.com/calendar/quickstart/java
public class GoogleCalendar {
    private static final String APPLICATION_NAME = "sqills-room-booking";

    private String calendarId;



    GoogleCalendar(String calendarId){
        this.calendarId = calendarId;
        initCalendar();

    }

    public void initCalendar(){
        HttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("src/main/java/nl/utwente/keys/sqills-room-booking-bc92a227d50d.json"))
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME).build();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void createEvent(String title, Date startTime, Date endTime){
        Event e = new Event();
        EventDateTime startDt = new EventDateTime();
        DateTime dt = new DateTime(startTime);
        startDt.setDateTime(dt);
        e.setStart(startDt);
        EventDateTime endDt = new EventDateTime();
        dt = new DateTime(endTime);
        endDt.setDateTime(dt);
        e.setEnd(endDt);
        e.setSummary(title);
    }



}
