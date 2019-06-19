package nl.utwente.gcalendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

//https://developers.google.com/calendar/quickstart/java
public class GoogleCalendar {


    GoogleCredential credentials;
    {
        try {
            credentials = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
                        .setJsonFactory(JacksonFactory.getDefaultInstance())
                        .setServiceAccountId("robo-695@sqills-room-booking.iam.gserviceaccount.com")
                        .setServiceAccountScopes(Arrays.asList("https://www.googleapis.com/auth/calendar"))
                        .setServiceAccountPrivateKeyFromP12File(new File("src/main/java/nl/utwente/keys/Sqills-Room-Booking-8ca7a41f5055.json"))
                        .setServiceAccountUser("robo-695@sqills-room-booking.iam.gserviceaccount.com")
                        .build();
            Calendar client = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credentials).build();

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
