package nl.utwente.servlet.desktopInterface;

import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.UserDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            if (req.getSession().getAttribute(AuthenticationFilter.principalName) == null) {
                req.getRequestDispatcher("/desktop/login.jsp").forward(req, res);
            } else {
                String email = (String) req.getSession().getAttribute("principalName");
                User user = UserDao.getUserFromEmail(email);

                if (user.isAdministrator()) {
                    req.setAttribute("users", UserDao.getAllUsers());
                    req.getRequestDispatcher("/desktop/admin.jsp").forward(req, res);
                } else {
                    res.setStatus(403);
                    res.getWriter().write("You do not have permissions to access this page.");
                }
            }
        } catch (InvalidEmailException | DAOException e) {
            res.setStatus(500);
            res.getWriter().write(e.getMessage());
        }
    }
}

