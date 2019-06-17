package nl.utwente.servlet.desktopInterface;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (hasCookie(req.getCookies())){
            req.getRequestDispatcher("/desktop/book.jsp").forward(req, res);
        } else {
            System.out.println("wf2g2");
        }

    }

    public boolean hasCookie(Cookie[] cookies){
        for (Cookie c : cookies){
            if (c.getName().equals("fuck")){
                System.out.println(c.getName());
                return true;
            }
        }
        return false;
    }
}
