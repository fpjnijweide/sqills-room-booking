package nl.utwente.servlet.tabletInterface;

import nl.utwente.dao.RoomDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpecificRoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splitUri = uri.split("/");
        String roomName = splitUri[3];
        if (!RoomDao.isValidRoomName(roomName)) {
            res.setStatus(404);
            res.getWriter().write("404");
        } else {
            req.setAttribute("roomName", roomName); // TODO maybe change "id" thing
            req.getRequestDispatcher("/tablet/specific-room.jsp").forward(req, res);
        }
    }
}
