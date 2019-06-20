//package nl.utwente.google;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//
//public class GoogleAuth {
//
//
//    private static final String CLIENT_ID = ;
//    HttpTransport httpTransport;
//
//    {
//        try {
//            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            Gson gson = new Gson();
//            JsonParser parser = new JsonParser();
//
//            JsonElement jsontree = parser.parse(new FileReader("src/main/java/nl/utwente/keys/credentials.json"));
//
//            JsonObject je = jsontree.getAsJsonObject();
//            System.out.println();
//
//
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
//                    // Specify the CLIENT_ID of the app that accesses the backend:
//                    .setAudience(Collections.singletonList(je.getAsJsonObject("web").get("client_id"))
//                    // Or, if multiple clients access the backend:
//                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//                    .build();
//
//            // (Receive idTokenString by HTTPS POST)
//
//            GoogleIdToken idToken = verifier.verify(idTokenString);
//            if(idToken !=null)
//
//            {
//                GoogleIdToken payload = idToken.getPayload();
//
//                // Print user identifier
//                String userId = payload.getSubject();
//                System.out.println("User ID: " + userId);
//
//                // Get profile information from payload
//                String email = payload.getEmail();
//                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//                String name = (String) payload.get("name");
//                String pictureUrl = (String) payload.get("picture");
//                String locale = (String) payload.get("locale");
//                String familyName = (String) payload.get("family_name");
//                String givenName = (String) payload.get("given_name");
//
//                // Use or store profile information
//                // ...
//
//            } else
//
//            {
//                System.out.println("Invalid ID token.");
//            }
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void main(String[] args) throws FileNotFoundException {
////        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
////        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
////        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//
//        JsonElement jsontree = parser.parse(new FileReader("src/main/java/nl/utwente/keys/credentials.json"));
//
//        JsonObject je = jsontree.getAsJsonObject();
//        System.out.println(je.getAsJsonObject("web").get("client_id"));
//    }
//
//}
