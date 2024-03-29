package nl.utwente.servlet.tabletInterface;

import nl.utwente.dao.RoomDao;
import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SpecificRoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] splitUri = uri.split("/");
        String roomName = splitUri[splitUri.length - 1];
        Connection connection = null;
        try {
            connection =DatabaseConnectionFactory.getConnection();
            if (!RoomDao.isValidRoomName(roomName,connection)) {
                connection.commit();
                connection.close();
                res.setStatus(404);
                res.getWriter().write("404 The room name could not be found.");
            } else {
                req.setAttribute("roomName", roomName); // TODO maybe change "id" thing
                req.getRequestDispatcher("/tablet/specific-room.jsp").forward(req, res);
            }
        } catch (DAOException | SQLException e) {
            res.setStatus(500);
            res.getWriter().write("Something went terribly wrong");;
        }
    }
}
