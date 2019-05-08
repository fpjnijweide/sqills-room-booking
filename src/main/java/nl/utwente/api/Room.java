package nl.utwente.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/room")
public class Room {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHi() {
        return "hi";
    }
}
