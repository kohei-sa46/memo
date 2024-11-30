package memo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCheckFilter implements Filter {
   
    private UserDAO userDao;

    public void init(FilterConfig config) throws ServletException {
    
        userDao = new UserDAO();
    }

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String userId = req.getParameter("user_id");
        String password = req.getParameter("password");

        if (userId != null && password != null) {
            try {
                boolean isValidUser = userDao.validateUser(userId, password);
                if (isValidUser) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", userId);
                    chain.doFilter(req, res);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        req.setAttribute("errorMessage", "IDまたはパスワードが違います");
        RequestDispatcher disp = req.getRequestDispatcher("/register.jsp");
        disp.forward(req, res);
    }

}
