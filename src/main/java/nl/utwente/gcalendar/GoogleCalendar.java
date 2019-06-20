package nl.utwente.gcalendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
//Test calendar ID: student.utwente.nl_cq0u4f1tg89j5j8701qpj8bpec@group.calendar.google.com
//https://developers.google.com/calendar/quickstart/java
public class GoogleCalendar {


    GoogleCredential credentials;
    {
        try {
            credentials = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
                        .setJsonFactory(JacksonFactory.getDefaultInstance())
                        .setServiceAccountId("robo-695@sqills-room-booking.iam.gserviceaccount.com")
                        .setServiceAccountScopes(Arrays.asList("https://www.googleapis.com/auth/calendar"))
                        .setServiceAccountPrivateKeyFromP12File(new File("src/main/java/nl/utwente/keys/sqills-room-booking-9768f9c01f14.p12"))
                        .setServiceAccountUser("robo-695@sqills-room-booking.iam.gserviceaccount.com")
                        .build();
            Calendar client = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credentials).build();
            System.out.println(client.events().insert("student.utwente.nl_cq0u4f1tg89j5j8701qpj8bpec@group.calendar.google.com", ));

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GoogleCalendar gc = new GoogleCalendar();

    }

}
