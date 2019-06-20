package nl.utwente.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ExceptionHandling {
    public static void throw404(String message) {
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type( MediaType.TEXT_PLAIN ).entity(message).build());
    }

    public static void throw400(String message) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).type( MediaType.TEXT_PLAIN ).entity(message).build());
    }

    public static void throw401(String message) {
        throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity(message).build());
    }

    public static void throw403(String message) {
        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build());
    }

    public static void throw500(String message) {
        throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(message).build());
    }

}
