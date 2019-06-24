package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidRoomNameException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookingViewServlet extends HttpServlet {
    @SuppressWarnings("Duplicates")
    @Override
    // Todo: @Marten Refactor names of variables.
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getSession().getAttribute(AuthenticationFilter.principalName)==null){
            req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
        } else {
            String uri = req.getRequestURI();
            String[] splitUri = uri.split("/");
            String bookingID = splitUri[3];
            req.setAttribute("id", bookingID); // TODO maybe change "id" thing
            try {
                req.setAttribute("booking", BookingDao.getOutputBooking(Integer.valueOf(bookingID)));
            } catch (InvalidBookingIDException e) {
                res.setStatus(404);
                req.getRequestDispatcher("/desktop/404.jsp").forward(req, res);
                res.getWriter().write(e.getMessage());
                req.getRequestDispatcher("/desktop/booking.jsp").forward(req, res);
            }
        }
    }
}

