package nl.utwente.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DesktopInterfaceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        res.getWriter().write("fuck off");
        req.getRequestDispatcher("/desktop/index.jsp").forward(req, res);
    }
}
