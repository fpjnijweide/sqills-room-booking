package nl.utwente.servlet.tabletInterface;

import nl.utwente.dao.RoomDao;
import nl.utwente.exceptions.DAOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexServlet extends HttpServlet {
    @Override
    //TODO @matren rename
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setAttribute("roomNames", RoomDao.getAllRoomNames());
            System.out.println(RoomDao.getAllRoomNames());
            req.getRequestDispatcher("/tablet/index.jsp").forward(req, res);
        } catch (DAOException e) {
            res.setStatus(500);
            res.getWriter().write("Something went terribly wrong");
        }


    }
}
