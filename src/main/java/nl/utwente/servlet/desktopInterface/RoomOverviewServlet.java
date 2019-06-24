package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;
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
    // Todo: @Marten sort out names of variables
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        if (req.getSession().getAttribute(AuthenticationFilter.principalName)==null){
            req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
        } else {
            List<String> roomNames = new ArrayList<>();
            for (String name : RoomDao.getAllRoomNames()) {
                roomNames.add(name);
            }
            req.setAttribute("roomIDs", roomNames);
            req.setAttribute("availableRoomIDs", RoomDao.getCurrentlyAvailableRooms());
            req.getRequestDispatcher("/desktop/roomOverview.jsp").forward(req, res);
        }
    }
}
