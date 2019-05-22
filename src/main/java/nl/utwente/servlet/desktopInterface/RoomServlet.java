package nl.utwente.servlet.desktopInterface;

import nl.utwente.dao.RoomDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splitUri = uri.split("/");
        // Todo: Add validation of URI.
        int roomID = Integer.valueOf(splitUri[3]);
        if (!RoomDao.isValidRoomID(roomID)) {
            res.setStatus(404);
            req.getRequestDispatcher("/desktop/404.jsp").forward(req, res);
        } else {
            req.setAttribute("id", roomID);
            req.getRequestDispatcher("/desktop/room.jsp").forward(req, res);
        }
    }
}
