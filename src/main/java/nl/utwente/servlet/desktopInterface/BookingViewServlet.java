package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.dao.UserDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.OutputBookingWithParticipants;
import nl.utwente.model.SpecifiedBooking;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nl.utwente.dao.UserDao.getUserFromEmail;
import static nl.utwente.dao.UserDao.userHasAccess;

public class BookingViewServlet extends HttpServlet {
    @SuppressWarnings("Duplicates")
    @Override
    // Todo: @Marten Refactor names of variables.
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = (String) req.getSession().getAttribute(AuthenticationFilter.principalName);
        if (email == null) {
            req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
        } else {
//            if (BookingDao.)
            String uri = req.getRequestURI();
            String[] splitUri = uri.split("/");
            String bookingID = splitUri[3];
            req.setAttribute("id", bookingID); // TODO maybe change "id" thing
            try {
                if (userHasAccess(getUserFromEmail(email), Integer.parseInt(bookingID))){
                        OutputBooking booking = BookingDao.getOutputBooking(Integer.valueOf(bookingID));
                        req.setAttribute("booking", booking);
                        req.setAttribute("email", UserDao.getEmailFromUsername(booking.getUserName()));
                        req.getRequestDispatcher("/desktop/booking.jsp").forward(req, res);

                } else {
                    res.setStatus(403);
                    res.getWriter().write("You are not authorized to view this booking");
                }
            } catch (InvalidBookingIDException e) {
                res.setStatus(404);
                res.getWriter().write(e.getMessage());
            } catch (DAOException e) {
                res.setStatus(500);
                res.getWriter().write("Something went terribly wrong");
            } catch (InvalidEmailException e) {
                res.setStatus(404);
                res.getWriter().write(e.getMessage());
            }
        }
    }
}

