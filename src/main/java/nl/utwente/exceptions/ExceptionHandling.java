package nl.utwente.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ExceptionHandling {
    public static void throw404(Exception e) {
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type( MediaType.TEXT_PLAIN ).entity(e.getMessage()).build());
    }

    public static void throw400(Exception e) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).type( MediaType.TEXT_PLAIN ).entity(e.getMessage()).build());
    }

    public static void throwForbidden() {
        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("").build());
    }
}
