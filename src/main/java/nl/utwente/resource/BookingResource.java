package nl.utwente.resource;

import com.google.api.services.calendar.model.Event;
import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.BookingDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.google.GoogleCalendar;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;
import nl.utwente.model.User;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import static nl.utwente.authentication.AuthenticationHandler.*;
import static nl.utwente.dao.BookingDao.prepareBooking;
import static nl.utwente.dao.ParticipantDao.getParticipantsOfBooking;
import static nl.utwente.dao.UserDao.getUserFromContext;
import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/booking")
public class BookingResource {
    @Context HttpServletResponse response;

    @Context
    SecurityContext securityContext;

    @Context
    AuthenticationFilter authenticationFilter;

    @Context
    ContainerRequestContext context;


    /**
     * Returns a specific booking from the database.
     * @param bookingID ID of booking to be returned
     * @return JSON Object for the requested booking
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public OutputBooking getSpecificBooking(@PathParam("bookingID") int bookingID) {
        try {
            return BookingDao.getOutputBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }



    /**
     * Creates a booking.
     * @return Booking ID
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public int createBooking(@Valid SpecifiedBooking booking) {
        booking = prepareBooking(securityContext,booking);
        try {
            return BookingDao.createBooking(booking);
        } catch (BookingException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return 0;
    }

//    /**
//     * Create recurring booking
//     *  @return JSON object containing a "success" boolean field specifying whether the booking was
//     *  successfully created
//     */
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/create/recurring")
//    public int createRecurringBooking(RecurringBooking booking) {
//        if (BookingDao.validRecurringBooking(booking)) {
//            booking = (RecurringBooking) prepareBooking(securityContext, booking);
//            try {
//                return BookingDao.createRecurringBooking(booking);
//            } catch (BookingException e) {
//                throw400(e.getMessage());
//            } catch (DAOException e) {
//                throw500("Something went terribly wrong");
//            }
//        }
//
//        return 0;
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/participants")
    public List<User> getParticipants(@PathParam("bookingID") int bookingID) {
        try {
            return getParticipantsOfBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }


    /**
     * Updates a specified booking.
     * @param bookingID ID specifying the booking to be updated
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully updated
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public SpecifiedBooking updateBooking(
        @PathParam("bookingID") int bookingID,
        @Valid SpecifiedBooking booking
    ) {
        booking = prepareBooking(securityContext,booking);
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if (!userOwnsBooking(securityContext, bookingID) && !userIsAdmin(securityContext)) {
                throw403("You are not authorized to edit this person's booking");
            }
            BookingDao.updateBooking(bookingID, booking);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (BookingException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }


        return booking;
    }


    /**
     * Deletes a specific booking
     * @param bookingID Path parameter specifying the booking to be deleted
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully deleted
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public void deleteBooking(@PathParam("bookingID") int bookingID) {
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if (!userOwnsBooking(securityContext, bookingID) && !userIsAdmin(securityContext)) {
                throw403("You are not authorized to edit this person's booking");
            }
            BookingDao.deleteBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
    }

    /**
     * Checks if a user participates in / owns booking, or is admin
     * Which means the user is allowed to edit the booking (up to a certain point)
     * @param bookingID
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/access")
    public boolean userHasAccess(@PathParam("bookingID") int bookingID) {
        boolean userInParticipants = false;
        try {
            if (userIsAdmin(securityContext)) {
                return true;
            } else if (userOwnsBooking(securityContext, bookingID)) {
                return true;
            }
            List<User> participantsList = getParticipantsOfBooking(bookingID);
            User user = getUserFromContext(context); // TODO Freek check if this owrks
            userInParticipants = participantsList.contains(user);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (NullPointerException e) {
            throw401("User is not logged in");
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return userInParticipants;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/filter")
    public List<OutputBooking> getFilteredBookings(
        @QueryParam("title") String title,
        @QueryParam("email") String email,
        @QueryParam("startDate") Date startDate,
        @QueryParam("endDate") Date endDate
    ) {
        try {
            return BookingDao.getFilteredBookings(
                email,
                title,
                startDate,
                endDate
            );
            } catch (DAOException e) {
                throw400(e.getMessage());
            }

        return null;
    }

    /**
     *
     */
    @POST
    @Path("/google-calendar/push-notification-events")
    public void googleCalendarPushNotification(@HeaderParam("X-Goog-Channel-ID")String channelID, @HeaderParam("X-Goog-Resource-ID")String resourceID, @HeaderParam("X-Goog-Resource-URI") String resourceURI, @HeaderParam("X-Goog-Channel-Token") String token ){
        System.out.println("Change detected");
        GoogleCalendar gc = new GoogleCalendar();
        Event e  = null;
        try {
            gc.getLatestEvents(token);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Event not found");
        }
    }

    /**
     *
     */
    @Deprecated
    @GET
    @Path("/google-calendar/test/add-watchers")
    public void googleCalendarTestAddWatchers(){
        GoogleCalendar gc = new GoogleCalendar();
        try {
            gc.setUpWatchers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     */
    @Deprecated
    @GET
    @Path("/google-calendar/test/stop-watchers")
    public void googleCalendarTestCallRemoveWatchers(@HeaderParam("channelID")String channelID, @HeaderParam("resourceID") String resourceID){
        GoogleCalendar gc = new GoogleCalendar();
        try {
            gc.removeWatchChannel(channelID, resourceID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
