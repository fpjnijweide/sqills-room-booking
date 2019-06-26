package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getSession().getAttribute(AuthenticationFilter.principalName)==null){
            req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
        } else {
            req.getRequestDispatcher("/desktop/book.jsp").forward(req, res);
        }
    }
}
