package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.RoomDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidRoomNameException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoomServlet extends HttpServlet {
    @Override
    // Todo: @Marten Refactor names of variables.
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = (String) req.getSession().getAttribute(AuthenticationFilter.principalName);
        if (email==null){
            req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
        } else {
            String uri = req.getRequestURI();
            String[] splitUri = uri.split("/");
            String roomName = splitUri[3];
            req.setAttribute("id", roomName); // TODO maybe change "id" thing
            try {
                req.setAttribute("bookings", RoomDao.getBookingsForThisWeek(roomName,email));
                req.getRequestDispatcher("/desktop/room.jsp").forward(req, res);
            } catch (InvalidRoomNameException e) {
                res.setStatus(404);
                req.getRequestDispatcher("/desktop/404.jsp").forward(req, res);
                res.getWriter().write(e.getMessage());
            } catch (DAOException e) {
                res.setStatus(500);
                res.getWriter().write("Something went terribly wrong");
            }
        }
    }
}

