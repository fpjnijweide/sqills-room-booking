package nl.utwente.servlet.desktopInterface;

import nl.utwente.dao.RoomDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomOverviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        // Todo: Temporary check until type of IDs gets sorted out
        List<String> roomIDs = new ArrayList<>();
        for (int id : RoomDao.getAllRoomsIDs()) {
            roomIDs.add(String.valueOf(id));
        }
        req.setAttribute("roomIDs", roomIDs);
        req.setAttribute("availableRoomIDs", RoomDao.getCurrentlyAvailableRooms());
        req.getRequestDispatcher("/desktop/roomOverview.jsp").forward(req, res);
    }
}
