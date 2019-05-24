package nl.utwente.servlet.desktopInterface;

import nl.utwente.dao.RoomDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoomOverviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        req.setAttribute("roomIDs", RoomDao.getAllRoomsIDs());
        req.setAttribute("availableRoomIDs", RoomDao.getCurrentlyAvailableRooms());
        for (String key : RoomDao.getCurrentlyAvailableRooms()) {
            System.out.println(key);
        }
        req.getRequestDispatcher("/desktop/roomOverview.jsp").forward(req, res);
    }
}
