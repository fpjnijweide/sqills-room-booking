package nl.utwente.api.room;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/room")
public class Room {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHi() {
        return "hi";
    }

    @GET
    @Path("/{roomNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(
        @PathParam("roomNumber") Integer roomNumber,
        @Context UriInfo uriInfo
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode bookings = factory.arrayNode();

        ObjectNode booking1 = factory.objectNode();
        booking1.put("roomNumber", roomNumber);
        booking1.put("startTime", "12:00");
        booking1.put("endTime", "13:00");
        bookings.add(booking1);

        ObjectNode booking2 = factory.objectNode();
        booking2.put("roomNumber", roomNumber);
        booking2.put("startTime", "9:00");
        booking2.put("endTime", "11:00");
        bookings.add(booking2);

        ObjectNode booking3 = factory.objectNode();
        booking3.put("roomNumber", roomNumber);
        booking3.put("startTime", "15:00");
        booking3.put("endTime", "17:00");
        bookings.add(booking3);


        return bookings.toString();
    }
}
