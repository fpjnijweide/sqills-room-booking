package nl.utwente.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuth {


    private static final String CLIENT_ID = "347026751328-n650qv0b0v1qjmmnk16vddsae05rqp4v.apps.googleusercontent.com";

    public static GoogleIdTokenVerifier getVerifier() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
            .setAudience(Collections.singletonList(CLIENT_ID)).build();
    }


    public static String getUser(GoogleIdToken idToken) {
        return idToken.getPayload().getEmail();
    }

    public static GoogleIdToken getToken(String tokenString) throws GeneralSecurityException, IOException {
        return getVerifier().verify(tokenString);
    }


}
