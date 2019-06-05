package nl.utwente.servlet.desktopInterface;

import nl.utwente.dao.RoomDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoomServlet extends HttpServlet {
    @Override
    // Todo: @Marten Refactor names of variables.
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splitUri = uri.split("/");
        String roomName = splitUri[3];
        if (!RoomDao.isValidRoomName(roomName)) {
            res.setStatus(404);
            req.getRequestDispatcher("/desktop/404.jsp").forward(req, res);
        } else {
            req.setAttribute("id", roomName); // TODO maybe change "id" thing
            req.setAttribute("bookings", RoomDao.getBookingsForThisWeek(roomName));
            req.getRequestDispatcher("/desktop/room.jsp").forward(req, res);
        }
    }
}
